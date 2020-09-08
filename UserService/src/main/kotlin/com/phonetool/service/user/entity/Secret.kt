package com.phonetool.service.user.entity

import lombok.Data
import java.sql.Timestamp
import java.util.Date
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Index
import javax.persistence.Table

@Entity
@Data
@Table(indexes = [
    Index(columnList = "userName,id")
])
class Secret {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int = 0

    @Column(nullable = false)
    lateinit var userName: String

    @Column(nullable = false)
    lateinit var secret: String

    @Column(columnDefinition = "datetime DEFAULT CURRENT_TIMESTAMP")
    var createdAt: Timestamp = Timestamp.from(Date().toInstant())
}
