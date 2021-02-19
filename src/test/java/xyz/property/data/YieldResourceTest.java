package xyz.property.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.http.HttpStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import xyz.property.data.model.Response;
import xyz.property.data.model.YieldStats;

import javax.inject.Inject;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;

@QuarkusTest
public class YieldResourceTest {

    @Inject
    ObjectMapper mapper;

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
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }


    @Test
    void shouldGetYieldWithValidPostcode() throws JsonProcessingException {
        YieldStats yieldStats = given()
                .queryParam("postcode", "B11BB")
                .when()
                .get("/yield")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(YieldStats.class);
        String expectedStats = "{\"status\":\"success\",\"code\":null,\"message\":null,\"process_time\":\"5.93\",\"postcode\":\"B1 1BB\",\"postcode_type\":\"full\",\"url\":\"https://propertydata.co.uk/draw?input=B1+1BB\",\"bedrooms\":2,\"data\":{\"long_let\":{\"points_analysed\":40,\"radius\":\"0.19\",\"gross_yield\":\"3.7%\"}}}";
        assertThat(yieldStats).usingRecursiveComparison().isEqualTo(mapper.readValue(expectedStats,YieldStats.class));
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
