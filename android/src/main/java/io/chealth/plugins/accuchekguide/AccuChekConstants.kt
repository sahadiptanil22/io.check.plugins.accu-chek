package io.chealth.plugins.accuchekguide

object AccuChekConstants {
    const val GLUCOSE_SERVICE_ID = "00001808-0000-1000-8000-00805f9b34fb"
    const val GLUCOSE_MEASUREMENT_CHARACTERISTICS = "00002a18-0000-1000-8000-00805f9b34fb"
    const val GLUCOSE_MEASUREMENT_CONTEXT_CHARACTERISTICS = "00002a34-0000-1000-8000-00805f9b34fb"
    //Record Access Control Point Glucose characteristic
    const val RACP_GLUCOSE_CHARACTERISTICS = "00002a52-0000-1000-8000-00805f9b34fb"
    // Last records (Most recent)
    val bytesLastStored = byteArrayOf(0x01,0x06)
    // This needs to be called when device is re paired.
    val bytesRePair = byteArrayOf(0x00)



    // Section 1: Plugin Errors used internally to PFA
    /*"bleInitializationFailed" --> If BLE didn't Get initilised properly
    "actionNameNotFound" --> Action Name not provided or Undefined Action Name
    "deviceNotFound" --> Device Name not provided for scan
    // Section 2: Plugin Errors used to show some correponding UI to user
    "pluginTimeOutError" --> Time Out Error
    "bluetoothUnavailable" --> bluetoothUnavailable or resourceUnavailable
    "insufficientSecurity" --> insufficientSecurity
    "genericFailure" --> genericFailure or unknown
    "bluetoothOff" --> off
    "bluetoothUnavailable" --> unavailable or resetting*/
}