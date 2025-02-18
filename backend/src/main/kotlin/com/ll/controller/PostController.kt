package com.ll.controller

import com.ll.entity.Post
import com.ll.service.PostService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/posts")
@Tag(name = "Post", description = "게시글 API")
class PostController (
    val postService: PostService
){
    @GetMapping
    @Operation(summary = "모든 게시글 조회", description = "모든 게시글을 조회합니다.")
    fun getAllPosts(): ResponseEntity<List<Post>> =
        ResponseEntity.ok(postService.getAllPosts())

    @GetMapping("/{id}")
    @Operation(summary = "게시글 조회", description = "게시글 ID로 특정 게시글을 조회합니다.")
    fun getPost(@PathVariable id: Long): ResponseEntity<Post> =
        postService.getPost(id)?.let{ResponseEntity.ok(it)}
            ?: ResponseEntity.notFound().build()


    @PostMapping
    @Operation(summary = "게시글 등록", description = "게시글 등록합니다.")
    fun createPost(@RequestBody post: Post): ResponseEntity<Post> {
        val savePost = postService.createPost(post)
        return ResponseEntity.status(HttpStatus.CREATED).body(savePost)
    }

    @PutMapping("/{id}")
    @Operation(summary = "게시글 수정", description = "게시글 ID로 특정 게시글을 수정합니다.")
    fun updatePost(@PathVariable id: Long, @RequestBody updatedPost: Post): ResponseEntity<Post> =
        postService.updatePost(id, updatedPost)?.let{ ResponseEntity.ok(it)}
            ?: ResponseEntity.notFound().build()


    @DeleteMapping("/{id}")
    @Operation(summary = "게시글 삭제", description = "게시글 ID로 특정 게시글을 삭제합니다.")
    fun deletePost(@PathVariable id: Long): ResponseEntity<Void> =
        postService.deletePost(id)?.let{ResponseEntity.noContent().build()}
            ?: ResponseEntity.notFound().build()

}