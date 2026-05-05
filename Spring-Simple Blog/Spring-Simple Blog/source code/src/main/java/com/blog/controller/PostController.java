package com.blog.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.validation.BindingResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blog.dto.PostDTO;
import com.blog.service.PostService;
import com.blog.vo.Post;
import com.blog.vo.Result;
import org.springframework.web.util.HtmlUtils;

@RestController
public class PostController {
	Logger log = LoggerFactory.getLogger(this.getClass());
	private static final String SUCCESS_MESSAGE = "Success";
	private final PostService postService;

	@Autowired
	public PostController(PostService postService) {
		this.postService = postService;
	}
	
	@GetMapping("/post")
	public Post getPost(@RequestParam("id") Long id) {
		return postService.getPost(id);
	}
	
	@GetMapping("/posts")
	public List<Post> getPosts() {
		return postService.getPosts();
	}
	
	@GetMapping("/posts/updtdate/asc")
	public List<Post> getPostsOrderByUpdtAsc() {
		return postService.getPostsOrderByUpdtAsc();
	}
	
	@GetMapping("/posts/regdate/desc")
	public List<Post> getPostsOrderByRegDesc() {
		return postService.getPostsOrderByRegDesc();
	}
	
	
	@GetMapping("/posts/search/title")
	public List<Post> searchByTitle(@RequestParam("query") String query) {
		return postService.searchPostByTitle(query);
	}
	
	//for Exercise 4-4
	@GetMapping("/posts/search/content")
	public List<Post> searchByContent(@RequestParam("query") String query) {
		return postService.searchPostByContent(query);
	}
	
	@PostMapping("/post")
    public Object savePost(HttpServletResponse response, @Valid @RequestBody PostDTO postDto, BindingResult bindingResult)  {
		if (bindingResult.hasErrors()) {
            // Ambil pesan error dari @NotBlank (misal: "Judul artikel tidak boleh kosong!")
            String errorMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
            
            // Kembalikan pesan error menggunakan class Result milikmu
            return new Result(400, errorMessage);
        }        
        
        String safeUser = HtmlUtils.htmlEscape(postDto.getUser());
        String safeTitle = HtmlUtils.htmlEscape(postDto.getTitle());
        String safeContent = HtmlUtils.htmlEscape(postDto.getContent());

        Post post = new Post(safeUser, safeTitle, safeContent);
        
        boolean isSuccess = postService.savePost(post);
        
        if(isSuccess) {
            return new Result(200, SUCCESS_MESSAGE);
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new Result(500, "Fail");
        }
    }
	
	@DeleteMapping("/post")
	public Object deletePost(HttpServletResponse response, @RequestParam("id") Long id)  {
		boolean isSuccess = postService.deletePost(id);
		
		log.info("id ::: {}", id);
		
		if(isSuccess) {
			return new Result(200, SUCCESS_MESSAGE);
		} else {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return new Result(500, "Fail");
		}
	}
	
	@PutMapping("/post")
    public Object modifyPost(HttpServletResponse response, @Valid @RequestBody PostDTO postDto, BindingResult bindingResult)  {      
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return new Result(400, errorMessage);
        }

        String safeTitle = HtmlUtils.htmlEscape(postDto.getTitle());
        String safeContent = HtmlUtils.htmlEscape(postDto.getContent());

        Post post = new Post(postDto.getId(), safeTitle, safeContent);
        
        boolean isSuccess = postService.updatePost(post);
                
        if(isSuccess) {
            return new Result(200,SUCCESS_MESSAGE);
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new Result(500, "Fail");
        }
    }
}
