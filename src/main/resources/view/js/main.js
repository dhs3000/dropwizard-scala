
require(["jquery", "components/MultipleSelect"], function( $, MultipleSelect ) {
    "use strict";

    // Setting up application

    $(function() {
        $( "select[multiple]" ).each(function( index, elem ) {
            new MultipleSelect(elem);
        });
    });

});