package com.footbaltoday.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.footbaltoday.entity.Article;
import com.footbaltoday.entity.Comment;
import com.footbaltoday.entity.User;

public interface ArticleRepository extends JpaRepository<Article, Integer> {

	public List<Article> findByUser(User user);
	
	public Page<Article> findAllByOrderByCreatedAtDesc(Pageable page);
	
	/*
	 @Query("from EstimateOptions options,BillNumber billnumber join fetch billnumber.Company company2 join fetch company2.user user2 
	  join fetch options.Company company join fetch company.user user where user.email = :userName and user2.email = :userName")
	 */
	@Query("SELECT  DISTINCT  c  FROM Article a, Comment c WHERE a.id = c.article.id and a.user.id=:userId and c.checked=0")
    public List<Comment> findCommentsNotChcked(@Param("userId") long userId);
	
	/*
	@Query(value = "SELECT count(ac) as count, function('date_format', max(ac.createdAt), '%M') as date FROM Article ac " +
	        "  GROUP BY function('date_format', ac.createdAt, '%m')")
	public List<Map<String,Object>> findArticleGroupedByMounth(@Param("userId") long userId);
	*/
	
	@Query(value = "SELECT concat( count(ac) , '|', function('date_format', max(ac.createdAt), '%M') ) FROM Article ac " +
	        "  GROUP BY function('date_format', ac.createdAt, '%m')")
	public List<String> findArticleGroupedByMounth(@Param("userId") long userId);
	
}
