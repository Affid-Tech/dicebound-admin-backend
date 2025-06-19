package org.affidtech.dnd.admin.web.controller

import jakarta.servlet.http.HttpServletResponse
import org.affidtech.dnd.admin.config.ConfigPrinter
import org.affidtech.dnd.admin.service.file.FileStorageService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class FileController(val storage: FileStorageService, val configPrinter: ConfigPrinter) {
	@GetMapping("/files/adventure-covers/{id}")
	fun getCover(@PathVariable id: UUID, response: HttpServletResponse) {
		val resource = storage.loadCover(id)
		response.contentType = "image/png"
		resource.inputStream.use { input ->
			input.copyTo(response.outputStream)
		}
	}
	
	@GetMapping("/files/test")
	fun testConfig() {
		configPrinter.printConfig()
	}
}
