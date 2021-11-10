package jpabook.jpashop.hello;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
@DisplayName("MockitoExtension 사용 테스트")
class HelloServiceTest1 {
    public static final Logger log = LoggerFactory.getLogger(HelloServiceTest1.class);
    @InjectMocks
    HelloService helloService;
    @Mock
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
        Long savedId = helloService.setHelloMsg(hello);
        String helloMsg = helloService.getHelloMsg(savedId);
        
        //then
        assertEquals(helloMsg, "hello");
    }

}