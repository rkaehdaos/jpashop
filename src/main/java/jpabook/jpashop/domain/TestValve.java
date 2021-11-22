package jpabook.jpashop.domain;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import lombok.val;

import java.util.List;

@Builder
public class TestValve {
    String name;
    int val1;
    int val2;
    val str;

    @Builder.Default
    int defValue1 = 999;

    @Singular("addVal")
    List<Integer> values;


}
