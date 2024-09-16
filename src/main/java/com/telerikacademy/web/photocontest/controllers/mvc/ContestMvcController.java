package com.telerikacademy.web.photocontest.controllers.mvc;

import com.telerikacademy.web.photocontest.entities.Contest;
import com.telerikacademy.web.photocontest.entities.ContestParticipation;
import com.telerikacademy.web.photocontest.entities.Photo;
import com.telerikacademy.web.photocontest.entities.User;
import com.telerikacademy.web.photocontest.entities.dtos.*;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @GetMapping("/finished")
    public String getAllFinishedContestsWithTheirWinnerPhotos(Model model, HttpSession session){
        try{
//            User user = authenticationHelper.tryGetUser(session);
//            model.addAttribute("user", user);
            List<FinishedContestAntItsWinner> finishedContestAntItsWinners = contestService.getAllUnActive();
            finishedContestAntItsWinners.stream().
                    filter(contest -> contest.getContestOutput().getPhase().getName().equals("Finished")).
                    collect(Collectors.toList());
            model.addAttribute("contestsWithWinners", finishedContestAntItsWinners);
            return "ContestsWithWinnersView";
        }catch (AuthenticationFailureException e) {
            return "redirect:/Login";
        }
    }

    @GetMapping("/phaseOne")
    public String getContestsPhaseOne(Model model,
                           @RequestParam(value = "title", required = false) String title,
                           @RequestParam(value = "category", required = false) String category,
                           @RequestParam(value = "page", defaultValue = "0") int page,
                           @RequestParam(value = "size", defaultValue = "3") int size,
                           @RequestParam(value = "sortBy", defaultValue = "title") String sortBy,
                           @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection) {

        Page<Contest> contestsPage = contestService.getAllActiveContestInPhase1(title, category, page, size, sortBy, sortDirection);

        model.addAttribute("contests", contestsPage.getContent());  // Потребителите от текущата страница
        model.addAttribute("totalPages", contestsPage.getTotalPages());  // Общо страници
        model.addAttribute("currentPage", page);  // Текуща страница
        model.addAttribute("size", size);
        model.addAttribute("sortBy", sortBy);  // Поле за сортиране
        model.addAttribute("sortDirection", sortDirection);  // Посока на сортиране
        model.addAttribute("title", title);
        model.addAttribute("category", category);

        return "ContestsFromPhase1View";  // Връщаме името на View-то
    }


    @GetMapping("/phaseTwo")
    public String getContestsPhaseTwo(Model model,
                                      @RequestParam(value = "title", required = false) String title,
                                      @RequestParam(value = "category", required = false) String category,
                                      @RequestParam(value = "page", defaultValue = "0") int page,
                                      @RequestParam(value = "size", defaultValue = "3") int size,
                                      @RequestParam(value = "sortBy", defaultValue = "title") String sortBy,
                                      @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection) {

        Page<Contest> contestsPage = contestService.getAllActiveContestInPhase2(title, category, page, size, sortBy, sortDirection);

        model.addAttribute("contests", contestsPage.getContent());  // Потребителите от текущата страница
        model.addAttribute("totalPages", contestsPage.getTotalPages());  // Общо страници
        model.addAttribute("currentPage", page);  // Текуща страница
        model.addAttribute("size", size);
        model.addAttribute("sortBy", sortBy);  // Поле за сортиране
        model.addAttribute("sortDirection", sortDirection);  // Посока на сортиране
        model.addAttribute("title", title);
        model.addAttribute("category", category);

        return "ContestsFromPhase2View";  // Връщаме името на View-то
    }


    @GetMapping("/phOne/{contestId}")
    public String showSingleContestPhaseOne(@PathVariable UUID contestId, Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            ContestOutput contest = contestService.findContestById(contestId);
            model.addAttribute("contest", contest);
            model.addAttribute("user", user);
            int alreadyParticipate = 1;
            List<ContestParticipation> participation = contestParticipationService.getAll();
            for (ContestParticipation contestParticipation : participation) {
                if (contestParticipation.getContest().getTitle().equals(contest.getTitle()) && contestParticipation.getUser().equals(user)) {
                    alreadyParticipate = 0;
                    break;
                }
            }
            int alreadyUploaded = 1;
            List<Photo> photosOfContest = photoService.getAllPhotosEntityOfContest(contestService.findContestEntityById(contestId));


            for (Photo photo1 : photosOfContest) {
                if (photo1.getUser().equals(user)) {
                    alreadyUploaded = 0;
                    break;
                }
            }

            model.addAttribute("contestId", contestId);
            model.addAttribute("alreadyUploaded", alreadyUploaded);
            model.addAttribute("alreadyParticipate", alreadyParticipate);
            return "ContestViewPhase1";
        } catch (EntityNotFoundException e) {
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
        } catch (EntityNotFoundException e) {
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
            return "redirect:/contest/phaseOne";
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
            return "redirect:/contest/phOne/" + id;
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @GetMapping("/{id}/upload/photo")
    public String createAndUploadPhoto(@PathVariable UUID id, Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetUser(session);
            model.addAttribute("photoInput", new PhotoInput());
            model.addAttribute("uploadFileInput", new UploadFileInput());
            model.addAttribute("combinedInput", new CombinedPhotoInput());
            model.addAttribute("contestId", id);
            return "CreatePhotoView";
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
    }


    @PostMapping("/{id}/upload/photo")
    public String createAndUploadPhoto(@PathVariable UUID id, @Valid @ModelAttribute("combinedInput") CombinedPhotoInput combinedInput, BindingResult bindingResult, Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/Login";
        }

        if (bindingResult.hasErrors()) {
            return "CreatePhotoView";
        }

        try {
            PhotoInput photoInput = combinedInput.getPhotoInput();
            photoInput.setContestId(id.toString());
            model.addAttribute("combinedInput", combinedInput);

            PhotoIdOutput photoIdOutput = photoService.createPhoto(photoInput, user);
            UploadFileInput uploadFileInput = new UploadFileInput(photoIdOutput.getId(), combinedInput.getFile());

            model.addAttribute("contestId", id);
            photoService.uploadPhoto(uploadFileInput);

            return "redirect:/contest/phaseOne";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (DuplicateEntityException e) {
            bindingResult.rejectValue("photoInput.title", "duplicate_post", e.getMessage());
            return "CreatePhotoView";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
