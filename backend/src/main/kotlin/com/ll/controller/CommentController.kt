package com.ll.controller

import com.ll.entity.Comment
import com.ll.service.CommentService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/comments/")
@Tag(name = "Comment", description = "댓글 관련 Api")
class CommentController (
    private val commentService: CommentService
){
    @GetMapping("/post/{postId}")
    @Operation(summary = "게시글 댓글 조회", description = "특정 게시글에 달린 모든 댓글 조회")
    fun getCommentsByPost(@PathVariable postId: Long): ResponseEntity<List<Comment>> =
        ResponseEntity.ok(commentService.getCommentsByPostId(postId))

    @PostMapping
    @Operation(summary = "댓글 등록", description = "댓글 등록")
    fun addComment(@RequestBody comment: Comment):ResponseEntity<Comment> =
        ResponseEntity.status(HttpStatus.CREATED).body(commentService.addComment(comment))

    @DeleteMapping("/{postId}")
    @Operation(summary = "댓글 삭제", description = "댓글 삭제")
    fun deleteComment(@PathVariable postId: Long): ResponseEntity<Void> {
        commentService.deleteComment(postId)
        return ResponseEntity.noContent().build()
    }

}