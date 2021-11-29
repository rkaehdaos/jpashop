package jpabook.jpashop.domain;

import lombok.*;

import javax.persistence.*;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
@Table(name = "orders")
@Getter @ToString(exclude = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {
    @Id
    @GeneratedValue
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

    // 오더 아이템 추가
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    // 배송 추가
    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    @Builder
    public Order(Member member, Delivery delivery, LocalDateTime localDateTime, OrderStatus orderStatus, OrderItem... orderItems) {
        this.member = member;
        this.delivery = delivery;
        this.localDateTime = localDateTime;
        this.orderStatus = orderStatus;
        Arrays.stream(orderItems).forEach(this::addOrderItem);
    }

//비지니스 로직

    // 생성 메서드
    // 주문
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        return Order.builder()
                .member(member)
                .delivery(delivery)
                .orderItems(orderItems)
                .orderStatus(OrderStatus.ORDER)
                .localDateTime(LocalDateTime.now()).build();
    }

    // 취소 메서드
    public void cancel() {
        if (delivery.getStatus() == DeliveryStatus.COMPLETE)
            throw new IllegalStateException("이미 완료된 주문은 취소가 불가");
        this.orderStatus = OrderStatus.CANCEL;
        orderItems.forEach(OrderItem::cancel);
    }

    /**
     * 주문 상품 전체 가격 조회(비지니스 로직 아님)
     *
     * @return 전체 주문 가격
     */
    public int getTotalPrice() {
        return orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum();
    }

}
