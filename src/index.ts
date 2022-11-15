import { registerPlugin } from '@capacitor/core';

import type { AccuChekGuidePlugin } from './definitions';

const AccuChekGuide = registerPlugin<AccuChekGuidePlugin>('AccuChekGuide', {
  web: () => import('./web').then(m => new m.AccuChekGuideWeb()),
});

export * from './definitions';
export { AccuChekGuide };
