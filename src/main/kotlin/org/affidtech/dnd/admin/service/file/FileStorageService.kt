package org.affidtech.dnd.admin.service.file

import org.springframework.core.io.Resource
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

interface FileStorageService {
	fun saveCover(adventureId: UUID, file: MultipartFile): String // возвращает URL
	fun getCoverUrl(adventureId: UUID): String
	fun loadCover(adventureId: UUID): Resource // для отдачи файла через контроллер
}