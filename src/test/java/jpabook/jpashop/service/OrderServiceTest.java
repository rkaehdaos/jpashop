package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired private EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    @DisplayName("상품주문")
    void item_order() throws Exception {

        //given
        Member member = createMember();

        Book book = createBook("jpa", 10000, 10);

        //when
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);


        //then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.ORDER, getOrder.getOrderStatus(),"상품 주문시 상태는 ORDER");
        assertEquals(1, getOrder.getOrderItems().size(), "주문한 상품 카운트 수가 정확해야");
        assertEquals(10000*orderCount, getOrder.getTotalPrice(), "주문가격은 정확히 계산이 되는가? ㅋ");
        assertEquals(8, book.getStockQuantity(), "주문 수량만큼 재고 수가 줄어야 함");

    }

    @Test
    @DisplayName("주문 취소")
    void order_cancel() throws Exception {
        //given

        //when

        //then
    }

    @Test
    @DisplayName("상품주문 재고수량 초과")
    void order_ordercount_over_stockQuantity() throws Exception {
        //given
        Member member = createMember();
        Item item = createBook("jpa", 10000, 10);

        //when
        int orderCount = 11;

        //then
        assertThrows(NotEnoughStockException.class, () -> {
            Long orderId = orderService.order(member.getId(), item.getId(), orderCount);
        });




    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setUsername("memberA");
        member.setAddress(new Address("seoul", "테헤란로", "123-123"));
        em.persist(member);
        return member;
    }
}