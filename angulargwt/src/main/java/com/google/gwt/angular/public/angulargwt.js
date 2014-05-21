/**
 * Basic callback handler for AngularGWT modules in AngularJS applications
 * This requires compiling all GWT-modules into a single js-File with an AngularEntryPoint
 * e.g. by providing one top module containing  all other GWT-Modules as dependencies
 */
function angulargwt(topmodule) {
	window.angularGwtModuleLoaded = function(moduleName) {
		console.log("finished loading module " + moduleName);
		angular.bootstrap(document,[topmodule]);
	}
}
