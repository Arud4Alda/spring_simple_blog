package com.blog; 

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.blog.service.CommentService;
import com.blog.service.PostService;
import com.blog.vo.Comment;
import com.blog.vo.Post;
import com.blog.vo.Result;
import com.blog.repository.PostJpaRepository;
import com.blog.repository.PostRepository;
import com.blog.repository.CommentJpaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AllInOneTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    // Kita Mock di level Repository agar tidak merusak database asli
    @MockBean
    private PostJpaRepository jpaRepository;

    @MockBean
    private PostRepository postRepository;

    @MockBean
    private CommentJpaRepository commentJpaRepository;

    private ObjectMapper objectMapper;
    private Post samplePost;
    private Comment sampleComment;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        
        // Persiapan data Post
        samplePost = new Post();
        samplePost.setId(1L);
        samplePost.setTitle("Belajar Spring Boot");
        samplePost.setContent("Materi PP Lanjut");

        // Persiapan data Comment
        sampleComment = new Comment();
        sampleComment.setId(10L);
        sampleComment.setPostId(1L);
        sampleComment.setNote("Sangat bermanfaat!");
        
    }


    // ==========================================
    // BAGIAN 1: PENGUJIAN POST SERVICE (6 Test)
    // ==========================================

    @Test
    public void testGetPosts_ShouldReturnList() {
        when(jpaRepository.findAllByOrderByUpdtDateDesc()).thenReturn(Arrays.asList(samplePost));
        List<Post> posts = postService.getPosts(); 
        assertFalse(posts.isEmpty());
        assertEquals(1, posts.size());
        verify(jpaRepository, times(1)).findAllByOrderByUpdtDateDesc();
    }

    @Test
    public void testGetPost_WhenIdExists_ShouldReturnPost() {
        when(jpaRepository.findOneById(1L)).thenReturn(samplePost);
        Post post = postService.getPost(1L); 
        assertNotNull(post);
        assertEquals("Belajar Spring Boot", post.getTitle());
    }

    @Test
    public void testGetPost_WhenIdDoesNotExist_ShouldReturnNull() {
        when(jpaRepository.findOneById(99L)).thenReturn(null);
        Post post = postService.getPost(99L);
        assertNull(post);
    }

    @Test
    public void testSavePost_ShouldReturnTrue() {
        when(jpaRepository.save(any(Post.class))).thenReturn(samplePost);
        boolean isSuccess = postService.savePost(samplePost); 
        assertTrue(isSuccess);
        verify(jpaRepository, times(1)).save(samplePost);
    }

    @Test
    public void testDeletePost_ShouldReturnTrue() {
        when(jpaRepository.findOneById(1L)).thenReturn(samplePost);
        doNothing().when(jpaRepository).deleteById(1L);
        boolean isSuccess = postService.deletePost(1L); 
        assertTrue(isSuccess);
        verify(jpaRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testSearchPostByTitle_ShouldReturnMatchingPosts() {
        when(jpaRepository.findByTitleContainingOrderByUpdtDateDesc("Spring"))
            .thenReturn(Arrays.asList(samplePost));
        List<Post> results = postService.searchPostByTitle("Spring"); 
        assertEquals(1, results.size());
    }


    // ==========================================
    // BAGIAN 2: PENGUJIAN COMMENT SERVICE (3 Test)
    // ==========================================

    @Test
    public void testGetCommentList_ShouldReturnList() {
        when(commentJpaRepository.findAllByPostIdOrderByRegDateDesc(1L)).thenReturn(Arrays.asList(sampleComment));
        List<Comment> comments = commentService.getCommentList(1L);
        assertEquals(1, comments.size());
        assertEquals("Sangat bermanfaat!", comments.get(0).getNote());
    }

    @Test
    public void testSaveComment_ShouldReturnTrue() {
        when(commentJpaRepository.save(any(Comment.class))).thenReturn(sampleComment);
        boolean isSuccess = commentService.saveComment(sampleComment);
        assertTrue(isSuccess);
    }

    @Test
    public void testDeleteComment_ShouldReturnTrue() {
        when(commentJpaRepository.findOneById(10L)).thenReturn(sampleComment);
        doNothing().when(commentJpaRepository).deleteById(10L);
        boolean isSuccess = commentService.deleteComment(10L);
        assertTrue(isSuccess);
        verify(commentJpaRepository, times(1)).deleteById(10L);
    }


    // ==========================================
    // BAGIAN 3: PENGUJIAN CONTROLLER (3 Test)
    // ==========================================

    @Test
    public void testGetPostsAPI_ShouldReturnStatusOk() throws Exception {
        when(jpaRepository.findAllByOrderByUpdtDateDesc()).thenReturn(Arrays.asList(samplePost));
        mockMvc.perform(get("/posts") 
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Belajar Spring Boot"));
    }

    @Test
    public void testGetPostByIdAPI_ShouldReturnPost() throws Exception {
        when(jpaRepository.findOneById(1L)).thenReturn(samplePost);
        mockMvc.perform(get("/post").param("id", "1")) 
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testCreatePostAPI_ShouldReturnStatusOk() throws Exception {
        when(jpaRepository.save(any(Post.class))).thenReturn(samplePost);
        mockMvc.perform(post("/post")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(samplePost))) 
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(200)); 
    }


    // ==========================================
    // BAGIAN 4: PENGUJIAN MODEL DATA (5 Test)
    // ==========================================

    @Test
    public void testPostEntity_GettersAndSetters() {
        Post post = new Post();
        post.setId(5L);
        post.setTitle("Testing");
        post.setContent("Isi konten");
        assertEquals(Long.valueOf(5L), post.getId());
        assertEquals("Testing", post.getTitle());
        assertEquals("Isi konten", post.getContent());
    }

    @Test
    public void testCommentEntity_GettersAndSetters() {
        Comment comment = new Comment();
        comment.setId(2L);
        assertEquals(Long.valueOf(2L), comment.getId());
    }

    @Test
    public void testResult_SuccessFlow() {
        Result result = new Result(200, "Berhasil");
        assertEquals(200, result.getIndex());
        assertEquals("Berhasil", result.getMessage());
    }

    @Test
    public void testResult_ErrorFlow() {
        Result result = new Result(500, "Terjadi kesalahan");
        assertEquals(500, result.getIndex());
        assertEquals("Terjadi kesalahan", result.getMessage());
    }

    @Test
    public void testPostEntity_InitialState_ShouldBeNull() {
        Post post = new Post();
        assertNull(post.getId());
        assertNull(post.getTitle());
    }
}