package com.phonetool.service.user.repository

import com.phonetool.service.user.entity.User
import org.springframework.data.repository.Repository

interface UserRepo: Repository<User, String> {
    fun getUserByUserName(userName: String): User
    fun save(user: User)
}
