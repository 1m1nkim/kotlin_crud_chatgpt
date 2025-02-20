package com.ll.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ll.domain.post.entity.Post
import com.ll.domain.post.controller.PostController
import com.ll.domain.post.service.PostService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(PostController::class)
class PostControllerMockTest (
    @Autowired val mockMvc: MockMvc,
    @Autowired val objectMapper: ObjectMapper
){
    @MockBean
    lateinit  var postService: PostService

    @Test
    fun `모든 게시글 조회 성공`(){
        val posts = listOf(Post(id = 1L, title="Title 1", content = "Content 1"))
        `when`(postService.getAllPosts()).thenReturn(posts)

        mockMvc.perform(get("/api/posts"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].title").value("Title 1"))
            .andExpect(jsonPath("$[0].content").value("Content 1"))
    }
}