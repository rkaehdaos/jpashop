package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {
    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime localDateTime; //주문시간

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; //주문 상태

    // 연관관계(편의)메서드
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //비지니스 로직

    // 생성 메서드
    // 주문
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems){
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        Arrays.stream(orderItems).forEach(order::addOrderItem);
        order.setOrderStatus(OrderStatus.ORDER);
        order.setLocalDateTime(LocalDateTime.now());
        return null;
    }

    // 취소 메서드
    public void cancel() {
        if(delivery.getStatus()==DeliveryStatus.COMPLETE){
            throw new IllegalStateException("이미 완료된 주문은 취소가 불가");
        }
        setOrderStatus(OrderStatus.CANCEL);
        orderItems.forEach(OrderItem::cancel);

    }

    // 비지니스가 아님
    // 조회 로직

    /**
     * 주문 상품 전체 가격 조회
     * @return 전체 주문 가격
     */
    public int getTotalPrice() {
        return orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum();
    }

}
