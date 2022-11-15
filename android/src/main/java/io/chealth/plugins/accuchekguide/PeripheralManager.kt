package io.chealth.plugins.accuchekguide

import android.content.Context
import android.util.Log
import com.currenthealth.peripheralframework.*
import com.currenthealth.peripheralframework.PeripheralFramework.initialize
import com.currenthealth.peripheralframework.PeripheralFramework.logger
import com.currenthealth.peripheralframework.internal.characteristicdata.GlucoseMeasurementData
import io.chealth.plugins.accuchekguide.AccuChekConstants.GLUCOSE_MEASUREMENT_CHARACTERISTICS
import io.chealth.plugins.accuchekguide.AccuChekConstants.GLUCOSE_MEASUREMENT_CONTEXT_CHARACTERISTICS
import io.chealth.plugins.accuchekguide.AccuChekConstants.GLUCOSE_SERVICE_ID
import io.chealth.plugins.accuchekguide.AccuChekConstants.RACP_GLUCOSE_CHARACTERISTICS
import io.chealth.plugins.accuchekguide.AccuChekConstants.bytesLastStored
import io.chealth.plugins.accuchekguide.AccuChekConstants.bytesRePair
import io.chealth.plugins.accuchekguide.util.LogUtil
import java.lang.ref.WeakReference
import java.util.*

/**
 * Class to help with Bluetooth initialization, bluetooth connectivity
 * and transmission of AccuCheck characteristics through sdk.
 *
 * @param context [Context] used to initialize [PeripheralFramework] and it's Bluetooth utilities.
 */
class PeripheralManager internal constructor(private val context: Context) {
    private val TAG = "PeripheralManager"
    lateinit var bluetoothManager: BluetoothManager
    var scanResultFetchedOnce:Boolean = false

    init {
        initialize(context)
        logger = LogcatDefaultLogger()
    }


    /**
     * Scan for target device and get callback if found.
     * This sets the [ScanManagerDelegate] to get the scan outcome and result.
     *
     * @param deviceId peripheral id to be in [ScanCriteria] as filter.
     * @param scanCallback to get the [Outcome] of Scan started.
     * @param scanResultCallback to get the [ScanResult] containing the target [BluetoothPeripheral]
     */
    fun scanForTargetDevice(
        deviceId: String,
        scanStartedCallback: (Outcome) -> Unit,
        scanResultCallback: (ScanResult) -> Unit,
        scanStoppedCallback: (Outcome) -> Unit
    ) {
        val scanManager = ScanManager.getInstance(context)
        val scanManagerDelegate: ScanManagerDelegate = object : ScanManagerDelegate {
            override fun onScanStarted(criteria: ScanCriteria?, outcome: Outcome) {
                scanStartedCallback(outcome)
                LogUtil.d(TAG, "ScanCriteria onScanStarted Outcome:$outcome")
            }

            override fun onScanStopped(criteria: ScanCriteria?, outcome: Outcome) {
                scanStoppedCallback(outcome)
                LogUtil.d(TAG, "ScanCriteria onScanStopped Outcome:$outcome")
            }

            override fun onScanResult(scanResult: ScanResult) {
                if(!scanResultFetchedOnce){
                    scanResultCallback(scanResult)
                    scanResultFetchedOnce = true
                    LogUtil.d(TAG, "ScanResult: mac" + scanResult.peripheral.macAddress)
                    LogUtil.d(TAG, "ScanResult: \${scanResult.peripheral.name}" + scanResult.peripheral.name)
                    scanManager.stopScan()
                }

            }
        }
        val scanManagerWeakReference = WeakReference(scanManagerDelegate)
        scanManager.delegate = scanManagerWeakReference

        val scanCriteria = ScanCriteria(macAddress = deviceId)
        scanManager.startScan(scanCriteria)
    }

    /**
     * Attempts to connect to the passed [BluetoothPeripheral].
     * Sets [BluetoothManagerDelegate] to get connection attempt result.
     * This sets the [ScanManagerDelegate] to get the scan outcome and result.
     *
     * @param targetBluetoothPeripheral Peripheral to attempt connection.
     * @param connectCallback callback method to call on connection success.
     */
    fun connectToTargetDevice(
        targetBluetoothPeripheral: BluetoothPeripheral,
        connectCallback: (Outcome, BluetoothPeripheral) -> Unit,
        bluetoothStatusCallback: (BluetoothStatus) -> Unit,
        disconnectCallback: (Outcome, BluetoothPeripheral) -> Unit
    ) {
        bluetoothManager = BluetoothManager.getInstance(context)
        val bluetoothManagerDelegate: BluetoothManagerDelegate = object : BluetoothManagerDelegate {
            override fun onBluetoothStatusChanged(bluetoothStatus: BluetoothStatus) {
                bluetoothStatusCallback(bluetoothStatus)
                LogUtil.d(TAG, "BluetoothManagerDelegate onBluetoothStatusChanged:$bluetoothStatus")
            }

            override fun onConnectionSetupComplete(
                peripheral: BluetoothPeripheral,
                outcome: Outcome
            ) {
                connectCallback(outcome, peripheral)
                LogUtil.d(TAG, "BluetoothManagerDelegate onConnectionSetupComplete Outcome:$outcome")
            }

            override fun onDisconnect(peripheral: BluetoothPeripheral, outcome: Outcome) {
                disconnectCallback(outcome, peripheral)
                LogUtil.d(TAG, "BluetoothManagerDelegate onDisconnect Outcome:$outcome")
            }
        }
        val bluetoothManagerDelegateWeakReference = WeakReference(bluetoothManagerDelegate)
        bluetoothManager.delegate = bluetoothManagerDelegateWeakReference
        bluetoothManager.connect(targetBluetoothPeripheral)
        //bluetoothManager.disconnect(targetBluetoothPeripheral)
    }

