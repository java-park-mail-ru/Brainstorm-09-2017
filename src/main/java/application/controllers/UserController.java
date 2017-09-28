package application.controllers;

import application.models.User;
import application.services.UserService;
import application.views.ErrorResponse;
import application.views.ErrorResponse.ErrorCode;
import application.views.ErrorResponseList;
import application.views.SuccessResponse;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@RestController
@CrossOrigin(origins = "https://bubblerise-front.herokuapp.com, https://bubblerise.herokuapp.com")
@RequestMapping(path = "/api/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity signup(@RequestBody User user) {
        final ErrorResponseList errors = userService.create(user);
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }
        return ResponseEntity.ok(new SuccessResponse("Successfully registered user"));
    }


    @PostMapping(path = "/signin", consumes = "application/json", produces = "application/json")
    public ResponseEntity signin(@RequestBody User credentials, HttpSession httpSession) {
        final User user = userService.getUserByLogin(credentials.getLogin());
        if (user == null || !user.getPassword().equals(credentials.getPassword())) {
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
        if (auth(httpSession) == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(ErrorCode.UNAUTHORIZED).toList());
        }

        final ErrorResponseList errors = userService.update(body);
        if (errors.isEmpty()) {
            return ResponseEntity.ok(new SuccessResponse("Edit complite."));
        } else {
            return ResponseEntity.badRequest().body(errors);
        }
    }


    @GetMapping(path = "/logout", produces = "application/json")
    public ResponseEntity logout(HttpSession httpSession) {
        httpSession.setAttribute("userId", null);
        return ResponseEntity.ok(new SuccessResponse("Successfully logout"));
    }


    public @Nullable User auth(HttpSession httpSession) {
        final Long userId = (Long) httpSession.getAttribute("userId");
        return userService.getUserById(userId);
    }
}
