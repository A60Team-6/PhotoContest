package com.telerikacademy.web.photocontest.controllers.mvc;


import com.telerikacademy.web.photocontest.entities.User;
import com.telerikacademy.web.photocontest.entities.dtos.UserOutput;
import com.telerikacademy.web.photocontest.entities.dtos.UserUpdate;
import com.telerikacademy.web.photocontest.exceptions.AuthenticationFailureException;
import com.telerikacademy.web.photocontest.exceptions.AuthorizationException;
import com.telerikacademy.web.photocontest.exceptions.DuplicateEntityException;
import com.telerikacademy.web.photocontest.helpers.AuthenticationHelper;
import com.telerikacademy.web.photocontest.services.contracts.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserMvcController {

    private final ConversionService conversionService;
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;

    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request) {
        return request.getRequestURI();
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @GetMapping("/{id}")
    public String showSingleUser(@PathVariable UUID id, Model model, HttpSession session) {
        try {
            User authUser = authenticationHelper.tryGetUser(session);
            UserOutput user = userService.findUserById(id, authUser);
            model.addAttribute("user", user);

            return "UserView";
        }catch (EntityNotFoundException e){
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @GetMapping("/{id}/update")
    public String showEditUserPage(@PathVariable UUID id, Model model, HttpSession session) {
        User user1;
        try {
            user1 = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/login";
        }

        try {
            UserUpdate userUpdate= conversionService.convert(user1, UserUpdate.class);
            model.addAttribute("user", userUpdate);
            model.addAttribute("id", id);
            return "UserUpdateView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @PostMapping("/{id}/update")
    public String updateUser(@PathVariable UUID id, @Valid @ModelAttribute("userDto") UserUpdate userDto,
                             BindingResult bindingResult, Model model, HttpSession session
    ) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/Login";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("currentUser", user);
            model.addAttribute("user", userService.findUserById(id, user));
            return "UserUpdateView";
        }

        try {
            model.addAttribute("user", userService.findUserById(id, user));
            model.addAttribute("id", id);
            model.addAttribute("userToUpdate", userDto);


            userService.editUser(user, userDto);
            return "redirect:/user/Me";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (DuplicateEntityException e) {
            bindingResult.rejectValue("name", "duplicate_post", e.getMessage());
            return "UserUpdateView";
        } catch (AuthorizationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable UUID id, Model model, HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        try {
            String redirect;
            if(user.getUsername().equals(userService.findUserById(id, user).getUsername())){
                redirect = "redirect:/auth/logout";
            }else {
                redirect = "redirect:/admin/users";
            }
            userService.deactivateUser(id, user);
            return redirect;
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (AuthorizationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        }
    }

//    @PostMapping("/{id}/juryInvitation")
//    public String userToModerator(@PathVariable UUID id, Model model, HttpSession session){
//
//        User user;
//        try {
//            user = authenticationHelper.tryGetUser(session);
//        } catch (AuthenticationFailureException e) {
//            return "redirect:/auth/login";
//        }
//
//        try {
//            User userToInvite = userService.findUserEntityById(id);
//            model.addAttribute("currentUser", user);
//            model.addAttribute("user", userToInvite);
//            model.addAttribute("id", id);
//            userService.inviteUserToBeJury(user, userToInvite);
//            return "redirect:/admin/users";
//        }catch (EntityNotFoundException e) {
//            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
//            model.addAttribute("error", e.getMessage());
//            return "ErrorView";
//        } catch (AuthorizationException e) {
//            model.addAttribute("error", e.getMessage());
//            return "AccessDeniedView";
//        }
//    }

    @GetMapping("/me")
    public String showCurrentUser( Model model, HttpSession session) {
        try {
            User me = authenticationHelper.tryGetUser(session);
            UserOutput user = userService.findUserById(me.getUserId(), me);
            model.addAttribute("user", user);
            model.addAttribute("id", me.getUserId());
            return "MeView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (AuthenticationFailureException e){
            return "AccessDeniedView";
        }
    }


}


