package nextstep.subway.section;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import nextstep.subway.common.BaseAcceptanceTest;
import nextstep.subway.common.ResponseAssertTest;
import nextstep.subway.line.LineAcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("지하철 노선 역 등록 관련 기능")
public class SectionAcceptanceTest extends BaseAcceptanceTest {

    int 초기_노선_길이;
    Long 노선_ID;
    Long 상행역_ID;
    Long 하행역_ID;
    LineResponse 신분당선;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // Given
        초기_노선_길이 = 12;
        신분당선 = LineAcceptanceTest.노선_생성_요청("신분당선", "bg-red-600", "강남역", "광교역", 초기_노선_길이).as(LineResponse.class);
        노선_ID = 신분당선.getId();
        상행역_ID = 신분당선.getStations().get(0).getId();
        하행역_ID = 신분당선.getStations().get(1).getId();
    }

    /**
     * Given 지하철 노선에 구간을 등록하고
     * When 노선에 새로운 구간을 등록하면
     * Then 새로운 역이 노선에 포함된다.
     */
    @Test
    void 노선에_새로운_역_등록() {
        // When
        ExtractableResponse<Response> 신규역 = StationAcceptanceTest.지하철역_생성_요청("신규역");
        Long 신규역_ID = 응답_ID(신규역);
        ExtractableResponse<Response> 노선에_지하철역_등록_응답 = 지하철역_생성_요청(노선_ID, 상행역_ID, 신규역_ID, 4);

        // Then
        노선에_지하철역_등록_확인(노선에_지하철역_등록_응답, "강남역", "신규역", "광교역");
    }

    /**
     * Given 지하철 노선에 구간을 등록하고
     * When 새로운 역을 상행 종점으로 등록하면
     * Then 새로운 역이 상행 종점으로 등록된다.
     */
    @Test
    void 새로운_역을_상행_종점으로_등록() {
        // When
        ExtractableResponse<Response> 신규역 = StationAcceptanceTest.지하철역_생성_요청("신사역");
        Long 신규역_ID = 응답_ID(신규역);
        ExtractableResponse<Response> 노선에_지하철역_등록_응답 = 지하철역_생성_요청(노선_ID, 신규역_ID, 상행역_ID, 3);

        // Then
        노선에_지하철역_등록_확인(노선에_지하철역_등록_응답, "신사역", "강남역", "광교역");
    }

    /**
     * Given 지하철 노선에 구간을 등록하고
     * When 새로운 역을 하행 종점으로 등록하면
     * Then 새로운 역이 하행 종점으로 등록된다.
     */
    @Test
    void 새로운_역을_하행_종점으로_등록() {
        // When
        ExtractableResponse<Response> 신규역 = StationAcceptanceTest.지하철역_생성_요청("동천역");
        Long 신규역_ID = 응답_ID(신규역);
        ExtractableResponse<Response> 노선에_지하철역_등록_응답 = 지하철역_생성_요청(노선_ID, 신규역_ID, 상행역_ID, 3);

        // Then
        노선에_지하철역_등록_확인(노선에_지하철역_등록_응답, "강남역", "광교역", "동천역");
    }

    /**
     * Given 지하철 노선에 구간을 등록하고
     * When 역 사이에 새로운 역을 등록하면
     * Then 400 Bad Request를 응답한다.
     */
    @ParameterizedTest
    @ValueSource(ints = {12, 13})
    void 기존_역_사이_길이보다_크거나_같은_길이_등록되있으면_예외(int distance) {
        // When
        ExtractableResponse<Response> 신규역 = StationAcceptanceTest.지하철역_생성_요청("양재역");
        Long 신규역_ID = 응답_ID(신규역);
        ExtractableResponse<Response> 노선에_지하철역_등록_응답 = 지하철역_생성_요청(노선_ID, 하행역_ID, 신규역_ID, distance);

        // Then
        printErrorMessage(노선에_지하철역_등록_응답);
        assertThat(노선에_지하철역_등록_응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 노선에 구간을 등록하고
     * When 상행역과 하행역을 똑같이 등록하면
     * Then 400 Bad Request를 응답한다.
     */
    @Test
    void 상행역과_하행역이_이미_노선에_등록되있으면_예외() {
        // When
        StationAcceptanceTest.지하철역_생성_요청("양재역");
        ExtractableResponse<Response> 지하철_노선에_지하철역_등록_응답 = 지하철역_생성_요청(노선_ID, 상행역_ID, 하행역_ID, 4);

        // Then
        printErrorMessage(지하철_노선에_지하철역_등록_응답);
        assertThat(지하철_노선에_지하철역_등록_응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 노선에 구간을 등록하고
     * When 상행역과 하행역에 둘 중 하나도 포함되어있지 않은 노선을 등록하면
     * Then 400 Bad Request를 응답한다.
     */
    @Test
    void 상행역과_하행역이_노선에_모두_존재하지않는_경우_예외() {
        // When
        ExtractableResponse<Response> 신규역1 = StationAcceptanceTest.지하철역_생성_요청("없는역1");
        Long 신규역1_ID = 응답_ID(신규역1);
        ExtractableResponse<Response> 신규역2 = StationAcceptanceTest.지하철역_생성_요청("없는역2");
        Long 신규역2_ID = 응답_ID(신규역2);
        ExtractableResponse<Response> 지하철_노선에_지하철역_등록_응답 = 지하철역_생성_요청(노선_ID, 신규역1_ID, 신규역2_ID, 4);

        // Then
        지하철역_등록_실패_검증(지하철_노선에_지하철역_등록_응답);
    }

    /**
     * Given 지하철 노선에 구간을 등록하고
     * When 상행역구간을 삭제하면
     * Then 해당 구간 정보는 삭제되고 조회되지 않는다.
     */
    @Test
    void 상행역을_삭제한다() {
        // Given
        ExtractableResponse<Response> 신규역 = StationAcceptanceTest.지하철역_생성_요청("신규역");
        Long 신규역_ID = 응답_ID(신규역);
        ExtractableResponse<Response> 지하철_노선에_지하철역_등록_응답 = 지하철역_생성_요청(노선_ID, 신규역_ID ,상행역_ID, 4);

        // When
        ExtractableResponse<Response> 지하철역_구간_삭제_응답 = 지하철역_구간_삭제_요청(노선_ID, 신규역_ID);

        // Then
        지하철역_구간_삭제_응답_검증(지하철역_구간_삭제_응답);
        ExtractableResponse<Response> 지하철노선_조회_응답 = LineAcceptanceTest.노선_조회_요청(노선_ID);
        노선에_지하철역_등록_확인(지하철노선_조회_응답, "강남역", "광교역");
    }

    /**
     * Given 지하철 노선에 구간을 등록하고
     * When 하행역 구간을 삭제하면
     * Then 해당 구간 정보는 삭제되고 조회되지 않는다.
     */
    @Test
    void 하행역을_삭제한다() {
        // Given
        ExtractableResponse<Response> 신규역 = StationAcceptanceTest.지하철역_생성_요청("신규역");
        Long 신규역_ID = 응답_ID(신규역);
        ExtractableResponse<Response> 지하철_노선에_지하철역_등록_응답 = 지하철역_생성_요청(노선_ID, 신규역_ID ,상행역_ID, 4);

        // When
        ExtractableResponse<Response> 지하철역_구간_삭제_응답 = 지하철역_구간_삭제_요청(노선_ID, 하행역_ID);

        // Then
        지하철역_구간_삭제_응답_검증(지하철역_구간_삭제_응답);
        ExtractableResponse<Response> 지하철노선_조회_응답 = LineAcceptanceTest.노선_조회_요청(노선_ID);
        노선에_지하철역_등록_확인(지하철노선_조회_응답, "신규역", "강남역");
    }

    /**
     * Given 지하철 노선에 구간을 등록하고
     * When 중간역 구간을 삭제하면
     * Then 해당 구간 정보는 삭제되고 조회되지 않는다.
     */
    @Test
    void 중간역을_삭제한다() {
        // given
        ExtractableResponse<Response> 신규역 = StationAcceptanceTest.지하철역_생성_요청("신규역");
        Long 신규역_ID = 응답_ID(신규역);
        ExtractableResponse<Response> 지하철_노선에_지하철역_등록_응답 = 지하철역_생성_요청(노선_ID, 신규역_ID ,상행역_ID, 4);

        // when
        ExtractableResponse<Response> 지하철역_구간_삭제_응답 = 지하철역_구간_삭제_요청(노선_ID, 상행역_ID);

        // Then
        지하철역_구간_삭제_응답_검증(지하철역_구간_삭제_응답);
        ExtractableResponse<Response> 지하철노선_조회_응답 = LineAcceptanceTest.노선_조회_요청(노선_ID);
        노선에_지하철역_등록_확인(지하철노선_조회_응답, "신규역", "광교역");
    }

    /**
     * Given 지하철 노선에 구간을 등록하고
     * When 존재하지 않는 역을 삭제하면
     * Then 400 Bad Request를 응답한다.
     */
    @Test
    void 노선에_등록되지_않은_역을_제거_요청_예외() {
        // When
        Long 존재하지_않는_역_ID = 30L;
        ExtractableResponse<Response> 지하철역_구간_삭제_응답 = 지하철역_구간_삭제_요청(노선_ID, 존재하지_않는_역_ID);

        // Then
        assertThat(지하철역_구간_삭제_응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
    /**
     * Given 지하철 노선에 구간을 하나만 등록하고
     * When 해당 역을 삭제하면
     * Then 400 Bad Request를 응답한다.
     */
    @Test
    void 구간이_하나인_노선에서_역을_제거_요청_예외() {
        // When
        지하철역_구간_삭제_요청(노선_ID, 상행역_ID);
        ExtractableResponse<Response> 지하철역_구간_삭제_응답 = 지하철역_구간_삭제_요청(노선_ID, 하행역_ID);

        // Then
        assertThat(지하철역_구간_삭제_응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void 지하철역_구간_삭제_응답_검증(ExtractableResponse<Response> 지하철역_구간_삭제_응답) {
        ResponseAssertTest.응답_컨텐츠가_없는_성공_확인(지하철역_구간_삭제_응답);
    }

    private ExtractableResponse<Response> 지하철역_구간_삭제_요청(Long 노선_ID, Long 삭제할_역_ID) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/{lineId}/sections?stationId={stationId}", 노선_ID, 삭제할_역_ID)
                .then().log().all()
                .extract();
    }

    private void 노선에_지하철역_등록_확인(ExtractableResponse<Response> response, String... expectStationNames) {
        List<String> stationsNameList = response.jsonPath().getList("stations.name", String.class);
        assertThat(stationsNameList).containsAll(Arrays.asList(expectStationNames));
    }

    private ExtractableResponse<Response> 지하철역_생성_요청(Long lineId, Long upStationId, Long downStationId, int distance) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("lineId", lineId);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/{lineId}/stations", lineId)
                .then().log().all()
                .extract();
    }

    private void 지하철역_등록_실패_검증(ExtractableResponse<Response> 지하철_노선에_지하철역_등록_응답) {
        printErrorMessage(지하철_노선에_지하철역_등록_응답);
        assertThat(지하철_노선에_지하철역_등록_응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private static void printErrorMessage(ExtractableResponse<Response> 지하철_노선에_지하철역_등록_응답) {
        System.out.println(지하철_노선에_지하철역_등록_응답.jsonPath().getString("errorMessage"));
    }
}
