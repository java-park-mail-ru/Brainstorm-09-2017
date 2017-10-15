package application.controllers;

import application.models.User;
import application.servicies.UsersService;
import application.views.ErrorResponse;
import application.views.ErrorResponse.ErrorCode;
import application.views.SuccessResponse;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;


@RestController
@CrossOrigin(origins = {"https://bubblerise-front.herokuapp.com", "https://bubblerise.herokuapp.com", "https://brainst0rm.herokuapp.com/"})
@RequestMapping(path = "/api/users")
public class UsersController {
    private UsersService usersService;


    @Autowired
    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }


    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity signup(@RequestBody User user) {
        final List<ErrorResponse> errors = usersService.create(user);
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }
        return ResponseEntity.ok(new SuccessResponse("Successfully registered user"));
    }


    @PostMapping(path = "/signin", consumes = "application/json", produces = "application/json")
    public ResponseEntity signin(@RequestBody User credentials, HttpSession httpSession) {
        final User user = usersService.auth(credentials);
        if (user == null) {
            return ResponseEntity.badRequest().body(new ErrorResponse(ErrorCode.AUTHORISATION_FAILED).toList());
        }
        httpSession.setAttribute("userId", user.getId());
        return ResponseEntity.ok(new SuccessResponse("Successfully signin"));
    }


    @GetMapping(path = "/me", produces = "application/json")
    public ResponseEntity currentUser(HttpSession httpSession) {
        final User user = auth(httpSession);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(ErrorCode.UNAUTHORIZED).toList());
        }
        return ResponseEntity.ok(user);
    }


    @PatchMapping(path = "/me", consumes = "application/json", produces = "application/json")
    public ResponseEntity editUser(@RequestBody User body, HttpSession httpSession) {
        final User user = auth(httpSession);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(ErrorCode.UNAUTHORIZED).toList());
        }

        final List<ErrorResponse> errors = usersService.update(user.getId(), body);
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }
        return ResponseEntity.ok(new SuccessResponse("Edit complite."));
    }


    @PostMapping(path = "/logout", produces = "application/json")
    public ResponseEntity logout(HttpSession httpSession) {
        httpSession.setAttribute("userId", null);
        return ResponseEntity.ok(new SuccessResponse("Successfully logout"));
    }


    @GetMapping(path = "/records", produces = "application/json")
    public ResponseEntity records() {
        return ResponseEntity.ok(usersService.getRecords());
    }


    public @Nullable User auth(HttpSession httpSession) {
        final Long userId = (Long) httpSession.getAttribute("userId");
        return usersService.findUserById(userId);
    }
}
