package com.footbaltoday.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "articles")
public class Article {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@NotEmpty(message = "should not be empty")
	@Size(min = 5, message = "must be greater than or equal to 5 characters")
	@Column(name = "title")
	private String title;
	
	@Column(name = "tags")
	private String tags;
	
	@Column(name = "body")
	@NotEmpty(message = "should not be empty")
	@Size(min = 50, message = "must be greater than or equal to 50 characters")
	private String body;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at")
	private Date createdAt;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_at")
	private Date updatedAt;
	
	@OneToOne
	@JoinColumn(name = "author_id")
	private User user;
	

    @OneToMany(
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @JoinColumn(name = "article_id")
    private List<Comment> comments = new ArrayList<Comment>();
 
    
	public Article() {
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	
	
	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
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

	public String getUpdatedAt() {
		return timeTostring(createdAt);
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	private String  timeTostring(Date dt) {

		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return sdf.format(dt);
	}

	@Override
	public String toString() {
		return "Article [id=" + id + ", title=" + title + ", tags=" + tags + ", body=" + body + ", createdAt="
				+ createdAt + ", updatedAt=" + updatedAt + ", user=" + user + "]";
	}
	
	
	
}
