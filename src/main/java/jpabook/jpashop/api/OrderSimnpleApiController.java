package jpabook.jpashop.api;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderSearch;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * xToOne(ManyToOne, OneToOne) 연관 관계 최적화
 * Order
 * Order -> Member : ManyToOne
 * Order -> Delivery : OneToOne
 */
@RestController
@RequiredArgsConstructor
public class OrderSimnpleApiController {
    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName();  //강제 초기화 :  order.getMember()까진 프록시. getName()을 하면 DB에서 가져온다
            order.getDelivery().getAddress();
        }
        return all;
    }

}
