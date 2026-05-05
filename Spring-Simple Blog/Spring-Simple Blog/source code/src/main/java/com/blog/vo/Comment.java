package com.blog.vo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "comment")
public class Comment {

	@Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="id")
	private Long id;
	
	@Column(name="postId")
	private Long postId;
    
    @Column(name="user")
	private String user;
    
    @Column(name="note")
	private String note;
    
    @Column(name="regDate")
	private Date regDate;

	public Comment() {
	}

	public Comment(Long postId, String user, String note) {
		this.postId = postId;
		this.user = user;
		this.note = note;
		this.regDate = new Date();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPostId() {
		return postId;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Date getRegDate() {
		return regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}
}
