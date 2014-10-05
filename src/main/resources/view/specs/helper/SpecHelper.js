
define( ["jasmine"], function( jasmine ) {
    "use strict";

    beforeEach(function() {
    	jasmine.base.CATCH_EXCEPTIONS = false;

    	jasmine.base.addMatchers({

    		toHaveMemberWithName: function() {
    			return {
    				compare: function(actual, expected) {
    					var object = actual;
    					return {
    						pass: object[expected] !== undefined
    					};
    				}
    			};
    		},

    		toBeExisting: function() {
    			return {
    				compare: function(actual, expected) {
    					return {
    						pass: actual.length > 0
    					};
    				}
    			};
    		},

            toBeEmpty: function() {
    			return {
    				compare: function(actual, expected) {
    					return {
    						pass: actual.length === 0
    					};
    				}
    			};
    		},


            toSelectNumberOfElements: function() {
    			return {
    				compare: function(actual, expected) {
    					return {
    						pass: actual.length === expected
    					};
    				}
    			};
    		},

    		toBeInstanceOf: function() {
    			return {
    				compare: function(actual, expected) {
    					var object = actual,
    						result = (object instanceof expected);

    					return {
    						pass: result
    					};
    				}
    			};
    		}

    	});
    });


    return "HELPER";
});