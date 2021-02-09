package xyz.property.data;

import io.quarkus.test.junit.QuarkusTest;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import xyz.property.data.model.Response;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;

@QuarkusTest
public class YieldResourceTest {

    @Test
    void shouldGetInvalidRequestWithPostCodeW1() {
        given()
                .queryParam("postcode", "W1")
                .when()
                .get("/yield")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    void shouldGetInvalidRequestWith6Bedrooms() {
        given()
                .queryParam("postcode", "B11BB")
                .queryParam("bedrooms", 6)
                .when()
                .get("/yield")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_NOT_ACCEPTABLE);
    }

    @Test
    void shouldGetInvalidRequestWithUnknownHouseType() {
        given()
                .queryParam("postcode", "B11BB")
                .queryParam("type", "Villa")
                .when()
                .get("/yield")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_NOT_ACCEPTABLE);
    }


    @Test
    void shouldGetYieldWithValidPostcode() {
        given()
                .queryParam("postcode", "B11BB")
                .when()
                .get("/yield")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("postcode", equalToIgnoringCase("B1 1BB"))
                .body("avgYield",equalTo("3.7%"));
    }

    @Test
    void shouldFallbackToRegionPostcodeWhenNoData() {
        Response response = given()
                .queryParam("postcode", "W149AA")
                .when()
                .get("/yield")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(Response.class);
        assertThat(response.getPostcode()).isEqualTo("W1");
    }

}
