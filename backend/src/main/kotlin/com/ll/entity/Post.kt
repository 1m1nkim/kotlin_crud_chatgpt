package com.ll.entity

import jakarta.persistence.*

@Entity
@Table(name = "posts")
open class Post(        //open을 해야 Jpa를 원활하게 사용할 수 있음
                        //기본적으로 코틀린은 class 생성 시 private final로 생성하기 때문에
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(nullable = false, length = 200)
    var title: String,

    @Lob
    @Column(nullable = false)
    var content: String,

    @OneToMany(mappedBy = "post", cascade = [CascadeType.ALL], orphanRemoval = true)
    var comments: MutableList<Comment> = mutableListOf()
)