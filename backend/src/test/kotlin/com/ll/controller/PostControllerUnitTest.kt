package com.ll.controller

import com.ll.domain.post.entity.Post
import com.ll.domain.post.controller.PostController
import com.ll.domain.post.service.PostService
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.http.HttpStatus
import kotlin.test.assertEquals

class PostControllerUnitTest {
    private val postService: PostService = Mockito.mock(PostService::class.java)
    private val postController = PostController(postService)

    @Test
    fun `모든 게시글 조회 성공`(){
        //given 임의의 데이터 생성
        val posts = listOf(Post(id = 1L, title = "Title 1", content = "Content 1"))
        Mockito.`when`(postService.getAllPosts()).thenReturn(posts)

        // when 컨트롤러의 getAllPosts 메소드 호출
        val response = postController.getAllPosts()

        //then 반환된 응답의 상태 코드와 바디가 예상과 일치하는지 검증
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(posts, response.body)
    }
}