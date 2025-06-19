package org.affidtech.dnd.admin.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class ConfigPrinter(@Value("\${spring.security.user.name}") val username: String, @Value("\${spring.security.user.password}") val adminPass: String) {
	
	fun printConfig() {
		println("Username: $username")
		println("Password: $adminPass")
	}
}