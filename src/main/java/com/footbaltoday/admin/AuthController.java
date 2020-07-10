package com.footbaltoday.admin;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.footbaltoday.entity.User;
import com.footbaltoday.service.UserServiceImpl;


@Controller
@RequestMapping("/admin/auth")
public class AuthController {
	
	@Autowired
	private UserServiceImpl userServiceImpl; 

  
	@GetMapping("/login")
	public String login() {
		return "admin/auth/login";
	}
	
	@GetMapping("/signup")
	public String signup(Model model) {
		User user = new User();
		model.addAttribute("user", user);
		return "admin/auth/signup";
	}
	
	@PostMapping("/signup")
	public String saveSignup(@RequestBody String str, @ModelAttribute("user") @Valid User user, BindingResult result,Model model) {
		System.out.println("str:::::"+str);
		if(!CheckPassword(str, user.getPassword())) {
			result.rejectValue("password", "error.user", "Confrim password should be equals password.");
		}
		
		if (result.hasErrors()) {
			user.setPassword("");
			model.addAttribute("user", user);
	         return "admin/auth/signup";
	      }
		userServiceImpl.save(user);
		return "redirect:/admin/auth/login";
	}
	
	private boolean CheckPassword(String str, String password) {
		String confirm_password ="";
		String[] params = str.split("&");
		for (String param : params) {
			String[] sub = param.split("=");
			if(sub[0].equalsIgnoreCase("confirm_password") && sub.length == 2 ) {
				confirm_password = sub[1];
			}
		}
		return confirm_password.contentEquals(password);
	}
}
