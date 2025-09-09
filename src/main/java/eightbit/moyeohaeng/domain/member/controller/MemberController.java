package eightbit.moyeohaeng.domain.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eightbit.moyeohaeng.domain.member.dto.response.MemberInfoResponse;
import eightbit.moyeohaeng.domain.member.service.MemberService;
import eightbit.moyeohaeng.global.security.CustomUserDetails;
import eightbit.moyeohaeng.global.success.CommonSuccessCode;
import eightbit.moyeohaeng.global.success.SuccessResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/me")
    public SuccessResponse<MemberInfoResponse> getMyInfo(@AuthenticationPrincipal CustomUserDetails user) {
        MemberInfoResponse memberInfo = MemberInfoResponse.from(memberService.findById(user.getId()));
        return SuccessResponse.of(CommonSuccessCode.SELECT_SUCCESS, memberInfo);
    }
}
