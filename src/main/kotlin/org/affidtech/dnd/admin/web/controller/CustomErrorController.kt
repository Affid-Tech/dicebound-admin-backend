package org.affidtech.dnd.admin.web.controller

import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.webmvc.error.ErrorAttributes
import org.springframework.boot.webmvc.error.ErrorController
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.WebRequest

@RestController
class CustomErrorController(
	private val errorAttributes: ErrorAttributes
) : ErrorController {
	@RequestMapping("/error")
	fun handleError(webRequest: WebRequest): ResponseEntity<Map<String, Any?>> {
		val attributes = errorAttributes.getErrorAttributes(webRequest, ErrorAttributeOptions.defaults()).toMap()
		return ResponseEntity.status(attributes["status"] as Int).body(attributes)
	}
}