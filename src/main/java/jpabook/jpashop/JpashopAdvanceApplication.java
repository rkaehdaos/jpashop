package jpabook.jpashop;

import jpabook.jpashop.controller.MemberForm;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import org.modelmapper.ModelMapper;
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
        modelMapper.createTypeMap(MemberForm.class, Member.class).setProvider(
                request -> {
                    MemberForm form = MemberForm.class.cast(request.getSource());
                    Address address =
                            new Address(form.getCity(),
                                    form.getStreet(),
                                    form.getZipcode());
                    Member member = new Member();
                    member.setName(form.getName());
                    member.setAddress(address);
                    return member;
                }
        );

        modelMapper.createTypeMap(Member.class, MemberForm.class).setProvider(
                request -> {
                    Member member = Member.class.cast(request.getSource());
                    MemberForm memberForm = new MemberForm();
                    memberForm.setName(member.getName());
                    memberForm.setCity(member.getAddress().getCity());
                    memberForm.setStreet(member.getAddress().getStreet());
                    memberForm.setZipcode(member.getAddress().getZipcode());
                    return memberForm;
                }
        );

        return modelMapper;
    }
}
