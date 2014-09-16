$( document ).ready(function() {
    $( "select[multiple]" ).each(function( index, elem ) {
        new MultipleSelect(elem);
    });
});
