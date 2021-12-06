package jpabook.jpashop;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDb {
    private final InitService initService;

    @PostConstruct
    public void init(){
        initService.dbInit1();
        initService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;

        public void dbInit1() {
            Member userA = Member.builder().name("userA").city("Seoul").street("테헤란로").zipcode("1-A").build();
            Book jpabook1 = Book.builder().name("JPA1").price(10000).stockQuantity(100).build();
            Book jpabook2 = Book.builder().name("JPA2").price(20000).stockQuantity(100).build();
            em.persist(userA);
            em.persist(jpabook1);
            em.persist(jpabook2);

            OrderItem order1Item1 = OrderItem.createOrderItem(jpabook1, 10000, 1);
            OrderItem order1Item2 = OrderItem.createOrderItem(jpabook2, 20000, 2);
            Order order1 = Order.createOrder(userA, createDelivery(userA), order1Item1, order1Item2);
            em.persist(order1);
        }

        public void dbInit2() {
            Member userB = Member.builder().name("userB").city("Asan").street("용연로").zipcode("1-B").build();
            Book springBook1 = Book.builder().name("SPRING1").price(20000).stockQuantity(200).build();
            Book springBook2 = Book.builder().name("SPRING2").price(40000).stockQuantity(300).build();
            em.persist(userB);
            em.persist(springBook1);
            em.persist(springBook2);
            OrderItem order2Item1 = OrderItem.createOrderItem(springBook1, 20000, 3);
            OrderItem order2Item2 = OrderItem.createOrderItem(springBook2, 40000, 4);
            Order order2 = Order.createOrder(userB, createDelivery(userB), order2Item1, order2Item2);
            em.persist(order2);
        }

        private Delivery createDelivery(Member member) {
            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            return delivery;
        }
    }


}


