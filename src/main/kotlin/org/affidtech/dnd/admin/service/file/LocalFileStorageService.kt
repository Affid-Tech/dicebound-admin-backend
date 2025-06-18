package org.affidtech.dnd.admin.service.file

import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.UUID

@Service
class LocalFileStorageService(
	@Value("\${app.file-storage.path}") private val fileStoragePath: String,
) : FileStorageService {
	private val coverDir = Paths.get(fileStoragePath)
	
	override fun saveCover(adventureId: UUID, file: MultipartFile): String {
		Files.createDirectories(coverDir)
		val target = coverDir.resolve("$adventureId")
		file.inputStream.use { input ->
			Files.copy(input, target, StandardCopyOption.REPLACE_EXISTING)
		}
		return getCoverUrl(adventureId)
	}
	
	override fun getCoverUrl(adventureId: UUID): String = "/files/adventure-covers/$adventureId"  // URL для отдачи через контроллер
	
	override fun loadCover(adventureId: UUID): Resource {
		val file = coverDir.resolve("$adventureId")
		return UrlResource(file.toUri())
	}
}
