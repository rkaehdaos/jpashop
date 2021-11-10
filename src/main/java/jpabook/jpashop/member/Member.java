package jpabook.jpashop.member;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter @Setter @ToString
@EqualsAndHashCode(of = "id")
public class Member {
    @Id @GeneratedValue
    private Long id;
    private String username;
}