    /**
     * Sets [BluetoothPeripheralDelegate] to get the notification status and characteristics readings.
     *
     * @param connectedBluetoothPeripheral Peripheral to set the delegate on.
     * @param readingsReceivedCallBack callback method to call on Glucose characteristics readings received.
     */
    fun setPeripheralDelegateAndGetReadings(
        connectedBluetoothPeripheral: BluetoothPeripheral,
        readingsReceivedCallBack: (GlucoseMeasurementData?, Outcome) -> Unit
    ) {
        val bluetoothPeripheralDelegate: BluetoothPeripheralDelegate =
            object : BluetoothPeripheralDelegate {
                override fun onMtuUpdated(
                    peripheral: BluetoothPeripheral,
                    mtu: Int,
                    outcome: Outcome
                ) {
                    LogUtil.d(TAG, "BluetoothPeripheralDelegate onMtuUpdated:$mtu Outcome:$outcome")
                }

                override fun onNotificationsEnabled(
                    peripheral: BluetoothPeripheral,
                    characteristicUuid: UUID,
                    serviceUuid: UUID,
                    outcome: Outcome
                ) {
                    LogUtil.d(TAG, "BluetoothPeripheralDelegate onNotificationsEnabled Outcome:$outcome")

                    //Calling Record Access Control Point to get characteristics updated of most recent saved record
                    /*connectedBluetoothPeripheral.writeToCharacteristic(
                        UUID.fromString(RACP_GLUCOSE_CHARACTERISTICS),
                        UUID.fromString(GLUCOSE_SERVICE_ID),
                        bytesRePair,
                        CharacteristicWriteType.WITH_RESPONSE
                    )*/
                    //Calling Record Access Control Point to get characteristics updated of most recent saved record
                    connectedBluetoothPeripheral.writeToCharacteristic(
                        UUID.fromString(RACP_GLUCOSE_CHARACTERISTICS),
                        UUID.fromString(GLUCOSE_SERVICE_ID),
                        bytesLastStored,
                        CharacteristicWriteType.WITH_RESPONSE
                    )
                }

                override fun onNotificationsDisabled(
                    peripheral: BluetoothPeripheral,
                    characteristicUuid: UUID,
                    serviceUuid: UUID,
                    outcome: Outcome
                ) {
                    LogUtil.d(TAG, "BluetoothPeripheralDelegate onNotificationsDisabled Outcome:$outcome")
                }

                override fun onCharacteristicValueUpdated(
                    peripheral: BluetoothPeripheral,
                    characteristicUuid: UUID,
                    serviceUuid: UUID,
                    value: ByteArray,
                    outcome: Outcome
                ) {
                    LogUtil.d(TAG, "BluetoothPeripheralDelegate onCharacteristicValueUpdated raw bytes:$value Outcome:$outcome")
                    if (characteristicUuid == UUID.fromString(GLUCOSE_MEASUREMENT_CHARACTERISTICS)) {
                        val glucoseMeasurementData: GlucoseMeasurementData? = GlucoseMeasurementData.fromBytes(value.toUByteArray())
                        LogUtil.d(
                            TAG,
                            "BluetoothPeripheralDelegate onCharacteristicValueUpdated GlucoseMeasurementData " +
                                    "${glucoseMeasurementData?.toString()}"
                        )
                        readingsReceivedCallBack(glucoseMeasurementData, outcome)
                        //releaseConnection(peripheral)
                    }

                }

                override fun onCharacteristicWrite(
                    peripheral: BluetoothPeripheral,
                    characteristicUuid: UUID,
                    serviceUuid: UUID,
                    value: ByteArray,
                    outcome: Outcome
                ) {
                    LogUtil.d(TAG, "BluetoothPeripheralDelegate onCharacteristicWrite data:$value Outcome:$outcome")
                }

                override fun onDescriptorRead(
                    peripheral: BluetoothPeripheral,
                    descriptorUuid: UUID,
                    characteristicUuid: UUID,
                    serviceUuid: UUID,
                    value: ByteArray,
                    outcome: Outcome
                ) {
                    LogUtil.d(TAG, "BluetoothPeripheralDelegate onDescriptorRead bytes:$value Outcome:$outcome")
                }

                override fun onDescriptorWrite(
                    peripheral: BluetoothPeripheral,
                    descriptorUuid: UUID,
                    characteristicUuid: UUID,
                    serviceUuid: UUID,
                    value: ByteArray,
                    outcome: Outcome
                ) {
                    LogUtil.d(
                        TAG, "BluetoothPeripheralDelegate onDescriptorWrite bytes:$value Outcome:$outcome"
                    )
                }
            }
        val bluetoothPeripheralDelegateWeakReference = WeakReference(bluetoothPeripheralDelegate)
        connectedBluetoothPeripheral.delegate = bluetoothPeripheralDelegateWeakReference
        //Enable notifications with Glucose Measurement UUID
        connectedBluetoothPeripheral.enableNotifications(
            UUID.fromString(GLUCOSE_MEASUREMENT_CHARACTERISTICS),
            UUID.fromString(GLUCOSE_SERVICE_ID)
        )
        /*connectedBluetoothPeripheral.enableNotifications(
            UUID.fromString(GLUCOSE_MEASUREMENT_CONTEXT_CHARACTERISTICS),
            UUID.fromString(GLUCOSE_SERVICE_ID)
        )*/
    }

    fun releaseConnection(bluetoothPeripheral: BluetoothPeripheral){
        if(::bluetoothManager.isInitialized){
            bluetoothManager.disconnect(bluetoothPeripheral)
        }
    }
}