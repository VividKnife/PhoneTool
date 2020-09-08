package com.phonetool.service.user.entity

import lombok.Data
import java.sql.Timestamp
import java.util.Date
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Entity
@Data
class AuthToken {
    @Id
    lateinit var id: String

    @Column(nullable = false)
    lateinit var userName: String

    @Column(nullable = false)
    var userId: Int = 0

    @Column(nullable = false)
    lateinit var token: String

    @Column(columnDefinition = "datetime DEFAULT CURRENT_TIMESTAMP")
    var createdAt: Timestamp = Timestamp.from(Date().toInstant())
}
