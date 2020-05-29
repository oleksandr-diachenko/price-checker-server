package oleksandrdiachenko.pricechecker.annotation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class PhoneNumberValidatorTest {

    @InjectMocks
    private PhoneNumberValidator validator;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @Test
    void shouldValidWhenPhoneNumberWithCountryCodeBracketsSpaceAndHyphen() {
        assertThat(validator.isValid("+380 (96) 555-01-25", constraintValidatorContext)).isTrue();
    }

    @Test
    void shouldValidWhenPhoneNumberWithCountryCodeBracketsSpaceAndHyphen2() {
        assertThat(validator.isValid("+38 (096) 555-01-25", constraintValidatorContext)).isTrue();
    }

    @Test
    void shouldInvalidWhenSimplePhoneNumber() {
        assertThat(validator.isValid("0965550125", constraintValidatorContext)).isFalse();
    }

    @Test
    void shouldInvalidWhenPhoneNumberWithSpaces() {
        assertThat(validator.isValid("096 555 0125", constraintValidatorContext)).isFalse();
    }

    @Test
    void shouldInvalidWhenPhoneNumberWithBracketsSpaceAndHyphen() {
        assertThat(validator.isValid("(096) 555-0125", constraintValidatorContext)).isFalse();
    }
}