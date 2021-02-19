package xyz.property.data.validator;

import xyz.property.data.annotations.ValidHouseType;
import xyz.property.data.model.HouseType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.concurrent.atomic.AtomicBoolean;

public class HouseTypeValidator implements ConstraintValidator<ValidHouseType, String> {

    @Override
    public void initialize(ValidHouseType constraintAnnotation) {
    }

    @Override
    public boolean isValid(String houseType, ConstraintValidatorContext constraintValidatorContext) {
        AtomicBoolean response = new AtomicBoolean(false);
        try {
            Enum.valueOf(HouseType.class, houseType);
            response.set(true);
        } catch (IllegalArgumentException e) {
            response.set(false);
        }
        catch (NullPointerException e){
            response.set(true); //HouseType is a non mandatory field.
        }
        return response.get();
    }
}
