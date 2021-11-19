package jpabook.jpashop;

import jpabook.jpashop.controller.memberForm;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.Provider;
import org.modelmapper.internal.bytebuddy.build.Plugin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static org.modelmapper.convention.MatchingStrategies.STRICT;

@SpringBootApplication
public class JpashopAdvanceApplication {

    public static void main(String[] args) {
        SpringApplication.run(JpashopAdvanceApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(STRICT);
        modelMapper.createTypeMap(memberForm.class, Member.class).setProvider(
                new Provider<Member>() {
                    @Override
                    public Member get(ProvisionRequest<Member> request) {
                        memberForm form = memberForm.class.cast(request.getSource());
                        Address address =
                                new Address(form.getCity(),
                                        form.getStreet(),
                                        form.getZipcode());
                        Member member = new Member();
                        member.setUsername(form.getName());
                        member.setAddress(address);
                        return member;
                    }
                }
        );
        return modelMapper;
    }
}
