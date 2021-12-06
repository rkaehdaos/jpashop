package jpabook.jpashop;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import jpabook.jpashop.controller.BookForm;
import jpabook.jpashop.controller.MemberForm;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.item.Book;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
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
    Hibernate5Module hibernate5Module() {
        Hibernate5Module hibernate5Module = new Hibernate5Module();
        hibernate5Module.configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING, true);
        return hibernate5Module;
    }

    @Bean
    public ModelMapper modelMapper() {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(STRICT)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE) //private인 경우 setter없이 필드면 같을때 자동 매핑한다고 함
                .setFieldMatchingEnabled(true);
        // MemberForm → Member
        modelMapper.createTypeMap(MemberForm.class, Member.class).setProvider(
                request -> {
                    MemberForm form = MemberForm.class.cast(request.getSource());
                    return Member.builder()
                            .name(form.getName())
                            .city(form.getCity())
                            .street(form.getStreet())
                            .zipcode(form.getZipcode())
                            .build();
                }
        );
        // Member → MemberForm
/*

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
*/

        //북폼 →북
        modelMapper.createTypeMap(BookForm.class, Book.class).setProvider(request -> {
            BookForm form = BookForm.class.cast(request.getSource());
            return Book.builder()
                    .name(form.getName())
                    .price(form.getPrice())
                    .stockQuantity(form.getStockQuantity())
                    .author(form.getAuthor())
                    .isbn(form.getIsbn()).build();
        });


        return modelMapper;
    }
}
