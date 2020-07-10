package com.footbaltoday.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.footbaltoday.entity.Article;
import com.footbaltoday.entity.Comment;

public interface CommentRepository  extends JpaRepository<Comment, Integer>{

	public List<Comment> findByArticle(Article article);
}
