package com.ll.restbytdd.domain.member.member.controller;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.logging.Handler;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class ApiV1MemberControllerTest {


	@Autowired
	private MockMvc mvc;

	@Test
	@DisplayName("회원가입")
	void t1() throws Exception {
		ResultActions resultActions = mvc
				.perform(
						post("/api/v1/member/join")
				)
				.andDo(print());

		resultActions
				.andExpect(handler().handlerType(ApiV1MemberController.class)) //ApiV1MemberController 클래스가 처리하면 좋겠다
				.andExpect(handler().methodName("join")) //join 메소드가 처리하면 좋겠다
				.andExpect(status().isCreated());

	}
}
