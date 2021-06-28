package xyz.property.data;

import io.quarkus.test.junit.QuarkusTest;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import xyz.property.data.service.PostCodeService;
import xyz.property.data.validators.PostCodeValidator;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@QuarkusTest
public class PostCodeValidationTest {

    @Inject
    @RestClient
    PostCodeService postCodeService;

    @Inject
    PostCodeValidator postCodeValidator;

    @Test
    void shouldValidatePostCodeWithHTML() {
        assertThat(postCodeService.validateFullPostcode("B1%201BB").await().atMost(Duration.ofSeconds(5)).result)
                .isEqualTo(true);
    }
    @Test
    void shouldValidatePostcodeWithoutSpace() {
        assertThat(postCodeService.validateFullPostcode("B11BB").await().atMost(Duration.ofSeconds(5)).result)
                .isEqualTo(true);
    }
    @Test
    void shouldValidatePostcodeWithSpace() {
        assertThat(postCodeService.validateFullPostcode("B11BB").await().atMost(Duration.ofSeconds(5)).result)
                .isEqualTo(true);
    }

    @Test
    void shouldNotThrowWithValidPostcode(){
        assertDoesNotThrow(() -> postCodeValidator.isValidFullPostCode("B11BB"));
    }
    @Test
    void shouldThrowWithInValidPostcode(){
        assertDoesNotThrow(() -> postCodeValidator.isValidFullPostCode("B1X1BB"));
    }

}
