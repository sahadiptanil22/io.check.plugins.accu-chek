package io.chealth.plugins.accuchekguide

import android.content.Context
import com.currenthealth.peripheralframework.*
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.mockito.stubbing.Answer

@RunWith(MockitoJUnitRunner::class)
class PeripheralCallbacksHandlerTest {

    var TAG = "AccuChekGuidePluginTest"

    val mockPeripheralManager = Mockito.mock(PeripheralManager::class.java)

    val mockScanManager = Mockito.mock(ScanManager::class.java)

    val mockScanResult = Mockito.mock(ScanResult::class.java)

    val mockBluetoothPeripheral = Mockito.mock(BluetoothPeripheral::class.java)

    @Mock
    lateinit var mMockContext: Context

    val accuChekGuidePlugin = AccuChekGuidePlugin()//Mockito.mock(AccuChekGuidePlugin::class.java)//AccuChekGuidePlugin()

    //var accuChekGuidePluginSpy: AccuChekGuidePlugin = Mockito.spy(AccuChekGuidePlugin())

    lateinit var accuPeripheralCallbacksHandlerSpy: PeripheralCallbacksHandler


    lateinit var peripheralManagerSpy: PeripheralManager

    @Mock
    private lateinit var mockScanStartedCallback: (Outcome?) -> Unit

    @Mock
    private lateinit var mockScanResultCallback: (ScanResult?) -> Unit

    @Mock
    private lateinit var mockScanStoppedCallback: (Outcome?) -> Unit

    @Mock
    private lateinit var mockBluetoothStatusCallback: (BluetoothStatus?) -> Unit

    @Mock
    private lateinit var mockConnectCallback: (Outcome,BluetoothPeripheral) -> Unit

    @Mock
    private lateinit var mockDisconnectCallback: (Outcome,BluetoothPeripheral) -> Unit

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        accuPeripheralCallbacksHandlerSpy = Mockito.spy(PeripheralCallbacksHandler(mockPeripheralManager))
    }

    @After
    fun tearDown() {
    }

    @Test
    fun scanStartedCallback_when_failure_should_call_returnErrorMappedToFlowConstantsNeeded() {
        accuPeripheralCallbacksHandlerSpy.scanStartedCallback(Outcome.Failure(FailureReason.GENERIC_FAILURE))

        Mockito.verify(accuPeripheralCallbacksHandlerSpy, Mockito.times(1))
            .returnErrorMappedToFlowConstantsNeeded(Outcome.Failure(FailureReason.GENERIC_FAILURE))
    }

    @Test
    fun scanStoppedCallback_when_failure_should_call_returnErrorMappedToFlowConstantsNeeded() {
        accuPeripheralCallbacksHandlerSpy.scanStoppedCallback(Outcome.Failure(FailureReason.GENERIC_FAILURE))

        Mockito.verify(accuPeripheralCallbacksHandlerSpy, Mockito.times(1))
            .returnErrorMappedToFlowConstantsNeeded(Outcome.Failure(FailureReason.GENERIC_FAILURE))
    }

    @Test
    fun scanForTargetDevice_should_call_PeripheralManager_scanForTargetDevice() {
        Mockito.doNothing().`when`(mockPeripheralManager).scanForTargetDevice(
            anyString(),
            any(),
            any(),
            any()
        )

        accuPeripheralCallbacksHandlerSpy.scanForTargetDevice("someDeviceId")

        Mockito.verify(mockPeripheralManager, Mockito.times(1)).scanForTargetDevice(eq("someDeviceId"),
            eq(accuPeripheralCallbacksHandlerSpy::scanStartedCallback),
            eq(accuPeripheralCallbacksHandlerSpy::scanResultCallback),
            eq(accuPeripheralCallbacksHandlerSpy::scanStoppedCallback))
    }

    @Test
    fun scanResultCallback_should_call_connectToTargetDevice() {
        Mockito.`when`(mockScanResult.peripheral).thenReturn(mockBluetoothPeripheral)
        Mockito.doNothing().`when`(mockPeripheralManager).connectToTargetDevice(
            any(),
            any(),
            any(),
            any()
        )
        accuPeripheralCallbacksHandlerSpy.scanResultCallback(mockScanResult)

        Mockito.verify(mockPeripheralManager, Mockito.times(1)).connectToTargetDevice(eq(mockBluetoothPeripheral),
            eq(accuPeripheralCallbacksHandlerSpy::connectCallback),
            eq(accuPeripheralCallbacksHandlerSpy::bluetoothStatusCallback),
            eq(accuPeripheralCallbacksHandlerSpy::disconnectCallback))
    }

    @Test
    fun bluetoothStatusCallback_should_call_BluetoothPeripheralDelegate() {

    }

    @Test
    fun scanForTargetDevice() {
    }

    @Test
    fun getPeripheralManager() {
    }

    @Test
    fun setPeripheralManager() {
    }
}