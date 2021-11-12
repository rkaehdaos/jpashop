package jpabook.jpashop.hello;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//기본값임
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
// 서블릿 컨테이너를 실제로 띄우지 않고 모킹함
// 모킹한 서블릿 컨테이너와 인터렉셔하려면 mockmvc 필요
@DisplayName("WebEnvironment.MOCK + Mockmvc")
@AutoConfigureMockMvc
@Transactional
@Slf4j
public class HelloControllerTest2 {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private HelloService helloService;

//    @Autowired
//    private ObjectMapper objectMapper;

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
    @Disabled
    void hello2_test() throws Exception {
        //given
        Hello hello = Hello.builder().helloMsg("hello").helloMsg2("dummy").build();

        //when
        helloService.setHelloMsg("hello"); //미리 저장

        //then
        mockMvc.perform(get("/hello/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string("hello"))
        ;
    }

    @Test
    void hello_post_test() throws Exception {
        //given
        String msg = "hello";

        //when

        //then
/*

        mockMvc.perform(post("/hello/"+msg))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk());
*/

/*

        MvcResult mvcResult = mockMvc.perform(post("/hello/" + msg))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andReturn();
        final String StringedId = mvcResult.getResponse().getContentAsString();
*/


        final String StringedId = mockMvc.perform(post("/hello/" + msg))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();


        log.info(StringedId);

        //then2
        mockMvc.perform(get("/hello/" + StringedId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string("hello"))
        ;

    }

}
