package jpabook.jpashop.api;

import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DisplayName("Simple Order Test")
@AutoConfigureMockMvc
@Transactional
class OrderSimnpleApiControllerTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private OrderRepository orderRepository;

    @Test
    void ordersV1Test() throws Exception {
        //given
        final int orderCount = orderRepository.findAll().size();

        //when
        mockMvc.perform(get("/api/v1/simple-orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(UTF_8))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.*", hasSize(orderCount))) // 주문 수
        ;
    }

    @Test
    void ordersV2Test() throws Exception {
        //given
        final int orderCount = orderRepository.findAll().size();

        //when
        mockMvc.perform(get("/api/v2/simple-orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(UTF_8))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.*", hasSize(orderCount))) // 주문 수
        ;
    }

    @Test
    void ordersV3Test() throws Exception {
        //given
        final int orderCount = orderRepository.findAll().size();

        //when
        mockMvc.perform(get("/api/v3/simple-orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(UTF_8))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.*", hasSize(orderCount))) // 주문 수
        ;
    }

    @Test
    void ordersV4Test() throws Exception {
        //given
        final int orderCount = orderRepository.findAll().size();

        //when
        mockMvc.perform(get("/api/v4/simple-orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(UTF_8))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.*", hasSize(orderCount))) // 주문 수
        ;
    }
}