package com.ll.service

import com.ll.entity.Post
import com.ll.repository.PostRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
@Transactional
class PostService (
    private val postRepository: PostRepository
){
    fun getAllPosts(): List<Post> =
        postRepository.findAll()

    fun getPost(id: Long): Post? =
        postRepository.findById(id).orElse(null)

    fun createPost(post: Post): Post =
        postRepository.save(post)

    fun updatePost(id: Long, updatedPost: Post): Post? {
        val existingPost = postRepository.findById(id).orElse(null) ?: return null
        existingPost.title = updatedPost.title
        existingPost.content = updatedPost.content
        return postRepository.save(existingPost)
    }

    fun deletePost(id: Long): Boolean {
        if(postRepository.existsById(id)){
            postRepository.deleteById(id)
            return true
        }
        return false
    }
}