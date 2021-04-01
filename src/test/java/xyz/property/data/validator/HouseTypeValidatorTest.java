package xyz.property.data.validator;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HouseTypeValidatorTest {

    HouseTypeValidator validator;

    @BeforeAll
    void setup() {
        validator = new HouseTypeValidator();
        validator.initialize(null);
    }

    @Test
    void houseTypeValidatorShouldFailForInvalidHouseTypes() {
        assertThat(validator.isValid("terraced", null)).isEqualTo(false);
        assertThat(validator.isValid("semiDetached", null)).isEqualTo(false);
    }

    @Test
    void houseTypeValidatorShouldPassForValidHouseTypes() {
        assertThat(validator.isValid(null, null)).isEqualTo(true);
        assertThat(validator.isValid("detached_house", null)).isEqualTo(true);
        assertThat(validator.isValid("semi-detached_house", null)).isEqualTo(true);
        assertThat(validator.isValid("terraced_house", null)).isEqualTo(true);
        assertThat(validator.isValid("flat", null)).isEqualTo(true);
    }
}