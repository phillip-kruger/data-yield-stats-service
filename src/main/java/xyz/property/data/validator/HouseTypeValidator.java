package xyz.property.data.validator;

import xyz.property.data.annotations.ValidHouseType;
import xyz.property.data.model.HouseType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class HouseTypeValidator implements ConstraintValidator<ValidHouseType, String> {

    List<String> valueList = null;

    @Override
    public void initialize(ValidHouseType constraintAnnotation) {
        valueList = new ArrayList<>();

        @SuppressWarnings("rawtypes")
        Enum[] enumValArr = HouseType.class.getEnumConstants();

        for (@SuppressWarnings("rawtypes") Enum enumVal : enumValArr) {
            valueList.add(enumVal.toString().toUpperCase());
        }
    }

    @Override
    public boolean isValid(String houseType, ConstraintValidatorContext constraintValidatorContext) {
        AtomicBoolean response = new AtomicBoolean(false);
        try {
            response.set(valueList.contains(houseType.toUpperCase()));
        } catch (IllegalArgumentException e) {
            response.set(false);
        }
        catch (NullPointerException e){
            response.set(true); //HouseType is a non mandatory field.
        }
        return response.get();
    }
}
