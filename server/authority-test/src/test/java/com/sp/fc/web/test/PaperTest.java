package com.sp.fc.web.test;


import com.sp.fc.web.service.Paper;
import com.sp.fc.web.service.PaperService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;




public class PaperTest extends WebIntegrationTest{

    @Autowired
    private PaperService paperService;

    TestRestTemplate client = new TestRestTemplate();

    List<String> students = new ArrayList<>(Arrays.asList("user1"));

    private Paper paper1 = Paper.builder()
            .paperId(1L)
            .title("Test1")
            .tutorId("tutor1")
            .studentIds(students)
            .state(Paper.State.PREPARE)
            .build();
    private Paper paper2 = Paper.builder()
            .paperId(2L)
            .title("Test2")
            .tutorId("tutor1")
            .studentIds(Arrays.asList("user2"))
            .state(Paper.State.PREPARE)
            .build();

    @DisplayName("1. user1이 시험지 리스트 조회한다. ")
    @Test
    public void test_1() {
        paperService.setPaper(paper1);
        client = new TestRestTemplate("user1","1111");
        ResponseEntity response = client.exchange(uri("/papers/my-papers"), HttpMethod.GET, null, new ParameterizedTypeReference<Object>() {
        });
        assertEquals(200, response.getStatusCodeValue());
//        assertEquals(0,response.getBody().size());
        System.out.println(response.getBody());

    }

    @DisplayName("2. user1이 user2의 시험지는 볼 수 없다.")
    @Test
    public void test_2() {
        paperService.setPaper(paper2);
        client = new TestRestTemplate("user1","1111");
        ResponseEntity response = client.exchange(uri("/papers/2"), HttpMethod.GET, null, new ParameterizedTypeReference<Object>() {
        });
        assertEquals(403, response.getStatusCodeValue());
    }
}
