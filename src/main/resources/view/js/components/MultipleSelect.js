
define(["jquery"], function( $ ) {
    "use strict";

    function MultipleSelect( originalSelect ) {

        var $originalSelect = $( originalSelect );

        this.$select = $originalSelect.clone()
                .removeAttr( "multiple" )
                .attr('data-group-id', MultipleSelect.groupSelectCount++);

        this.maxSelections = this.$select.find( "option" ).length;
        this.selectionCount = 1;

        this.$addButton = $( '<span role="button" class="icon-button">' +
                             '  <i class="fa fa-plus-circle"></i>' +
                             '</span>' )
                             .click( this.addSelect.bind( this ) );

        this.$select.prepend( $( '<option default="default" selected="selected" value="">...</option>' ) );

        $originalSelect.replaceWith( this.newSelect().add( this.$addButton ) );
    }

    MultipleSelect.groupSelectCount = 0;

    MultipleSelect.prototype.newSelect = function() {
        var $result = this.$select.clone();

        $result.attr( 'id', $result.attr('id') + "_" + this.selectionCount++ );
        $result.on( 'change', this.selected.bind( this ) );

        return $result;
    };

    MultipleSelect.prototype.addSelect = function( event ) {
        this.newSelect().insertBefore( this.$addButton );
        if ( this.selectionCount > this.maxSelections ) {
            this.$addButton.hide();
            this.addSelect = function() {};
        }
    };

    MultipleSelect.prototype.selected = function( event ) {
        var $target = $( event.target ),
            $group = $( 'select[data-group-id="' + $target.data('group-id') + '"]' );

        if ( $target.find( 'option[value!=""]:selected' ).length ) {
            $target.unbind( 'change' );
        }

        if ( $group.find( 'option[value!=""]:selected' ).length < $group.length ) {
            return;
        }

        this.addSelect();
    };


    return MultipleSelect;

});