package jpabook.jpashop.domain.item;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter @ToString(callSuper = true)
@DiscriminatorValue("Album")
public class Album extends Item {
    private String artist;
    private String etc;
}
