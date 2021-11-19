package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@Getter @Setter @ToString
public class memberForm {
    @NotEmpty(message = "회원 이름은 필수")
    private String name;
    private String city;
    private String street;
    private String zipcode;
}
