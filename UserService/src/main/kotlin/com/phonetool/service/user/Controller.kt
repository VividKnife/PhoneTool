package com.phonetool.service.user

import com.google.common.hash.Hashing
import com.phonetool.service.user.entity.AuthToken
import com.phonetool.service.user.entity.Secret
import com.phonetool.service.user.entity.User
import com.phonetool.service.user.exception.ForbiddenException
import com.phonetool.service.user.exception.UserNotFoundException
import com.phonetool.service.user.model.AddUserRequest
import com.phonetool.service.user.model.AuthRequest
import com.phonetool.service.user.model.LoginRequest
import com.phonetool.service.user.model.LookupRequest
import com.phonetool.service.user.repository.AuthTokenRepo
import com.phonetool.service.user.repository.SecretRepo
import com.phonetool.service.user.repository.UserRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.lang.IllegalArgumentException
import java.nio.charset.StandardCharsets
import java.util.Base64
import java.util.UUID

@RestController
class Controller {

    @Autowired
    lateinit var userRepo: UserRepo

    @Autowired
    lateinit var secretRepo: SecretRepo

    @Autowired
    lateinit var authTokenRepo: AuthTokenRepo

    @PostMapping(path = ["/add"])
    fun addNewUser(@RequestBody request: AddUserRequest) {
        val user = User()
        user.userName = request.userName
        user.passwordHash = hash(request.password)
        userRepo.save(user)
    }

    @PostMapping(path = ["/lookup"])
    fun lookup(@RequestBody request: LookupRequest): String {
        try {
            userRepo.getUserByUserName(request.userName)
        } catch (e: EmptyResultDataAccessException) {
            throw UserNotFoundException()
        }
        var secret: Secret
        return try {
            secret = secretRepo.getSecretByUserName(request.userName)
            secret.secret
        } catch (e: EmptyResultDataAccessException) {
            secret = Secret()
            secret.userName = request.userName
            secret.secret = hash(UUID.randomUUID().toString())
            secretRepo.save(secret)
            secret.secret
        }

    }

    @PostMapping(path = ["/login"])
    fun login(@RequestBody request: LoginRequest): String {
        val user: User
        try {
            user = userRepo.getUserByUserName(request.userName)
        } catch (e: EmptyResultDataAccessException) {
            throw UserNotFoundException()
        }
        return try {
            val secret = secretRepo.getSecretByUserName(request.userName)
            if (hash(user.passwordHash + ":" +secret.secret) == request.password) {
                val authToken = AuthToken()
                authToken.userId = user.id
                authToken.userName = user.userName
                authToken.token = hash(UUID.randomUUID().toString())
                authToken.id = UUID.randomUUID().toString()
                authTokenRepo.save(authToken)
                return Base64.getEncoder().encodeToString("${authToken.id}:${authToken.token}".toByteArray())
            } else {
                "failed"
            }
        } catch (e: EmptyResultDataAccessException) {
            "failed"
        }

    }

    @PostMapping(path = ["/auth"])
    fun auth(@RequestBody request: AuthRequest): String {
        try {
        val idAndToken = Base64.getDecoder().decode(request.authToken).toString(StandardCharsets.UTF_8)
        if (idAndToken.contains(":")) {
            val array = idAndToken.split(":")
            if (array.size == 2) {
                val id = array[0]
                val token = array[1]
                try {
                    val authToken = authTokenRepo.getAuthTokenById(id)
                    if (authToken.token == token) {
                        return authToken.userName
                    } else {
                        throw ForbiddenException()
                    }
                } catch (e: EmptyResultDataAccessException) {
                    throw ForbiddenException()
                }
            }
        }} catch (e: IllegalArgumentException) { }
        throw ForbiddenException()
    }

    private fun hash(s: String): String {
        return Hashing.sha256()
                .hashString(s, StandardCharsets.UTF_8)
                .toString()
    }
}

