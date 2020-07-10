package com.footbaltoday.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "comments")
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@NotEmpty(message = "Please enter your comment")
	@Column(name = "content")
	private String content;
	
	@Column(name = "email")
	private String email;
	
	@NotEmpty(message = "Please enter your name")
	@Column(name = "author")
	private String author;
	
	@Column(name = "checked")
	private int checked;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at")
	private Date createdAt;
	
	@OneToOne
	@JoinColumn(name = "article_id")
	private Article article;
	
	public Comment() {
		this.checked = 0;
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getChecked() {
		return checked;
	}

	public void setChecked(int checked) {
		this.checked = checked;
	}

	public String getCreatedAt() {
		return timeTostring(createdAt);
	}
	
	public Date getCreatedAtT() {
		return createdAt;
		//return timeTostring(createdAt);
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}
	
	private String  timeTostring(Date dt) {

		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return sdf.format(dt);
	}

	@Override
	public String toString() {
		return "Comment [id=" + id + ", content=" + content + ", email=" + email + ", author=" + author + ", checked="
				+ checked + ", createdAt=" + createdAt + ", article=" + article + "]";
	}
	
	
}
