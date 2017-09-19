package application.controllers;

import application.models.User;
import application.services.UserService;
import application.views.StatusView;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(path = "/user")
public class UserController {
    @RequestMapping(method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
    public ResponseEntity signup(@RequestBody User user) {
        final String error = user.create();
        if (error != null) {
            return ResponseEntity.badRequest().body(new StatusView(error));
        }
        return ResponseEntity.ok(new StatusView("OK"));
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity getUser(HttpSession httpSession) {
        Integer userId = (Integer) httpSession.getAttribute("userId");
        User user = UserService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new StatusView("User not authorized"));
        }
        return ResponseEntity.ok(user);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity editUser(@RequestBody User body, HttpSession httpSession) {
        Integer userId = (Integer) httpSession.getAttribute("userId");
        User user = UserService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new StatusView("User not authorized"));
        }


        return ResponseEntity.ok(user);

    }
}
