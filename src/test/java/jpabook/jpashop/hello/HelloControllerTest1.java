package jpabook.jpashop.hello;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.lenient;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = HelloController.class)
@DisplayName("MockMvc+MockBean 모킹")
class HelloControllerTest1 {
    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    HelloService helloService;

    @Test
    void hello_get_test() throws Exception {
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
        lenient().when(helloService.getHelloMsg(1L)).thenReturn("hello");

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
        lenient().when(helloService.setHelloMsg(msg)).thenReturn(1L);

        //then
        mockMvc.perform(post("/hello/"+msg))
                .andDo(print())
                .andExpect(status().isOk());

    }
}