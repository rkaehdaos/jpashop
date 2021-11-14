package jpabook.jpashop.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Address {
    private String city;
    private String zipcode;
}
