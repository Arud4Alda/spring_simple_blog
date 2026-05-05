package com.blog.dto;

public class PostDTO {
    private Long id;
    private String user;
    private String title;
    private String content;

    // Tambahkan Getter dan Setter untuk ketiga variabel di atas
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUser() { return user; }
    public void setUser(String user) { this.user = user; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
