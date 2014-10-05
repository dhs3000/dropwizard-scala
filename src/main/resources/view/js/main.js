
require.config({
   paths: {
       jquery: 'lib/jquery-2.1.1.min',
       lodash: 'lib/lodash.min'
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

require(["jquery", "components/MultipleSelect"], function( $, MultipleSelect ) {
    "use strict";

    // Setting up application

    $( "body" ).addClass( "with-js" );

    $(function() {
        $( "select[multiple]" ).each(function( index, elem ) {
            new MultipleSelect(elem);
        });
    });

});