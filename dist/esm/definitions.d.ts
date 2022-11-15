import {PluginCallback} from "@capacitor/core";

export interface AccuChekGuidePlugin {
    onAction(options: {actionName: string, ourContext: any}, onComplete: PluginCallback): Promise< { type: string, data: any }>;
}
