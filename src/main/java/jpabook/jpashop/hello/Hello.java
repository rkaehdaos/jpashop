package jpabook.jpashop.hello;

import lombok.*;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Getter() @ToString(of = {"id","helloMsg","helloMsg2"})
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class Hello {
    @Id @GeneratedValue
    private Long id;
    private String helloMsg;
    private String helloMsg2 = UUID.randomUUID().toString();

    @Builder
    public Hello( String helloMsg, String helloMsg2) {
        this.helloMsg = helloMsg;
        this.helloMsg2 = helloMsg2;
    }
}
