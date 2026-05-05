package com.blog.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.blog.repository.PostJpaRepository;
import com.blog.repository.PostRepository;
import com.blog.vo.Post;

@Service
public class PostService {

	private final PostRepository postRepository;
	private final PostJpaRepository jpaRepository;

	@Autowired
	public PostService(PostRepository postRepository, PostJpaRepository jpaRepository) {
		this.postRepository = postRepository;
		this.jpaRepository = jpaRepository;
	}

	public Post getPost(Long id) {
		return jpaRepository.findOneById(id);
	}
	
	public List<Post> getPosts() {
		return jpaRepository.findAllByOrderByUpdtDateDesc();
	}
	
	public List<Post> getPostsOrderByUpdtAsc() {
		return postRepository.findPostOrderByUpdtDateAsc();
	}
	
	public List<Post> getPostsOrderByRegDesc() {
		return postRepository.findPostOrderByRegDateDesc();
	}
	
	public List<Post> searchPostByTitle(String query) {
		return jpaRepository.findByTitleContainingOrderByUpdtDateDesc(query);
	}
	
	public List<Post> searchPostByContent(String query) {
		return jpaRepository.findByContentContainingOrderByUpdtDateDesc(query);
	}
	
	public boolean  savePost(Post post) {
		try {
        jpaRepository.save(post);
        return true; 
		} catch (Exception e) {
			return false; 
		}
	}
	
	public boolean deletePost(Long id) {
		Post result = jpaRepository.findOneById(id);
		
		if(result == null)
			return false;
		
		jpaRepository.deleteById(id);		
		return true;
	}
	
	public boolean updatePost(Post post) {
		Post result = jpaRepository.findOneById(post.getId());
		
		if(result == null)
			return false;
		
		if(!StringUtils.isEmpty(post.getTitle())) {
			result.setTitle(post.getTitle());
		}
		
		if(!StringUtils.isEmpty(post.getContent())) {
			result.setContent(post.getContent());
		}
		
		jpaRepository.save(result);
		
		return true;
	}
}
