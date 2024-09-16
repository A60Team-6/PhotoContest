package com.telerikacademy.web.photocontest.controllers.mvc;

import com.telerikacademy.web.photocontest.entities.User;
import com.telerikacademy.web.photocontest.entities.dtos.Login;
import com.telerikacademy.web.photocontest.entities.dtos.Register;
import com.telerikacademy.web.photocontest.exceptions.AuthenticationFailureException;
import com.telerikacademy.web.photocontest.exceptions.DuplicateEntityException;
import com.telerikacademy.web.photocontest.exceptions.EntityNotFoundException;
import com.telerikacademy.web.photocontest.helpers.AuthenticationHelper;
import com.telerikacademy.web.photocontest.services.contracts.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@RequiredArgsConstructor
@Controller
@RequestMapping("/auth")
public class AuthenticationController {


    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final ConversionService conversionService;

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("login", new Login());
        return "Login";
    }

    @PostMapping("/login")
    public String handleLogin(@Valid @ModelAttribute("login") Login loginDto,
                              BindingResult bindingResult, HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "Login";
        }

        try {
            User user = authenticationHelper.verifyAuthentication(loginDto.getUsername(), loginDto.getPassword());
            session.setAttribute("currentUser", loginDto.getUsername());
            session.setAttribute("isOrganizer", user.getRole().getName().equals("Organizer"));
            session.setAttribute("isJury", user.getRole().getName().equals("Jury"));
            session.setAttribute("isInvited", user.getIsInvited().equals(true));
            return "redirect:/";
        } catch (AuthenticationFailureException e) {
            bindingResult.rejectValue("username", "auth_error", e.getMessage());
            return "Login";
        } catch (EntityNotFoundException e) {
            bindingResult.rejectValue("username", "auth_error", e.getMessage());
            return "Login";
        }
    }

    @GetMapping("/logout")
    public String handleLogout(HttpSession session) {
        session.removeAttribute("currentUser");
        return "redirect:/";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("register", new Register());
        return "Register";
    }


    @PostMapping("/register")
    public String handleRegister(@Valid @ModelAttribute("register") Register registerDto, BindingResult bindingResult,
                                 Model model) {
        if (!registerDto.getPassword().equals(registerDto.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "password_error", "Password confirmation should match password");
        }

        if (userService.existsByEmail(registerDto.getEmail())) {
            bindingResult.rejectValue("email", "email_error", "Email is already in use");
        }

        if(userService.existsByUsername(registerDto.getUsername())){
            bindingResult.rejectValue("username", "username_error", "Username is already in use");
        }

        if (bindingResult.hasErrors()) {
            return "Register";
        }

        try {
            //User user = modelMapper.fromDto(registerDto);
            User user = conversionService.convert(registerDto, User.class);
            model.addAttribute("user", user);

            userService.createUser(registerDto);
            if (!registerDto.getProfilePhoto().isEmpty()) {
                assert user != null;
                userService.uploadPhoto(user.getUsername(), registerDto.getProfilePhoto());
            }
            return "redirect:/auth/login";
        } catch (DuplicateEntityException e) {
            bindingResult.rejectValue("username", "username_error", e.getMessage());
            return "Register";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
