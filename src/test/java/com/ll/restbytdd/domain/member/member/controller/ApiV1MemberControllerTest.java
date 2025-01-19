package com.ll.restbytdd.domain.member.member.controller;


import com.ll.restbytdd.domain.member.member.entity.Member;
import com.ll.restbytdd.domain.member.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.logging.Handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class ApiV1MemberControllerTest {

	@Autowired
	private MemberService memberService;

	@Autowired
	private MockMvc mvc;


	@Test
	@DisplayName("회원가입")
	void t1() throws Exception {
		ResultActions resultActions = mvc
				.perform(
						post("/api/v1/members/join")
								.content("""
                                        {
                                            "username": "usernew",
                                            "password": "1234",
                                            "nickname": "무명"
                                        }
                                        """.stripIndent())
								.contentType(
										new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8)
								)
				)
				.andDo(print());

		resultActions
				.andExpect(handler().handlerType(ApiV1MemberController.class)) //클래스
				.andExpect(handler().methodName("join")) //메소드
				.andExpect(status().isCreated()) //상태
				.andExpect(jsonPath("$.resultCode").value("201-1")) //상태코드
				.andExpect(jsonPath("$.msg").value("무명님 환영합니다. 회원가입이 완료되었습니다."))

				.andExpect(jsonPath("$.data").exists())
				.andExpect(jsonPath("$.data.id").isNumber())
				.andExpect(jsonPath("$.data.createDate").isString())
				.andExpect(jsonPath("$.data.modifyDate").isString())
				.andExpect(jsonPath("$.data.nickname").value("무명"));

		Member member = memberService.findByUsername("usernew").get();
		assertThat(member.getNickname()).isEqualTo("무명");
	}

	@Test
	@DisplayName("회원가입 시 이미 사용중인 username, 409")
	void t2() throws Exception {
		ResultActions resultActions = mvc
				.perform(
						post("/api/v1/members/join")
								.content("""
										{
										    "username": "user1",
										    "password": "1234",
										    "nickname": "무명"
										}
										""".stripIndent())
								.contentType(
										new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8)
								)
				)
				.andDo(print());

		resultActions
				.andExpect(handler().handlerType(ApiV1MemberController.class)) //클래스
				.andExpect(handler().methodName("join")) //메소드
				.andExpect(status().isConflict()) //상태
				.andExpect(jsonPath("$.resultCode").value("409-1")) //상태코드
				.andExpect(jsonPath("$.msg").value("해당 username은 이미 사용중입니다."));

	}


	@Test
	@DisplayName("로그인")
	void t3() throws Exception {
		ResultActions resultActions = mvc
				.perform(
						post("/api/v1/members/login")
								.content("""
                                        {
                                            "username": "user1",
                                            "password": "1234"
                                        }
                                        """.stripIndent())
								.contentType(
										new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8)
								)
				)
				.andDo(print());

		resultActions
				.andExpect(handler().handlerType(ApiV1MemberController.class)) //클래스
				.andExpect(handler().methodName("login")) //메소드
				.andExpect(status().isOk()) //상태
				.andExpect(jsonPath("$.resultCode").value("200-1")) //상태코드
				.andExpect(jsonPath("$.msg").value("유저1님 환영합니다."))

				.andExpect(jsonPath("$.data").exists())
				.andExpect(jsonPath("$.data.item").exists())
				.andExpect(jsonPath("$.data.item.id").isNumber())
				.andExpect(jsonPath("$.data.item.createDate").isString())
				.andExpect(jsonPath("$.data.item.modifyDate").isString())
				.andExpect(jsonPath("$.data.item.nickname").value("유저1"))
				.andExpect(jsonPath("$.data.apiKey").isString());

	}
}

