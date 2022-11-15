package io.chealth.plugins.accuchekguide

import com.currenthealth.peripheralframework.*
import com.currenthealth.peripheralframework.internal.characteristicdata.GlucoseMeasurementData
import io.chealth.plugins.accuchekguide.util.LogUtil

class PeripheralCallbacksHandler(var peripheralManager: PeripheralManager) {

    private val TAG = "PeripheralCallbacksHandler"

    /**
     * Callback method to be registered on [ScanManagerDelegate.onScanStarted] for Outcome of scanning start.
     *
     * @param outcome from [ScanManagerDelegate.onScanStarted]
     */
    fun scanStartedCallback(outcome: Outcome) {
        LogUtil.d(TAG, "scanStartedCallback Outcome:$outcome")
        if (outcome != Outcome.Success) {
            returnErrorMappedToFlowConstantsNeeded(outcome)
        }
    }

    /**
     * Callback method to be registered on [ScanManagerDelegate.onScanStopped] for Outcome of scanning stop.
     *
     * @param outcome from [ScanManagerDelegate.onScanStopped]
     */
    fun scanStoppedCallback(outcome: Outcome) {
        LogUtil.d(TAG, "scanStoppedCallback Outcome:$outcome")
        if (outcome != Outcome.Success) {
            returnErrorMappedToFlowConstantsNeeded(outcome)
        }
    }

    fun scanForTargetDevice(deviceId: String) {
        peripheralManager.scanForTargetDevice(deviceId, ::scanStartedCallback, ::scanResultCallback, ::scanStoppedCallback)
    }

    /**
     * Callback method to be registered on [ScanManagerDelegate] for outcome with [ScanResult]
     * and attempt to connect to target peripheral with [PeripheralManager.connectToTargetDevice]
     *
     * @param scanResult from [ScanManagerDelegate.onScanResult]
     */
    fun scanResultCallback(scanResult: ScanResult) {
        LogUtil.d(TAG, "scanResultCallback ScanResult:${scanResult.toString()}")
        peripheralManager.connectToTargetDevice(scanResult.peripheral, this::connectCallback, ::bluetoothStatusCallback, ::disconnectCallback)
    }

    fun bluetoothStatusCallback(bluetoothStatus: BluetoothStatus) {
        LogUtil.d(TAG, "onBluetoothStatusChanged BluetoothStatus:$bluetoothStatus")
    }

    /**
     * Callback method to be registered on [BluetoothManagerDelegate] for outcome with [BluetoothManagerDelegate.onConnectionSetupComplete]
     * and attempt to get readings from  [BluetoothPeripheralDelegate].
     *
     * @param outcome from [BluetoothManagerDelegate.onConnectionSetupComplete]
     * @param targetBluetoothPeripheral target peripheral to get readings from.
     */
    fun connectCallback(outcome: Outcome, targetBluetoothPeripheral: BluetoothPeripheral) {
        LogUtil.d(TAG, "connectCallback Outcome:$outcome")
        if (outcome == Outcome.Success) {
            peripheralManager.setPeripheralDelegateAndGetReadings(targetBluetoothPeripheral, ::readingsReceivedCallBack)
        } else {
            returnErrorMappedToFlowConstantsNeeded(outcome)
            //peripheralManager.releaseConnection(targetBluetoothPeripheral)
        }
    }

    fun disconnectCallback(outcome: Outcome, targetBluetoothPeripheral: BluetoothPeripheral) {
        LogUtil.d(TAG, "disconnectCallback Outcome:$outcome")
        if (outcome == Outcome.Success) {

        } else {
            returnErrorMappedToFlowConstantsNeeded(outcome)
            //peripheralManager.releaseConnection(targetBluetoothPeripheral)
        }
    }

    /**
     * Callback method to be registered on [BluetoothPeripheralDelegate.onCharacteristicValueUpdated] for readings.
     *
     * @param outcome from [BluetoothPeripheralDelegate.onCharacteristicValueUpdated]
     */
    fun readingsReceivedCallBack(glucoseMeasurementData: GlucoseMeasurementData?, outcome: Outcome) {
        glucoseMeasurementData?.glucoseConcentration
        LogUtil.d(TAG, "readingsReceivedCallBack glucoseMeasurementData:${glucoseMeasurementData.toString()}")
        if (outcome != Outcome.Success) {
            returnErrorMappedToFlowConstantsNeeded(outcome)
        }

    }

    /**
     * Get string constants mapped to [Outcome.Failure.reason] needed to return to Flow/Capacitor.
     */
    fun returnErrorMappedToFlowConstantsNeeded(outcome: Outcome): String {

        when (outcome) {

            Outcome.Success -> {
                LogUtil.d(TAG, "Outcome:${Outcome.Success}")
            }
            Outcome.Unknown -> {
                LogUtil.d(TAG, "Outcome:${Outcome.Unknown}")
            }
            Outcome.Failure(FailureReason.MISSING_PERMISSION) -> {
                LogUtil.d(TAG, "Outcome:${FailureReason.MISSING_PERMISSION}")
            }
            Outcome.Failure(FailureReason.BLUETOOTH_UNAVAILABLE) -> {
                LogUtil.d(TAG, "Outcome:${FailureReason.BLUETOOTH_UNAVAILABLE}")
            }
            Outcome.Failure(FailureReason.INSUFFICIENT_SECURITY) -> {
                LogUtil.d(TAG, "Outcome:${FailureReason.INSUFFICIENT_SECURITY}")
            }
            Outcome.Failure(FailureReason.NOT_ALLOWED) -> {
                LogUtil.d(TAG, "Outcome:${FailureReason.NOT_ALLOWED}")
            }
            Outcome.Failure(FailureReason.RESOURCE_UNAVAILABLE) -> {
                LogUtil.d(TAG, "Outcome:${FailureReason.RESOURCE_UNAVAILABLE}")
            }
            Outcome.Failure(FailureReason.PERIPHERAL_DISCONNECTED) -> {
                LogUtil.d(TAG, "Outcome:${FailureReason.PERIPHERAL_DISCONNECTED}")
            }
            Outcome.Failure(FailureReason.TIMEOUT) -> {
                LogUtil.d(TAG, "Outcome:${FailureReason.TIMEOUT}")
            }
            Outcome.Failure(FailureReason.GENERIC_FAILURE) -> {
                LogUtil.d(TAG, "Outcome:${FailureReason.GENERIC_FAILURE}")
            }
            Outcome.Failure(FailureReason.UNKNOWN) -> {
                LogUtil.d(TAG, "Outcome:${FailureReason.UNKNOWN}")
            }
            else -> {
                return "Unknown error."
            }

        }
        return "Unknown error."
    }
}