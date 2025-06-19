package org.affidtech.dnd.admin.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain


@Configuration
class APISecurityConfig {
	
	@Bean
	fun filterChain(http: HttpSecurity): SecurityFilterChain {
		http
			.csrf { csrf ->
				csrf.disable()
			}
			.authorizeHttpRequests { auth ->
				auth
					.anyRequest().permitAll()
			}
		return http.build()
	}
}