package jpabook.jpashop.domain.item;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter @Setter @ToString(callSuper = true)
@DiscriminatorValue("Movie")
public class Movie extends Item{
    private String director;
    private String actor;
}