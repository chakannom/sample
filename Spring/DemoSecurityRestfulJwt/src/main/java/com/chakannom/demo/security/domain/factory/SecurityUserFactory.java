package com.chakannom.demo.security.domain.factory;

import org.springframework.security.core.authority.AuthorityUtils;

import com.chakannom.demo.security.domain.user.SecurityUserVo;
import com.chakannom.demo.security.domain.user.UserVo;

public class SecurityUserFactory {

	public static SecurityUserVo create(UserVo userVo) {
		return new SecurityUserVo(
				userVo.getUserId(),
				userVo.getPassWd(),
				AuthorityUtils.commaSeparatedStringToAuthorityList(userVo.getAuthorities()));
	}
}
