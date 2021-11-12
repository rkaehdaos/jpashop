package jpabook.jpashop.hello;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.lenient;

@SpringBootTest(classes = HelloService.class)
@Slf4j
public class HelloServiceTest2 {
    @Autowired
    HelloService helloService;
    @MockBean
    HelloRepository helloRepository;

    private Hello hello = Hello.builder()
            .helloMsg("hello")
            .helloMsg2(null)
            .build();


    @Test
    void mock_test1() {
        //given
        lenient().when(helloRepository.save(hello)).thenReturn(1L);
        lenient().when(helloRepository.findById(1L)).thenReturn(hello);

        //when
        Long savedId = helloService.setHelloMsg("hello");
        String helloMsg = helloService.getHelloMsg(savedId);

        //then
        assertEquals(helloMsg, "hello");
    }

}
