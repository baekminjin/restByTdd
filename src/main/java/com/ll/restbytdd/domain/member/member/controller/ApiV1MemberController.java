package com.ll.restbytdd.domain.member.member.controller;


import com.ll.restbytdd.domain.member.member.dto.MemberDto;
import com.ll.restbytdd.domain.member.member.entity.Member;
import com.ll.restbytdd.domain.member.member.service.MemberService;
import com.ll.restbytdd.global.exceptions.ServiceException;
import com.ll.restbytdd.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class ApiV1MemberController {

	private final MemberService memberService;

	record MemberJoinReqBody(
			String username,
			String password,
			String nickname
	) {
	}


	@PostMapping("/join")
	public RsData<MemberDto> join(//Void는 result code와 msg 외 추가 데이터가 없을 경우
								  @RequestBody MemberJoinReqBody reqBody
	) {
		Member member = memberService.join(reqBody.username(), reqBody.password(), reqBody.nickname());

		return new RsData<>(
				"201-1",
				"%s님 환영합니다. 회원가입이 완료되었습니다." .formatted(member.getName()),
				new MemberDto(member)

		);
	}


	record MemberLoginReqBody(
			String username,
			String password
	) {
	}

	record MemberLoginResBody(
			MemberDto item,
			String apiKey
	) {
	}

	@PostMapping("/login")
	public RsData<MemberLoginResBody> login(//Void는 result code와 msg 외 추가 데이터가 없을 경우
								  @RequestBody MemberLoginReqBody reqBody
	) {
		Member member = memberService.findByUsername(reqBody.username())
				.orElseThrow(() -> new ServiceException("401-1","존재하지 않는 사용자입니다."));

		if (!member.matchPassword(reqBody.password()))
			throw new ServiceException("401-1","비밀번호가 일치하지 않습니다.");

		return new RsData<>(
				"200-1",
				"%s님 환영합니다." .formatted(member.getName()),
				new MemberLoginResBody(
						new MemberDto(member),
						member.getApiKey()
				)

		);
	}
}
