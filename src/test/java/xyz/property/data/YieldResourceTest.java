package xyz.property.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import xyz.property.data.model.YieldStats;

import javax.inject.Inject;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
public class YieldResourceTest {

    @Inject
    ObjectMapper mapper;

    @Test
    void shouldGetNotFoundWithInvalidPostcode() {
        given()
                .queryParam("postcode", "WW")
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
                .statusCode(HttpStatus.SC_BAD_REQUEST);
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
    void shouldFallBackToOutCodeStatsWhenInsufficientData() throws JsonProcessingException {
        YieldStats yieldStats = given()
                .queryParam("postcode", "W149AA")
                .when()
                .get("/yield")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .body().as(YieldStats.class);

        String expectedStats = "{\"status\":null,\"code\":null,\"message\":null,\"process_time\":null,\"effective_date\":1621869046191,\"postcode\":\"W1\",\"postcode_type\":\"outcode\",\"url\":null,\"bedrooms\":null,\"type\":null,\"data\":{\"long_let\":{\"points_analysed\":null,\"radius\":null,\"gross_yield\":\"4.6\"}}}";
        assertThat(yieldStats).usingRecursiveComparison().ignoringFields("effective_date").isEqualTo(mapper.readValue(expectedStats, YieldStats.class));

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
        String expectedStats = "{\"status\":\"success\",\"code\":null,\"type\":\"flat\",\"message\":null,\"process_time\":\"2.64\",\"postcode\":\"B1 1BB\",\"postcode_type\":\"full\",\"url\":\"https://propertydata.co.uk/draw?input=B1+1BB\",\"bedrooms\":2,\"data\":{\"long_let\":{\"points_analysed\":40,\"radius\":\"0.17\",\"gross_yield\":\"4.2%\"}}}";
        assertThat(yieldStats).usingRecursiveComparison().ignoringFields("effective_date").isEqualTo(mapper.readValue(expectedStats, YieldStats.class));
    }

    @Test
    void shouldGetOutcodeYieldWithValidOutcode() throws JsonProcessingException {
        YieldStats yieldStats = given()
                .queryParam("postcode", "W1")
                .when()
                .get("/yield")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(YieldStats.class);
        String expectedStats = "{\"status\":null,\"code\":null,\"message\":null,\"process_time\":null,\"effective_date\":1621869046191,\"postcode\":\"W1\",\"postcode_type\":\"outcode\",\"url\":null,\"bedrooms\":null,\"type\":null,\"data\":{\"long_let\":{\"points_analysed\":null,\"radius\":null,\"gross_yield\":\"4.6\"}}}";
        assertThat(yieldStats).usingRecursiveComparison().ignoringFields("effective_date").isEqualTo(mapper.readValue(expectedStats, YieldStats.class));
    }

    @Test
    void shouldFailWithInvalidOutcode() throws JsonProcessingException {
        given()
                .queryParam("postcode", "WW")
                .when()
                .get("/yield")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    void shouldFallbackToOutcodeStatsWithDistrictPostCode() throws JsonProcessingException {
        YieldStats yieldStats = given()
                .queryParam("postcode", "W1")
                .when()
                .get("/yield")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(YieldStats.class);

        String expectedStats = "{\"status\":null,\"code\":null,\"message\":null,\"process_time\":null,\"effective_date\":1621869046191,\"postcode\":\"W1\",\"postcode_type\":\"outcode\",\"url\":null,\"bedrooms\":null,\"type\":null,\"data\":{\"long_let\":{\"points_analysed\":null,\"radius\":null,\"gross_yield\":\"4.6\"}}}";
        assertThat(yieldStats).usingRecursiveComparison().ignoringFields("effective_date").isEqualTo(mapper.readValue(expectedStats, YieldStats.class));
    }

}
