package jpabook.jpashop.hello;


import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class HelloVO {
    Long id;
    String helloMsg;
    String helloMsg2 = UUID.randomUUID().toString();
}
