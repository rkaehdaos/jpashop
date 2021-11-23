package jpabook.jpashop.domain;

import lombok.*;

import javax.persistence.Embeddable;

@Embeddable

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder @AllArgsConstructor
@Getter @ToString
public class Address {
    private String city;
    String street;
    String zipcode;
}
