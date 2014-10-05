
define( ["jquery", "components/MultipleSelect"], function( $, MultipleSelect ) {
    "use strict";

    describe( "A MultipleSelect", function() {

        var $container,
            multiselect,
            element = function( selector ) {
                return $container.find( selector );
            };

        beforeEach(function() {

            $container = $('<div id="test_container" />');

            $( '<select id="m" multiple="multiple">' +
               '    <option>HALLO</option>' +
               '    <option>WELT</option>' +
               '</select>').appendTo( $container );

            multiselect = $container.find( 'select[multiple]' )[0];
        });

        it( "should replace a given html-multi-select by its own markup", function() {
            var s = new MultipleSelect( multiselect );

            expect( element( "#" + multiselect.id ) ).toBeEmpty();
            expect( element( '[data-group-id="' + multiselect.id + '"]' ) ).toSelectNumberOfElements( 1 );

        });

        it( "should have an empty entry", function() {
            var s = new MultipleSelect( multiselect );

            expect( element( 'option[default="default"][selected="selected"][value=""]' ) ).toBeExisting();
        });

        describe( "should have an add button", function() {
            it( "that adds a new select when clicked", function() {
                var s = new MultipleSelect( multiselect ),
                    $button = element( '[role="button"]' );

                expect( $button ).toBeExisting();
                expect( element( '[data-group-id="' + multiselect.id + '"]' ) ).toSelectNumberOfElements( 1 );

                $button.click();
                expect( element( '[data-group-id="' + multiselect.id + '"]' ) ).toSelectNumberOfElements( 2 );


            });

            it( "that removes itself when no more select options are available", function() {
                var s = new MultipleSelect( multiselect ),
                    $button = element( '[role="button"]' );

                expect( $button ).toBeExisting();

                $button.click();
                expect( element( '[role="button"]' ) ).toBeEmpty();


            });
        });

    });

    return "SPEC";
});
