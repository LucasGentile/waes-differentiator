package com.waes.assignment.waesdifferentiator.api.v1.diff;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.waes.assignment.waesdifferentiator.WaesDifferentiatorApplication;
import com.waes.assignment.waesdifferentiator.api.v1.diff.dto.DiffResultDTO;
import com.waes.assignment.waesdifferentiator.api.v1.diff.dto.DiffSideDTO;
import com.waes.assignment.waesdifferentiator.api.v1.diff.enums.DiffResultType;
import com.waes.assignment.waesdifferentiator.api.v1.diff.model.DiffPair;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static com.waes.assignment.waesdifferentiator.api.v1.diff.utils.TestUtils.*;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = WaesDifferentiatorApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
public class DiffControllerIntegrationTest {
    private static RequestSpecification spec;

    @LocalServerPort
    private int port;

    @Before
    public void setup() throws Exception {
        spec = new RequestSpecBuilder().setContentType(ContentType.JSON).setAccept(ContentType.JSON).setPort(port).setBaseUri("http://localhost").build();
    }

    @Test
    public void getDiffPair_existingElement_success() throws IOException {
        // given an existing DiffPair
        Long diffPairId = getRandomId();
        DiffSideDTO leftContent = createDiffSideDTO(encodeBase64String("testLeft"));
        apiPostLeft(diffPairId, leftContent);

        // when it is searched with the diffPair endpoint
        Response response = apiGetById(diffPairId);

        // then the return status is OK and the element should be returned
        response.then().assertThat().statusCode(HttpStatus.SC_OK);

        String stringJson = response.getBody().asString();
        DiffPair diffPair = stringJsonToDiffPair(stringJson);

        assertEquals(diffPairId, diffPair.getDiffId());
        assertEquals(leftContent.getDiffSideContent(), diffPair.getLeftSide());
    }

    @Test
    public void saveDiffContent_LeftSide_success() throws IOException {
        // given an DiffSideDTO with the content for the LEFT side
        Long diffPairId = getRandomId();
        DiffSideDTO dto = createDiffSideDTO(encodeBase64String("testLeft"));

        // when executing the left endpoint to save the DiffPair containing the LEFT side content, then the status should be CREATED.
        apiPostLeft(diffPairId, dto).then().assertThat().statusCode(HttpStatus.SC_CREATED);

        // and the saved DiffPair should contain the left content of the respective id
        DiffPair savedDto = stringJsonToDiffPair(apiGetById(diffPairId).getBody().asString());
        assertEquals(dto.getDiffSideContent(), savedDto.getLeftSide());
        assertEquals(diffPairId, savedDto.getDiffId());
    }

    @Test
    public void saveDiffContent_RightSide_success() throws IOException {
        // given an DiffSideDTO with the content for the RIGHT side
        Long diffPairId = getRandomId();
        DiffSideDTO dto = createDiffSideDTO(encodeBase64String("testRight"));

        // when executing the right endpoint to save the DiffPair containing the RIGHT side content, then the status should be CREATED.
        apiPostRight(diffPairId, dto).then().assertThat().statusCode(HttpStatus.SC_CREATED);

        // and the saved DiffPair should contain the right side from the respective id
        DiffPair savedDto = stringJsonToDiffPair(apiGetById(diffPairId).getBody().asString());
        assertEquals(dto.getDiffSideContent(), savedDto.getRightSide());
        assertEquals(diffPairId, savedDto.getDiffId());
    }

    @Test
    public void executeRunDifferentiatior_RightAndLeftSideNotNull_success() throws IOException {
        // given an id, from a DiffPair with LEFT and RIGHT sides
        Long diffPairId = getRandomId();
        DiffSideDTO dtoLeft = createDiffSideDTO(encodeBase64String("testLeft"));
        apiPostLeft(diffPairId, dtoLeft).then().assertThat().statusCode(HttpStatus.SC_CREATED);
        DiffSideDTO dtoRight = createDiffSideDTO(encodeBase64String("testRight"));
        apiPostRight(diffPairId, dtoRight).then().assertThat().statusCode(HttpStatus.SC_CREATED);

        // when executing the diff endpoint, then the status should be OK.
        Response response = apiPostDiff(diffPairId);
        assertEquals(HttpStatus.SC_OK, response.getStatusCode());


        // and the result DiffResultDTO should contain the DiffPair diff-ed, the ResulType and OffsetResults
        DiffResultDTO resultDto = stringJsonToDiffResultDTO(response.getBody().asString());
        assertEquals(dtoLeft.getDiffSideContent(), resultDto.getDiffPair().getLeftSide());
        assertEquals(dtoRight.getDiffSideContent(), resultDto.getDiffPair().getRightSide());
        assertEquals(DiffResultType.DIFF, resultDto.getDiffResultType());
        assertNotNull(resultDto.getDiffOffsets());
    }

    private Response apiPostLeft(Long diffId, DiffSideDTO dto) {
        return given().spec(spec).body(dto, ObjectMapperType.JACKSON_2).when().post("/api/v1/diff/" + diffId + "/left");
    }

    private Response apiPostRight(Long diffId, DiffSideDTO dto) {
        return given().spec(spec).body(dto, ObjectMapperType.JACKSON_2).when().post("/api/v1/diff/" + diffId + "/right");
    }

    private Response apiPostDiff(Long diffId) {
        return given().spec(spec).post("/api/v1/diff/" + diffId);
    }

    private Response apiGetById(Long diffId) {
        return given().spec(spec).get("/api/v1/diff/diffPair/" + diffId);
    }

    private DiffPair stringJsonToDiffPair(String stringJson) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(stringJson, DiffPair.class);
    }

    private DiffResultDTO stringJsonToDiffResultDTO(String stringJson) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(stringJson, DiffResultDTO.class);
    }
}
