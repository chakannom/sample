package com.chakannom.demo.security.domain.jason;

import com.chakannom.demo.security.domain.common.CommonVo;

public class AuthenticationResponse extends CommonVo {

	private static final long serialVersionUID = 1L;

	private String token;

	public AuthenticationResponse() {
		super();
	}

	public AuthenticationResponse(String token) {
		this.setToken(token);
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
