import { registerPlugin } from '@capacitor/core';
const AccuChekGuide = registerPlugin('AccuChekGuide', {
    web: () => import('./web').then(m => new m.AccuChekGuideWeb()),
});
export * from './definitions';
export { AccuChekGuide };
//# sourceMappingURL=index.js.map