package com.blog.dto;

public class CommentDTO {
    private Long postId;
    private String user;
    private String comment;

    // Tambahkan Getter dan Setter
    public Long getPostId() { return postId; }
    public void setPostId(Long postId) { this.postId = postId; }

    public String getUser() { return user; }
    public void setUser(String user) { this.user = user; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    
}
