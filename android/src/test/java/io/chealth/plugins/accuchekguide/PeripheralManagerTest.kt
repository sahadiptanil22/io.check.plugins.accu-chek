package io.chealth.plugins.accuchekguide

import android.content.Context
import com.currenthealth.peripheralframework.Outcome
import com.currenthealth.peripheralframework.ScanManager
import com.currenthealth.peripheralframework.ScanResult
import com.nhaarman.mockitokotlin2.times

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.stubbing.Answer

class PeripheralManagerTest {

    lateinit var manager: PeripheralManager
    var mockContext: Context = Mockito.mock(Context::class.java)

    var  mockScanManager: ScanManager = Mockito.mock(ScanManager::class.java)

    var mockScanResult: ScanResult = Mockito.mock(ScanResult::class.java)

    @Mock
    private lateinit var mockScanStartedCallback: (Outcome?) -> Unit
    @Mock
    private lateinit var mockScanResultCallback: (ScanResult?) -> Unit
    @Mock
    private lateinit var mockScanStoppedCallback: (Outcome?) -> Unit

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        manager = Mockito.spy(PeripheralManager(mockContext))
    }

    @Test
    fun getScanResultFetchedOnce() {
        var scanManager = Mockito.mockStatic(ScanManager::class.java)

        /*try (utilities: MockedStatic<ScanManager> = Mockito.mockStatic(ScanManager.class)) {
            utilities.when(ScanManager.getInstance(any())).thenReturn(mockScanManager);
            assertThat(StaticUtils.name()).isEqualTo("Eugen");
        }*/

            Mockito.mockStatic(ScanManager::class.java).use { mockedUuid ->
                Mockito.`when`(mockScanManager.startScan(any())).thenAnswer(
                    Answer<Any?> {
                        mockScanManager.delegate?.get()?.onScanResult(mockScanResult)
                    })
                mockedUuid.`when`<Any> { ScanManager.getInstance(any()) }
                    .thenReturn(mockScanManager)

                manager.scanForTargetDevice("anyDeviceId",
                    mockScanStartedCallback,
                    mockScanResultCallback,
                    mockScanStoppedCallback)

                //verify(mockScanManager, times(1)).
            }




                /*val result = cut.createOrder("MacBook Pro", 2L, null)
                assertEquals("8d8b30e3-de52-4f1c-a71c-9905a8043dac", result.id)*/
    }
}