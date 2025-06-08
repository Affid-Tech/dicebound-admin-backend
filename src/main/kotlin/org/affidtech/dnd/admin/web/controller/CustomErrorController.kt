package org.affidtech.dnd.admin.web.controller

import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.WebRequest
import org.springframework.http.ResponseEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.servlet.error.ErrorAttributes

@RestController
class CustomErrorController(
	@Autowired private val errorAttributes: ErrorAttributes
) : ErrorController {
	@RequestMapping("/error")
	fun handleError(webRequest: WebRequest): ResponseEntity<Map<String, Any>> {
		val attributes: Map<String, Any> = errorAttributes.getErrorAttributes(webRequest, ErrorAttributeOptions.defaults())
		return ResponseEntity.status(attributes["status"] as Int).body(attributes)
	}
}