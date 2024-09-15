package com.telerikacademy.web.photocontest.controllers.mvc;

import com.telerikacademy.web.photocontest.entities.JuryPhotoRating;
import com.telerikacademy.web.photocontest.entities.Photo;
import com.telerikacademy.web.photocontest.entities.User;
import com.telerikacademy.web.photocontest.entities.dtos.ContestOutput;
import com.telerikacademy.web.photocontest.entities.dtos.JuryPhotoRatingInput;
import com.telerikacademy.web.photocontest.entities.dtos.JuryPhotoRatingOutput;
import com.telerikacademy.web.photocontest.entities.dtos.PhotoOutput;
import com.telerikacademy.web.photocontest.exceptions.*;
import com.telerikacademy.web.photocontest.helpers.AuthenticationHelper;
import com.telerikacademy.web.photocontest.services.contracts.JuryPhotoRatingService;
import com.telerikacademy.web.photocontest.services.contracts.PhotoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
@RequestMapping("/photo")
public class PhotoMvcController {



    private final PhotoService photoService;
    private final AuthenticationHelper authenticationHelper;
    private final JuryPhotoRatingService juryPhotoRatingService;


    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request) {
        return request.getRequestURI();
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @GetMapping("photos/of/user")
    public String showPhotosOfUser(Model model, HttpSession session){
        try{
            User user = authenticationHelper.tryGetUser(session);
            List<Photo> photosOfUser = photoService.getAllPhotosEntityOfUser(user);
            model.addAttribute("photosOfUser", photosOfUser);
            model.addAttribute("user", user);
            return "PhotosOfUserView";
        } catch (AuthenticationFailureException e) {
            return "AccessDeniedView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @GetMapping("/{id}/ratings")
    public String getJuryRatingsForPhoto(@PathVariable UUID id, Model model, HttpSession session){
        try {
            User user = authenticationHelper.tryGetUser(session);
            Photo photo = photoService.findPhotoEntityById(id);
            model.addAttribute("user", user);
            model.addAttribute("photo", photo);
            List<JuryPhotoRatingOutput> juryPhotoRatingOutputs = juryPhotoRatingService.getAllRatingsForPhoto(id);
            model.addAttribute("juryPhotoRatingOutputs", juryPhotoRatingOutputs);
            return "JuryPhotoRatingsView";
        } catch (AuthenticationFailureException e) {
            return "AccessDeniedView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }


    @GetMapping("/{id}")
    public String showSinglePhoto(@PathVariable UUID id, Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            PhotoOutput photoOutput = photoService.getPhotoById(id);
            List<JuryPhotoRating> juryPhotoRatingList = juryPhotoRatingService.getAllRatingsEntityForPhoto(id);
            boolean isRated = false;
            for(JuryPhotoRating juryPhotoRating : juryPhotoRatingList) {
                if(juryPhotoRating.getJury().equals(user) && juryPhotoRating.getPhoto().getId() == id){
                    isRated = true;
                }
            }
            model.addAttribute("isRated", isRated);

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


    @GetMapping("/{id}/set/score")
    public String giveScoreToPhoto(@PathVariable UUID id, Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
            model.addAttribute("user", user);
            JuryPhotoRatingInput juryPhotoRatingInput = new JuryPhotoRatingInput();
            juryPhotoRatingInput.setPhotoId(id);
            juryPhotoRatingInput.setUserId(user.getUserId());
            model.addAttribute("juryPhotoRating",juryPhotoRatingInput);
            model.addAttribute("photoId", id);
            model.addAttribute("userId", user.getUserId());
            return "NewRatingView";
        }catch (AuthorizationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        }catch (AuthenticationFailureException e) {
            return "AccessDeniedView";
        }
    }

    @PostMapping("/{id}/set/score")
    public String giveScoreToPhoto(@PathVariable UUID id,
                             JuryPhotoRatingInput juryPhotoRating,
                             BindingResult bindingResult,
                             Model model,
                             HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/Login";
        }
        if (juryPhotoRating.getCategoryMatch() != null && !juryPhotoRating.getCategoryMatch()) {
            juryPhotoRating.setScore(0);
        }

        if (bindingResult.hasErrors()) {
            return "NewRatingView";
        }

        try {
            JuryPhotoRatingInput juryPhotoRatingInput = new JuryPhotoRatingInput();
            juryPhotoRating.setUserId(user.getUserId());
            juryPhotoRating.setPhotoId(id);
            model.addAttribute("juryPhotoRating",juryPhotoRatingInput);
            juryPhotoRatingService.createRating(juryPhotoRating);
            model.addAttribute("photoId", id);
            return "redirect:/photo/" + id;
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (DuplicateEntityException e) {
            bindingResult.rejectValue("name", "duplicate_post", e.getMessage());
            return "NewRatingView";
        }
    }
}
