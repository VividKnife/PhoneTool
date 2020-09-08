package com.phonetool.service.user.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.RuntimeException

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "ForbiddenException")
class ForbiddenException: RuntimeException()
