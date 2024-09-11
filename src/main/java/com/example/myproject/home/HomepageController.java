package com.example.myproject.home;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.myproject.Clogger;
import com.example.myproject.validation.ValidationError;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class HomepageController {

	@Autowired
	private UserRepository userRepository;

	@PostMapping("/register")
	public String doRegistration(@RequestParam("username") String username, @RequestParam("fullname") String fullname,
			@RequestParam("email") String email, @RequestParam("password") String password, Model model) {
		Clogger.info("Or here");

		ValidationError validationError = new ValidationError();

		User existingUserByEmail = userRepository.findByEmail(email);
		if (existingUserByEmail != null) {
			validationError.setEmail("Sorry, email is already taken");
		}

		User existingUserByUsername = userRepository.findByUsername(username);
		if (existingUserByUsername != null) {
			validationError.setUsername("Sorry, username is already taken");
		}

		if (validationError.hasErrors()) {
			model.addAttribute("error", validationError);
			return "register";
		}

		// insert a new user to database
		User user = new User();
		user.setUsername(username);
		user.setFullname(fullname);
		user.setPassword(password);
		user.setEmail(email);
		user.setType(UserType.USER);
		userRepository.save(user);

		return "redirect:/"; // upon successful registration

	}

	@GetMapping("/")
	public String getHomepage() {
		return "index";
	}

	@GetMapping("/register")
	public String getRegisterPage() {
		return "register";
	}

	@GetMapping("/login")
	public String getLoginPage() {
		return "login";
	}

	@PostMapping("/login")
	public String doLogin(@RequestParam("username") String username, @RequestParam("password") String password,
			Model model, HttpServletResponse response) {
		Clogger.info("username = " + username);
		Clogger.info("password = " + password);

		// SELECT * FROM _user WHERE username = 'nisil' AND password = 'Nepal@123'
		User user = userRepository.findByUsernameAndPassword(username, password);

		if (user == null) {
			ValidationError error = new ValidationError();
			error.setLogin("Invalid credentials!");
			model.addAttribute("error", error);
			return "login";
		}

		// A user exists with given (username, password) in database
		String session = UserIdGenerator.generateId();
		user.setSession(session);
		userRepository.save(user);

//		response.setHeader("Set-Cookie", "id=" + session);
		Cookie cookie = new Cookie("session", session);
		cookie.setPath("/");

		response.addCookie(cookie);

		return "redirect:/dashboard";
	}

}
