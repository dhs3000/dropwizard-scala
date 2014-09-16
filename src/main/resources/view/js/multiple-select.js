(function( window, $, _ ) {

function MultipleSelect( container ) {
    _.bindAll( this, 'addSelect', 'selected' );

    var $container = $( container );

    this.$select = $container.clone()
            .removeAttr( "multiple" );

    this.maxSelections = this.$select.find( "option" ).length;
    this.selectionCount = 1;

    this.$addButton = $( '<span role="button" class="icon-button">' +
                         '  <i class="fa fa-plus-circle"></i>' +
                         '</span>' )
                         .click( this.addButtonClicked ); // TODO: One click handler per document

    this.$select.prepend( $( '<option default="default" selected="selected" value="">...</option>' ) );

    $container.replaceWith( this.newSelect().add( this.$addButton ) );
}

MultipleSelect.prototype.newSelect = function() {
    var result = this.$select.clone();
    result.attr('id', result.attr('id') + "_" + this.selectionCount++);

    result.on('change', this.selected);
    return result;
};

MultipleSelect.prototype.addSelect = function( event ) {
    this.newSelect().insertBefore( this.$addButton );
    if ( this.selectionCount > this.maxSelections ) {
        this.$addButton.hide();
        this.addSelect = function() {};
    }
};

MultipleSelect.prototype.selected = function( event ) {
    $( event.target ).unbind('change');
    this.addSelect();
};


window.MultipleSelect = MultipleSelect;

}( window, $, _ ));