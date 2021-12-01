package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    /**
     *  등록 V1: 요청 값으로 Member 엔티티를 직접 받는다.
     *  문제점
     *  - 엔티티에 프레젠테이션 계층을 위한 로직이 추가된다.
     *  - 엔티티에 API 검증을 위한 로직이 들어간다. (@NotEmpty 등등)
     *  - 실무에서는 회원 엔티티를 위한 API가 다양하게 만들어지는데, 한 엔티티에 각각의 API를
     *    위한 모든 요청 요구사항을 담기는 어렵다.
     *  - 엔티티가 변경되면 API 스펙이 변한다.
     *  결론
     *  - API 요청 스펙에 맞추어 별도의 DTO를 파라미터로 받는다.
     *
     * @param member 엔티티 Member 타입의 member
     * @return CreateMemberResponse
     */
    @PostMapping(value = "/api/v1/members"
            , consumes = MediaType.APPLICATION_JSON_VALUE
            , produces = MediaType.APPLICATION_JSON_VALUE
    )
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long memberId = memberService.join(member);
        return new CreateMemberResponse(memberId);
    }

    /**
     * 등록 V2: 요청 값으로 별도의 DTO를 파라미터
     * @param req CreateMemberRequest 타입의 커맨드 객체
     * @return CreateMemberResponse 타입의 응답 객체
     */
    @PostMapping(value = "/api/v2/members"
            , consumes = MediaType.APPLICATION_JSON_VALUE
            , produces = MediaType.APPLICATION_JSON_VALUE
    )
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest req) {
        Member member = Member.builder().name(req.getName()).build();
        Long memberId = memberService.join(member);
        return new CreateMemberResponse(memberId);
    }

    /**
     * 수정 API
     * @param memberId Member ID
     * @param req UpdateMemberRequest 타입의 요청
     * @return UpdateMemberResponse
     */
    @PutMapping(value = "/api/v2/members/{memberId}")
    public UpdateMemberResponse updateMemberV2(
            @PathVariable Long memberId,
            @RequestBody @Valid UpdateMemberRequest req) {
        memberService.update(memberId, req.getName());
        Member member = memberService.FindById(memberId);
        return new UpdateMemberResponse(memberId, member.getName());
    }

    @GetMapping("/api/v1/members")
    public List<Member> listMemberV1() {
        return memberService.findMembers();
    }


    //req, res

    @Data
    static class CreateMemberRequest {
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class CreateMemberResponse {
        private Long id;
    }

    @Data
    static class UpdateMemberRequest {
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }
}
