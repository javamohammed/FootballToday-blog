package com.footbaltoday.admin;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.footbaltoday.entity.Article;
import com.footbaltoday.entity.Chart;
import com.footbaltoday.entity.Comment;
import com.footbaltoday.entity.User;
import com.footbaltoday.repository.ArticleRepository;
import com.footbaltoday.repository.CommentRepository;
import com.footbaltoday.repository.UserRepository;
import com.footbaltoday.service.UserDetailsServiceImpl;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	private final String UPLOAD_DIR = "uploads";   
	 
	@Autowired
	private ArticleRepository articleRepository;   
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;
        
	@GetMapping("/dashboard")
	public String  dashboard(Model model) {
		User user = userRepository.findByEmail(getCurrentEmailUser());
		List<String> chartsDOA = articleRepository.findArticleGroupedByMounth(user.getId());
		List<Chart> charts = new ArrayList<Chart>();

		for (String ch : chartsDOA) {
			String[] str = ch.split("\\|");
			charts.add(new Chart(str[0], str[1]));
		}
		
		
		model.addAttribute("name", "Mohammed");
		model.addAttribute("charts", charts);
		model.addAttribute("active", "dashboard");
		return "admin/index";
	}
	
	@GetMapping("/new") 
	public String newArticle(Model model) {
		Article article = new Article();
	    model.addAttribute("active", "newArticle");
	    model.addAttribute("article", article);
		return "admin/new_article";
	}
	@GetMapping("/article/{id}") 
	public String showArticle(@PathVariable int id, Model model) {
		User user = userRepository.findByEmail(getCurrentEmailUser());
		Article article =articleRepository.getOne(id);
		if(article.getUser().getId() != user.getId()) {
			return "redirect:/admin/articles";
		}
		model.addAttribute("active", "Articles");
	    model.addAttribute("article", article);
		return "admin/article";
	}
	
	@GetMapping("/edit/{id}") 
	public String showEditArticle(@PathVariable int id, Model model) {
		Article article =articleRepository.getOne(id);
		User user = userRepository.findByEmail(getCurrentEmailUser());
		if(article.getUser().getId() != user.getId()) {
			return "redirect:/admin/articles";
		}
		model.addAttribute("active", "Articles");
	    model.addAttribute("article", article);
		return "admin/new_article";
	}
	
	@GetMapping("/delete/{id}") 
	public String deleteArticle(@PathVariable int id, Model model) {
		Article article =articleRepository.getOne(id);
		User user = userRepository.findByEmail(getCurrentEmailUser());
		if(article.getUser().getId() != user.getId()) {
			return "redirect:/admin/articles";
		}
		articleRepository.delete(article);

		return "redirect:/admin/articles?success=delete";
	}
	
	@GetMapping("/comment/{id}") 
	public String showComment(@PathVariable int id, Model model) {
		User user = userRepository.findByEmail(getCurrentEmailUser());
		Comment comment = commentRepository.getOne(id);
		if(comment.getArticle().getUser().getId() != user.getId()) {
			return "redirect:/admin/articles";
		}
		model.addAttribute("active", "Comments");
	    model.addAttribute("comment", comment);
		return "admin/comment";
	}
	
	@GetMapping("/comment/delete/{id}") 
	public String deleteComment(@PathVariable int id, Model model) {
		Comment comment = commentRepository.getOne(id);
		User user = userRepository.findByEmail(getCurrentEmailUser());
		if(comment.getArticle().getUser().getId() != user.getId()) {
			return "redirect:/admin/articles";
		}
		commentRepository.delete(comment);

		return "redirect:/admin/comments?success=denied";
	}

	@GetMapping("/comment/allowed/{id}") 
	public String allowedComment(@PathVariable int id, Model model) {
		Comment comment = commentRepository.getOne(id);
		User user = userRepository.findByEmail(getCurrentEmailUser());
		if(comment.getArticle().getUser().getId() != user.getId()) {
			return "redirect:/admin/articles";
		}
		comment.setChecked(1);
		commentRepository.save(comment);

		return "redirect:/admin/comments?success=allowed";
	}

	@PostMapping("/new")
	public String newArticleSave(@Valid @ModelAttribute("article") Article article, BindingResult result,Model model) {
		
		if (result.hasErrors()) {
			model.addAttribute("active", "newArticle");
	         return "admin/new_article";
	      }     
		
		article.setUser(userRepository.findByEmail(getCurrentEmailUser()));
		article.setCreatedAt(new Date());
		article.setUpdatedAt(new Date());
		articleRepository.save(article);
		
		return "redirect:/admin/articles?success=saved";
	}
	
	@GetMapping("/articles")
	public String articles(@RequestParam(required = false) String success, Model model) {
		
		User user = userRepository.findByEmail(getCurrentEmailUser());
		
		List<Article> articles = articleRepository.findByUser(user);
		model.addAttribute("articles", articles);
	    model.addAttribute("active", "Articles");
	    if(success != null) {
	    	if(success.equalsIgnoreCase("delete")) {
	    		 model.addAttribute("message", "<b>Well done!</b> You successfully delete an Article.");
	    	}else if(success.equalsIgnoreCase("saved")) {
	    		model.addAttribute("message", "<b>Well done!</b> You successfully save an Article.");
	    	}
	    }
		return "admin/articles";
	}
	
	@GetMapping("/comments")
	public String comments(@RequestParam(required = false) String success, Model model) {
		
		User user = userRepository.findByEmail(getCurrentEmailUser());
		
		 List<Comment> comments = articleRepository.findCommentsNotChcked(user.getId());
		model.addAttribute("comments", comments);
	    model.addAttribute("active", "Comments");
	    if(success != null) {
	    	if(success.equalsIgnoreCase("denied")) {
	    		 model.addAttribute("message", "<b>Well done!</b> You successfully denied a comment.");
	    	}else if(success.equalsIgnoreCase("allowed")) {
	    		model.addAttribute("message", "<b>Well done!</b> You successfully allowed a comment.");
	    	}
	    }
		return "admin/comments";
	}
	
	@GetMapping("/profile")
	public String profile(Model model) {
		User user = userRepository.findByEmail(getCurrentEmailUser());
		//System.out.println("user:::::::"+user);
		model.addAttribute("user", user);
	    model.addAttribute("active", "Profile");
		return "admin/profile";
	}

	@PostMapping("/profile")
	public String saveProfile(@RequestParam("file") MultipartFile file, @ModelAttribute("user") User user ,Model model) {
		
		User oldUser = userRepository.findByEmail(getCurrentEmailUser());
		if (!file.isEmpty()) {
			  File uploadRootDir = new File(UPLOAD_DIR);
		      // Create directory if it not exists.
		      if (!uploadRootDir.exists()) {
		         uploadRootDir.mkdirs();
		      }
			 // normalize the file path
	      
	        	  // Client File Name
		         String name = file.getOriginalFilename();
		        // System.out.println("Client File Name = " + name);
		         String extention = "";

		         int i = name.lastIndexOf('.');
		         if (i > 0) {
		        	 extention = name.substring(i+1);
		         }
	          //  System.out.println("===="+extention);
		         if (name != null && name.length() > 0) {
			            try {
			               // Create the file at server
			            	
			               File serverFile = new File(uploadRootDir.getAbsolutePath() + File.separator + oldUser.getId() + "." + extention);
			 
			               BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
			               stream.write(file.getBytes());
			               stream.close();
			               //
			               String avatar = "/" + UPLOAD_DIR + "/" + oldUser.getId() + "." + extention;
			               user.setAvatar(avatar); 
			               userDetailsServiceImpl.getUserDetails().setAvatar(avatar);
	
			              // System.out.println("Write file: " + serverFile);
			            } catch (Exception e) {
			               System.out.println("Error Write file: " + name);
			            }
	        	
                 }
		         
		}
		updateUser(user, oldUser);
		model.addAttribute("msg", "Your profile is updated");
		return "admin/profile";
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
	
	
	
	private void updateUser(User user, User oldUser) {
		user.setId(oldUser.getId());
		user.setEmail(oldUser.getEmail());
		user.setPassword(oldUser.getPassword());
		user.setName(oldUser.getName());
		user.setCreatedAt(new Date());
		user.setUpdatedAt(new Date());
		userRepository.save(user);
	}
	
}
