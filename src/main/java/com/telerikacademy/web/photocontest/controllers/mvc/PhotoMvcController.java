package com.telerikacademy.web.photocontest.controllers.mvc;

import com.telerikacademy.web.photocontest.entities.Photo;
import com.telerikacademy.web.photocontest.entities.dtos.ContestOutput;
import com.telerikacademy.web.photocontest.entities.dtos.PhotoOutput;
import com.telerikacademy.web.photocontest.exceptions.AuthenticationFailureException;
import com.telerikacademy.web.photocontest.exceptions.EntityNotFoundException;
import com.telerikacademy.web.photocontest.services.contracts.PhotoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
@RequestMapping("/photo")
public class PhotoMvcController {



    private final PhotoService photoService;


    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request) {
        return request.getRequestURI();
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @GetMapping("/{id}")
    public String showSinglePhoto(@PathVariable UUID id, Model model) {
        try {
            PhotoOutput photoOutput = photoService.getPhotoById(id);
            Photo photo = photoService.findPhotoEntityById(id);
            model.addAttribute("photo", photo);
            return "PhotoView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (AuthenticationFailureException e) {
            return "AccessDeniedView";
        }
    }


    //Създване на снимка с нейното ъплоадване
}
