package jpabook.jpashop.repository;

import jpabook.jpashop.repository.order.query.OrderQueryDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {

    private final EntityManager em;

    public List<OrderQueryDto> findOrderQueryDtos() {
        return em.createQuery(
                        "select new jpabook.jpashop.repository.order.query.OrderQueryDto(" +
                                "o.id, m.name, o.orderDate, o.orderStatus, d.address)" +
                                " from Order o" +
                                " join o.member m" +
                                " join o.delivery d", OrderQueryDto.class)
                .getResultList();
    }
}
