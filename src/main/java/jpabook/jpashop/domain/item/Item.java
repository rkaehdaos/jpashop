package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE )
@DiscriminatorColumn //default name : "DTYPE"
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Item {
    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

//    @Builder
    protected Item(String name, int price, int stockQuantity) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    // 업데이트용
    protected void changeItemValue(Item item) {
        this.name = item.getName();
        this.price = item.getPrice();
        this.stockQuantity = item.getStockQuantity();
    }

    //재고를 늘리고 줄이고 확정하는 비지니스 로직 넣기

    /**
     * Stock 증가
     *
     * @param quantity 재고 증가 요청 값
     */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    /**
     * Stock 감소
     *
     * @param quantity 재고 증가 감소 값(0보다 작을 수 없음)
     */
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("neeed more stock");
        }
        this.stockQuantity -= quantity;

    }


}