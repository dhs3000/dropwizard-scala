
require.config({

    baseUrl: '/js/',

    paths: {
       jquery: 'lib/jquery-2.1.1.min',
       lodash: 'lib/lodash.min',

       jasmine: '/specs/jasmine',
       specs: '/specs/specs'


    },

    map: {
     // '*' means all modules will get 'jquery-private'
     // for their 'jquery' dependency.
     '*': { 'jquery': 'lib/jquery-private' },

     // 'jquery-private' wants the real jQuery module
     // though. If this line was not here, there would
     // be an unresolvable cyclic dependency.
     'lib/jquery-private': { 'jquery': 'jquery' }
    }
});

require([
    "jasmine",
    "/specs/helper/SpecHelper.js",
    "specs"
    ], function( jasmine, helper, specs ) {
    "use strict";

    var specToUrl = function(spec) {
        return "/specs/spec/" + spec + ".js" ;
    };


    require(specs.map(specToUrl), function() {
        jasmine.showTestResults();
    });
});