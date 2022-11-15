package io.chealth.plugins.accuchekguide

import com.currenthealth.peripheralframework.*
import com.currenthealth.peripheralframework.internal.characteristicdata.GlucoseMeasurementData
import com.getcapacitor.JSObject
import com.getcapacitor.Plugin
import com.getcapacitor.annotation.CapacitorPlugin
import com.getcapacitor.PluginMethod
import com.getcapacitor.PluginCall
import io.chealth.plugins.accuchekguide.util.LogUtil

@CapacitorPlugin(name = "AccuChekGuide")
class AccuChekGuidePlugin : Plugin() {
    private val TAG = "AccuChekGuidePlugin"
    private val implementation = AccuChekGuide()
    private lateinit var pluginCall: PluginCall
    private lateinit var peripheralManager: PeripheralManager
    private lateinit var peripheralCallbacksHandler: PeripheralCallbacksHandler

    @PluginMethod
    fun onAction(call: PluginCall) {
        pluginCall = call
        //TODO use peripheral id coming from connect
        /*
         * Following will trigger scanning and further chain of events like connecting to bluetooth,
         * enabling notifications and calling needed glucose characteristics.
         * On scan for targeted device called whole process will wait for device to be discovered.
         *
         * All this is done in one go, without another 'action' trigger to connect and get readings because Accuchek Guide device enables
         * itself to be discovered only when it's in following data transfer mode.
         * 1. When live test is done, reading is saved in log and most recent data needs to be fetched.
         * 2. When manually data is transferred from old readings.
         */
        //peripheralManager.scanForTargetDevice("48:70:1E:A9:8F:E3", ::scanStartedCallback, ::scanResultCallback, ::scanStoppedCallback)
        peripheralManager = PeripheralManager(context)
        peripheralCallbacksHandler = PeripheralCallbacksHandler(peripheralManager)
        peripheralCallbacksHandler.scanForTargetDevice("48:70:1E:A9:8F:E3")

        val value = call.getString("connect")
        val ret = JSObject()
        ret.put("value", value?.let { implementation.echo(it) })
        call.resolve(ret)
    }
}