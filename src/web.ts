import { WebPlugin } from '@capacitor/core';

import type { AccuChekGuidePlugin } from './definitions';

export class AccuChekGuideWeb extends WebPlugin implements AccuChekGuidePlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
