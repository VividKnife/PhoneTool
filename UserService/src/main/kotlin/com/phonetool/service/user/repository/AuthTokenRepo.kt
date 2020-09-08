package com.phonetool.service.user.repository

import com.phonetool.service.user.entity.AuthToken
import org.springframework.data.repository.Repository

interface AuthTokenRepo: Repository<AuthToken, String> {

    fun save(token: AuthToken)

    fun getAuthTokenById(id: String): AuthToken
}
