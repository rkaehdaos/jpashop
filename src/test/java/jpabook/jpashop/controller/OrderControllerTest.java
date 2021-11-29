package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.service.ItemService;
import jpabook.jpashop.service.MemberService;
import jpabook.jpashop.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DisplayName("주문  TEST")
@AutoConfigureMockMvc
@Transactional
@Slf4j
class OrderControllerTest {
    @Autowired private OrderService orderService;
    @Autowired private OrderRepository orderRepository;
    @Autowired private MemberService memberService;
    @Autowired private ItemService itemService;
    @Autowired private MockMvc mockMvc;
    @Autowired private ModelMapper modelMapper;
    @Test void isModelMapper() { assertNotNull(modelMapper); }

    @Test
    @DisplayName("주문 작성 폼 화면 정상")
    void createForm_Test() throws Exception {
        mockMvc.perform(get("/order"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("members"))
                .andExpect(model().attributeExists("items"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
//                .andExpect(content().string(containsString("안뇽")))
        ;
    }

    @Test
    @DisplayName("주문 작성 기능")
    void createOrder_Test() throws Exception {
        Member member = Member.builder().name("memberA").city("Seoul").street("테헤란로").zipcode("123123").build();
        Book book = Book.builder().name("book").price(1000).stockQuantity(100).author("Name").isbn("12345").build();
        Long memberId = memberService.join(member);
        Long bookId = itemService.save(book);


        mockMvc.perform(post("/order")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("memberId", String.valueOf(memberId))
                        .param("itemId", String.valueOf(bookId))
                        .param("count", String.valueOf(50))
                )
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(flash().attributeExists("orderId"))
        ;

        List<Order> orderList = orderRepository.findAll();
        assertNotNull(orderList,"비어있지 않아야 하고");
        assertEquals(1, orderList.size(),"1개만 들어있는데");
        Order findOrder = orderList.get(0);
//        log.info("**findOrder: "+findOrder);


    }

}