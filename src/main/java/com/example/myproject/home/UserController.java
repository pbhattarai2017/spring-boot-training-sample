package com.example.myproject.home;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@GetMapping("/dashboard")
	public String getDetails(HttpServletRequest request, Model model) {

		Cookie[] cookies = request.getCookies();
		Cookie userCookie = null;
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("session")) {
					userCookie = cookie;
					break;
				}
			}
		}

		if (userCookie == null) {
			return "redirect:/login";
		}

		User user = userRepository.findBySession(userCookie.getValue());

		if (user == null) {
			return "redirect:/login";
		}

		// user is authenticated

		model.addAttribute("user", user);

		return "dashboard";
	}

	@GetMapping("/logout")
	public String doLogout(HttpServletRequest request, Model model) {

		Cookie[] cookies = request.getCookies();
		Cookie userCookie = null;
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("session")) {
					userCookie = cookie;
					break;
				}
			}
		}

		if (userCookie == null) {
			return "redirect:/login";
		}

		User user = userRepository.findBySession(userCookie.getValue());

		if (user == null) {
			return "redirect:/login";
		}

		// user is authenticated
		user.setSession(null);
		userRepository.save(user);

		return "redirect:/";
	}
}
