package jpabook.jpashop.hello;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class HelloService {
    private final HelloRepository helloRepository;

    public String getHelloMsg(final Long id) {
        return helloRepository.findById(id).getHelloMsg();
    }

    public Long setHelloMsg(String msg) {

        return helloRepository.save(Hello.builder().helloMsg(msg).helloMsg2(null).build());
    }
}
