package xyz.property.data.validator;

import lombok.val;
import xyz.property.data.annotations.HouseType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.concurrent.atomic.AtomicBoolean;

public class HouseTypeValidator implements ConstraintValidator<HouseType, String> {

    @Override
    public void initialize(HouseType constraintAnnotation) {
    }

    @Override
    public boolean isValid(String houseType, ConstraintValidatorContext constraintValidatorContext) {
        val response = new AtomicBoolean(false);
        try {
            Enum.valueOf(xyz.property.data.model.HouseType.class, houseType);
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
