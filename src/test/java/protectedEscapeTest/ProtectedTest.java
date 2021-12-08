package protectedEscapeTest;

import lombok.*;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProtectedTest {

    public static final Logger log = LoggerFactory.getLogger(ProtectedTest.class);

    @Test
    void protectedTest1() throws Exception {
        Parent parent = new Parent("127.0.0.1", "testAccess1234");
        log.info(parent.toString());
        Child child = new Child(parent);
        log.info(child.toString());
        child.changeIP();
        log.info(child.toString());
    }
}


@Getter @ToString
class Parent {

    protected String serverIp;
    private String accessToken;

    public Parent(String serverIp, String accessToken) {
        this.serverIp = serverIp;
        this.accessToken = accessToken;
    }
}

@Getter @Setter @ToString(callSuper = true)
class Child extends Parent {

    private String childStr;

    public Child(Parent parent) {
        super(parent.getServerIp(), parent.getAccessToken());
        this.childStr = "I am B!";
    }

    public void changeIP() {
        this.serverIp = "123.456.789.012";

    }
}