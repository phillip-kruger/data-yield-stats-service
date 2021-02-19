package xyz.property.data.annotations;


import xyz.property.data.validator.HouseTypeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Documented
@Constraint(validatedBy = HouseTypeValidator.class)
@Target({
        METHOD,
        FIELD,
        ANNOTATION_TYPE,
        CONSTRUCTOR,
        PARAMETER,
        TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
@SupportedValidationTarget(ValidationTarget.ANNOTATED_ELEMENT)
@ReportAsSingleViolation
public @interface HouseType {
    String value() default "";
    String message() default  "Invalid house type: must be one of flat, terraced_house, semi-detached_house, detached_house";

    Class<? extends Payload>[] payload() default {};
    Class<?>[] groups() default {};
}
