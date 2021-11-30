package jpabook.jpashop.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter @ToString(exclude = "orders")
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member")
    private List<Order> orders;

    @Builder
    public Member(String name, String city, String street, String zipcode, List<Order> orders) {
        this.name = name;
        this.orders = orders;
        this.address = Address.builder().city(city).street(street).zipcode(zipcode).build();
        this.orders = Objects.requireNonNullElseGet(orders, ArrayList::new);
    }
}

