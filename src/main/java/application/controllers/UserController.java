package application.controllers;

import application.models.User;
import application.services.UserService;
import application.views.ErrorResponse;
import application.views.SuccessResponse;
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
        if (error != null) {
            return ResponseEntity.badRequest().body(new ErrorResponse(error));
        }
        return ResponseEntity.ok(new SuccessResponse("Successfully registered user"));
    }

    @RequestMapping(path = "/my", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity currentUser(HttpSession httpSession) {
        Integer userId = (Integer) httpSession.getAttribute("userId");
        User user = UserService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("User not authorized"));
        }
        return ResponseEntity.ok(user);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity editUser(@RequestBody User body, HttpSession httpSession) {
        Integer userId = (Integer) httpSession.getAttribute("userId");
        User user = UserService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("User not authorized"));
        }


        return ResponseEntity.ok(user);

    }
}
