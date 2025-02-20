package com.ll.domain.comment.entity

import com.ll.domain.post.entity.Post
import jakarta.persistence.*

@Entity
@Table(name = "comments")
class Comment (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(nullable = false, length = 500)
    var content: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    var post: Post
){

}