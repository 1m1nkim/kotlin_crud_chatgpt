package com.ll.repository

import com.ll.entity.Post
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PostRepository : JpaRepository<Post, Long>{
    fun findByTitleContaining(keyword: String): List<Post>
}