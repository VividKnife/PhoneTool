package com.phonetool.service.user.repository

import com.phonetool.service.user.entity.Secret
import org.springframework.data.repository.Repository

interface SecretRepo: Repository<Secret, Int> {
    fun getSecretByUserName(userName: String): Secret

    fun save(secret: Secret)
}
