package com.anf.core.configurations;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * Interface for Age Validation Node Path configuration.
 *
 * @author Shiv
 */
@ObjectClassDefinition(name = "Age Validation Limit Node", description = "This is used to get the Age Validator Limit Node")
public @interface AgeValidationNodePath {

    @AttributeDefinition(name = "Age Validation Limits", description = "Enter the age validation limit Node Path", type = AttributeType.STRING)
    String getAgeLimitPath() default "/etc/age";
}
