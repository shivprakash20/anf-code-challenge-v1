package com.anf.core.configurations;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * Interface for Json Data Path configuration.
 *
 * @author Shiv
 */
@ObjectClassDefinition(name = "Form Dropdown: Json Data Path", description = "This is used to form dropdown json data path")
public @interface JsonDataPath {

    @AttributeDefinition(name = "Json Data Path", description = "Enter Json Data Path for dynamic dropdown", type = AttributeType.STRING)
    String getJsonDataPath() default "/content/dam/anf-code-challenge/exercise-1/countries.json";
}
