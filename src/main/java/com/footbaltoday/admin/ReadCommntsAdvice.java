package com.footbaltoday.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.footbaltoday.entity.Article;
import com.footbaltoday.entity.Comment;
import com.footbaltoday.entity.User;
import com.footbaltoday.repository.ArticleRepository;
import com.footbaltoday.repository.UserRepository;

@ControllerAdvice
public class ReadCommntsAdvice {

	@Autowired
	private ArticleRepository articleRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	 @ModelAttribute
     public void addBugetToModel(Model model) {
		 User user = userRepository.findByEmail(getCurrentEmailUser());
		 if(user != null) {
			 List<Comment> comments = articleRepository.findCommentsNotChcked(user.getId());
			 /*for (Comment comment : comments) {
				
				 System.out.println(comment);
			}*/
	        model.addAttribute("count_admin_cmnts", comments.size());
		 }
		
    }
	 
	 private String getCurrentEmailUser() {
			String email = "";
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (principal instanceof UserDetails) {
				 email = ((UserDetails)principal).getUsername();
				} else {
				email = principal.toString();
				}
			return email;
		}
		
}
