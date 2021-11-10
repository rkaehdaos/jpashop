package jpabook.jpashop.hello;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.lenient;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

//기본값임
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
// 서블릿 컨테이너를 실제로 띄우지 않고 모킹함
// 모킹한 서블릿 컨테이너와 인터렉셔하려면 mockmvc 필요
@DisplayName("WebEnvironment.MOCK + Mockmvc")
@AutoConfigureMockMvc
public class HelloControllerTest2 {
    @Autowired
    MockMvc mockMvc;

    @Test
    void hello_test() throws Exception {
        mockMvc.perform(get("/hello"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("data", "hello!!!"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().string(containsString("안뇽")));
    }

    @Test
    void hello2_test() throws Exception {
        //given
        Hello hello = Hello.builder().helloMsg("hello").helloMsg2("dummy").build();

        //when


        //then


    }

}
