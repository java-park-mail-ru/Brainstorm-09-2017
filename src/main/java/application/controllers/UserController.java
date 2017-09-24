package application.controllers;

import application.models.User;
import application.services.UserService;
import application.views.ErrorResponse;
import application.views.SuccessResponse;
import application.views.UserData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

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
        final String error = userService.create(user);
        if (!error.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorResponse(error));
        }
        return ResponseEntity.ok(new SuccessResponse("Successfully registered user"));
    }


    @PostMapping(path = "/signin", consumes = "application/json", produces = "application/json")
    public ResponseEntity signin(@RequestBody User credentials, HttpSession httpSession) {
        final User user = userService.getUserByLogin(credentials.getLogin());
        if (user == null || !user.getPassword().equals(credentials.getPassword())) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Authorization failed."));
        }
        httpSession.setAttribute("userId", user.getId());
        return ResponseEntity.ok(new SuccessResponse("Successfully signin"));
    }


    @GetMapping(path = "/me", produces = "application/json")
    public ResponseEntity currentUser(HttpSession httpSession) {
        final Long userId = (Long) httpSession.getAttribute("userId");
        final User user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("User not authorized"));
        }
        return ResponseEntity.ok(new UserData(user));
    }


    @PatchMapping(path = "/me", consumes = "application/json", produces = "application/json")
    public ResponseEntity editUser(@RequestBody User body, HttpSession httpSession) {
        final Long userId = (Long) httpSession.getAttribute("userId");
        final User user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("User not authorized"));
        }

        final String errors = userService.update(body);
        if (errors.isEmpty()) {
            return ResponseEntity.ok(new SuccessResponse("Edit complite."));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(errors));
        }
    }


    @GetMapping(path = "/logout", produces = "application/json")
    public ResponseEntity logout(HttpSession httpSession) {
        httpSession.setAttribute("userId", null);
        return ResponseEntity.ok(new SuccessResponse("Successfully logout"));
    }
}
