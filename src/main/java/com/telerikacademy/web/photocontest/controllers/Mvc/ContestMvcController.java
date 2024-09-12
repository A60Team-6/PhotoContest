package com.telerikacademy.web.photocontest.controllers.mvc;

import com.telerikacademy.web.photocontest.entities.Contest;
import com.telerikacademy.web.photocontest.entities.ContestParticipation;
import com.telerikacademy.web.photocontest.entities.Photo;
import com.telerikacademy.web.photocontest.entities.User;
import com.telerikacademy.web.photocontest.entities.dtos.ContestInput;
import com.telerikacademy.web.photocontest.entities.dtos.ContestOutput;
import com.telerikacademy.web.photocontest.exceptions.*;
import com.telerikacademy.web.photocontest.helpers.AuthenticationHelper;
import com.telerikacademy.web.photocontest.services.contracts.ContestParticipationService;
import com.telerikacademy.web.photocontest.services.contracts.ContestService;
import com.telerikacademy.web.photocontest.services.contracts.PhotoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
@RequestMapping("/contest")
public class ContestMvcController {


    private final ContestService contestService;
    private final AuthenticationHelper authenticationHelper;
    private final ConversionService conversionService;
    private final ContestParticipationService contestParticipationService;
    private final PhotoService photoService;

    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request) {
        return request.getRequestURI();
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @GetMapping("/contests")
    public String getContests(Model model,
                           @RequestParam(value = "title", required = false) String title,
                           @RequestParam(value = "category", required = false) String category,
                           @RequestParam(value = "phase", required = false) String phase,
                           @RequestParam(value = "page", defaultValue = "0") int page,
                           @RequestParam(value = "size", defaultValue = "10") int size,
                           @RequestParam(value = "sortBy", defaultValue = "firstName") String sortBy,
                           @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection) {

        Page<Contest> contestsPage = contestService.getContestsWithFilters(title, category, phase, page, size, sortBy, sortDirection);

        model.addAttribute("contests", contestsPage.getContent());  // Потребителите от текущата страница
        model.addAttribute("totalPages", contestsPage.getTotalPages());  // Общо страници
        model.addAttribute("currentPage", page);  // Текуща страница
        model.addAttribute("sortBy", sortBy);  // Поле за сортиране
        model.addAttribute("sortDirection", sortDirection);  // Посока на сортиране

        return "UsersView";  // Връщаме името на View-то
    }

    @GetMapping("/phaseOne")
    public String getAllContestsInPhaseOne(Model model, HttpSession session){
        try{
            User user = authenticationHelper.tryGetUser(session);
            model.addAttribute("user", user);
            List<Contest> contestList = contestService.getAllActiveContestInPhase1();
            model.addAttribute("contests", contestList);
            return "ContestsFromPhase1View";
        }catch (AuthenticationFailureException e){
            return "redirect:/Login";
        }
    }

    @GetMapping("/phaseTwo")
    public String getAllContestsInPhaseTwo(Model model, HttpSession session){
        try{
            User user = authenticationHelper.tryGetUser(session);
            model.addAttribute("user", user);
            List<Contest> contestList = contestService.getAllActiveContestInPhase2();
            model.addAttribute("contests", contestList);
            return "ContestsFromPhase2View";
        }catch (AuthenticationFailureException e){
            return "redirect:/Login";
        }
    }

    @GetMapping("/{id}/phOne")
    public String showSingleContestPhaseOne(@PathVariable UUID id, Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            ContestOutput contest = contestService.findContestById(id);
            model.addAttribute("contest", contest);
            model.addAttribute("user", user);
            boolean alreadyParticipate = false;
            List<ContestParticipation> participation = contestParticipationService.getAll();
            for(ContestParticipation contestParticipation : participation){
                if(contestParticipation.getContest().getTitle().equals(contest.getTitle()) && contestParticipation.getUser().equals(user)){
                    alreadyParticipate = true;
                }
            }
            boolean alreadyUploaded = false;
            List<Photo> photosOfUser = photoService.getAllPhotosEntityOfUser(user);
            List<Photo> photosOfContest = photoService.getAllPhotosEntityOfContest(contestService.findContestEntityById(id));

            for(Photo photo : photosOfUser){
                for(Photo photo1 : photosOfContest){
                    if(photo.equals(photo1)){
                        alreadyUploaded = true;
                    }
                }
            }
            session.setAttribute("alreadyUploaded", alreadyUploaded);
            session.setAttribute("contestId", id);
            session.setAttribute("alreadyParticipate", alreadyParticipate);
            return "ContestViewPhase1";
        }catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @GetMapping("/{id}/phTwo")
    public String showSingleContestPhaseTwo(@PathVariable UUID id, Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            ContestOutput contest = contestService.findContestById(id);
            model.addAttribute("contest", contest);
            model.addAttribute("user", user);
            List<Photo> photos = photoService.getAllPhotosEntityOfContest(contestService.findContestEntityById(id));
            session.setAttribute("photos", photos);
            session.setAttribute("contestId", id);
            return "ContestViewPhase2";
        }catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @GetMapping("/new")
    public String showNewContestPage(Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/Login";
        }

        model.addAttribute("contest", new ContestInput());
        return "ContestCreateView";
    }


    @PostMapping("/new")
    public String createContest(@Valid @ModelAttribute("contest") ContestInput contestInput,
                             BindingResult bindingResult,
                             Model model,
                             HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/Login";
        }

        if (bindingResult.hasErrors()) {
            return "ContestCreateView";
        }

        try {
            contestService.createContest(contestInput, user);
            return "redirect:/";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (DuplicateEntityException e) {
            bindingResult.rejectValue("name", "duplicate_contest", e.getMessage());
            return "ContestCreateView";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteContest(@PathVariable UUID id, Model model, HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        try {
            contestService.deactivateContest(id, user);
            return "HomeView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (AuthorizationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        }
    }

    @PostMapping("/{id}/participate")
    public String participateInContest(@PathVariable UUID id, Model model, HttpSession session) {
        try {
            User authUser = authenticationHelper.tryGetUser(session);
            ContestOutput contestOutput = contestService.findContestById(id);
            model.addAttribute("contest", contestOutput);
            model.addAttribute("user", authUser);
            contestParticipationService.participateInContest(authUser, id);
            return "ContestViewPhase1";
        }catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }
    //    празен ред
}
