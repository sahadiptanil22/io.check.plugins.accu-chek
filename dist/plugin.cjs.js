'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

var core = require('@capacitor/core');

const AccuChekGuide = core.registerPlugin('AccuChekGuide', {
    web: () => Promise.resolve().then(function () { return web; }).then(m => new m.AccuChekGuideWeb()),
});

class AccuChekGuideWeb extends core.WebPlugin {
    async echo(options) {
        console.log('ECHO', options);
        return options;
    }
}

var web = /*#__PURE__*/Object.freeze({
    __proto__: null,
    AccuChekGuideWeb: AccuChekGuideWeb
});

exports.AccuChekGuide = AccuChekGuide;
//# sourceMappingURL=plugin.cjs.js.map
