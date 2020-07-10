package com.footbaltoday.blog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.validation.Valid;

import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.footbaltoday.entity.Article;
import com.footbaltoday.entity.Comment;
import com.footbaltoday.repository.ArticleRepository;
import com.footbaltoday.repository.CommentRepository;

@Controller
@RequestMapping("/")
public class PublicController {
 
	@Autowired
	private ArticleRepository articleRepository;
	
	@Autowired
	private CommentRepository commentRepository;
	
	@GetMapping("/")
	public String getIndex(@RequestParam Optional<String> page, Model model) {
		int pageNo = 0;
		int pageSize = 6;  
		if(!page.isEmpty() && isInteger(page.get())) {
			 pageNo = Integer.parseInt(page.get()) - 1;
		}
		
		
		Pageable pageable = PageRequest.of(pageNo, pageSize);  
		 Page<Article> articles =  articleRepository.findAllByOrderByCreatedAtDesc(pageable);
 
		 model.addAttribute("articles", articles);
		 int totalPages = articles.getTotalPages();
		 if (totalPages > 0) {  
	            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
	                .boxed()
	                .collect(Collectors.toList());
	         model.addAttribute("pageNumbers", pageNumbers);
	        }
		 //System.out.println(articles);
		return "blog/home";
	}
	
	@PostMapping("/comment/{articleId}")
	public String saveComment(@PathVariable String articleId, @Valid @ModelAttribute("comment") Comment comment, BindingResult result,Model model) {
		
		if(!isInteger(articleId)) {
			return "redirect:/";
		}
		if (result.hasErrors()) {
			return "redirect:/article/"+articleId;
	      }     
		Optional<Article> article = articleRepository.findById(Integer.parseInt(articleId));
		comment.setArticle(article.get());
		comment.setCreatedAt(new Date());
		System.out.println(comment);
		commentRepository.save(comment);
		
		return "redirect:/article/"+articleId;
	}
	
	
	@GetMapping("/article/{id}")
	public String getArticle(@PathVariable String id,Model model) {
		
		if(!isInteger(id)) {
			return "redirect:/";
		}
		int i = Integer.parseInt(id);
		Optional<Article> article = articleRepository.findById(i);
		if(article.isEmpty()) {
			return "redirect:/";
		}
		
		List<Article> list =  articleRepository.findByUser(article.get().getUser());
		List<Article>  articles = new ArrayList<Article>();
		if(list.size() <= 3) {
			articles = list;
		}else {
			Random rand = new Random();
	         
	         while (articles.size() < 3) {
	        	 Article a =  list.get(rand.nextInt(list.size()));	
	        	 if(!articles.contains(a)) {
	        		 articles.add(a);
	        	 }
			}
	         
		} 
		
		
		model.addAttribute("comment", new Comment());
		model.addAttribute("articles", articles);
		model.addAttribute("article", article.get());
		return "blog/article";
	}
	
	
	private  boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
	    // only got here if we didn't return false
	    return true;
	}
	
}
