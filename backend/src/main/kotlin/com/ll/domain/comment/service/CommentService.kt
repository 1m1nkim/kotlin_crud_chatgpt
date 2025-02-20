package com.ll.domain.comment.service

import com.ll.domain.comment.entity.Comment
import com.ll.domain.comment.repository.CommentRepository
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