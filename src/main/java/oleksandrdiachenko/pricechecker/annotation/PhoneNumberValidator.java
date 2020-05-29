package oleksandrdiachenko.pricechecker.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext constraintValidatorContext) {
        return phone.matches("^(\\+\\d{1,3}( )?)?((\\(\\d{2,3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{2}[- .]\\d{2}$");
    }
}
