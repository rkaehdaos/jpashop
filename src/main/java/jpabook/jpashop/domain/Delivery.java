package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter @Setter @ToString
public class Delivery {
    @Id @GeneratedValue
    @JoinColumn(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery")
    private Order order;
    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

}
