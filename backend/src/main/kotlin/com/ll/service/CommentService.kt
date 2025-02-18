package com.ll.service

import com.ll.entity.Comment
import com.ll.repository.CommentRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
@Transactional
class CommentService (
    private val commentRepository: CommentRepository
){
    fun getCommentsByPostId(postId: Long): List<Comment> =
        commentRepository.findByPostId(postId)

    fun addComment(comment: Comment): Comment =
        commentRepository.save(comment)

    fun deleteComment(commentId: Long) =
        commentRepository.deleteById(commentId)
}