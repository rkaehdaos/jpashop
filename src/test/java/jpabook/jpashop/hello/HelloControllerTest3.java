package jpabook.jpashop.hello;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

//기본값임
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// 실제 내장톰캣 올림
// mockMvc가 아니라 RestTemplate를 사용해야 내장 톰캣과 인터렉션
@DisplayName("WebEnvironment.RANDOM_PORT + RestTemplate ")
@AutoConfigureMockMvc
@Transactional
@Slf4j
public class HelloControllerTest3 {
    @Autowired
    TestRestTemplate testRestTemplate;

    @LocalServerPort
    private int randomPort;

    @Test
    void postTest() throws URISyntaxException {
        //given
        URI uri = URI.create("http://localhost:" + randomPort + "/hello");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType(MediaType.TEXT_PLAIN, StandardCharsets.UTF_8));
        HttpEntity<String> req = new HttpEntity<>("hello", headers);

        //when
        final ResponseEntity<String> result = testRestTemplate.postForEntity(uri, req, String.class);

        //then
        assertEquals(result.getStatusCode(), HttpStatus.CREATED, "결과값 비교");
    }

}
