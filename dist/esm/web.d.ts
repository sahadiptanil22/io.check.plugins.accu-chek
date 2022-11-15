import { WebPlugin } from '@capacitor/core';
import type { AccuChekGuidePlugin } from './definitions';
export declare class AccuChekGuideWeb extends WebPlugin implements AccuChekGuidePlugin {
    echo(options: {
        value: string;
    }): Promise<{
        value: string;
    }>;
}
