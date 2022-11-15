import Foundation
import Capacitor
/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(AccuCheckGuidePlugin)
public class AccuCheckGuidePlugin: CAPPlugin {
    
    @objc func onAction(_ call: CAPPluginCall) {
            let actionName = call.getString("actionName") ?? ""
            call.resolve([
                "value": "Set up done"
            ])
        }

}
