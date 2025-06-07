package org.affidtech.dnd.admin.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.CONFLICT)
class AlreadyExistsException(message: String): RuntimeException(message)
