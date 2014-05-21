# Validation Module for AngularGwt
## introducing the bean-validate directive
### allowing to use JSR-303 annotations of GWT on models 

carve-out of validation stuff from the tipcalc example to keep the main lib uncluttered

At present, you still have to provide your own validatorfactory and set it up in the .gwt.xml
this will change so that this is generated 

see tipcalc example and/or [GWT Validation documentation](http://www.gwtproject.org/doc/latest/DevGuideValidation.html)