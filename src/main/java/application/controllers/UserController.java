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
@CrossOrigin // for localhost usage
//@CrossOrigin(origins = "https://[...].herokuapp.com") //for remote usage
@RequestMapping(path = "/api/users")
public class UserController {
    @PutMapping(path = "/signup", consumes = "application/json", produces = "application/json")
    public ResponseEntity signup(@RequestBody User user) {
        final String error = user.create();
        if (!error.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorResponse(error));
        }
        return ResponseEntity.ok(new SuccessResponse("Successfully registered user"));
    }


    @PostMapping(path = "/signin", consumes = "application/json", produces = "application/json")
    public ResponseEntity signin(@RequestBody User credentials, HttpSession httpSession) {
        final User user = UserService.getUserByLogin(credentials.getLogin());
        if (user == null || !user.getPassword().equals(credentials.getPassword())) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Authorisation fail."));
        }
        httpSession.setAttribute("userID", user.getId());
        return ResponseEntity.ok(new SuccessResponse("Successfully signin"));
    }


    @GetMapping(path = "/me", produces = "application/json")
    public ResponseEntity currentUser(HttpSession httpSession) {
        final Integer userId = (Integer) httpSession.getAttribute("userId");
        final User user = UserService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("User not authorized"));
        }
        return ResponseEntity.ok(new UserData(user));
    }


    @PostMapping(path = "/edit", consumes = "application/json", produces = "application/json")
    public ResponseEntity editUser(@RequestBody User body, HttpSession httpSession) {
        Integer userId = (Integer) httpSession.getAttribute("userId");
        User user = UserService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("User not authorized"));
        }

        String errors = user.update(body);
        if (errors.isEmpty()) {
            return ResponseEntity.ok(new SuccessResponse("Edit complite."));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(errors));
        }
    }
}
