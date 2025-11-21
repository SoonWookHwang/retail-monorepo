package com.retail.member.security;

import com.retail.common.security.SecurityConfigBase;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(SecurityConfigBase.class)
public class SecurityConfig {
}
