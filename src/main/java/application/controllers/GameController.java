package application.controllers;

import application.models.User;
import application.servicies.UsersService;
import application.views.ErrorResponse;
import application.views.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;


@RestController
@CrossOrigin(origins = {"https://bubblerise-front.herokuapp.com", "https://bubblerise.herokuapp.com", "https://brainst0rm.herokuapp.com"})
@RequestMapping(path = "/api/game")
public class GameController {
    private UsersService usersService;


    @Autowired
    public GameController(UsersService usersService) {
        this.usersService = usersService;
    }


    @GetMapping(path = "/records", produces = "application/json")
    public ResponseEntity records() {
        return ResponseEntity.ok(usersService.getRecords());
    }


    @PostMapping(path = "/local_record", produces = "application/json")
    public ResponseEntity localRecord(HttpSession httpSession, @RequestBody User body) {
        final User user = usersService.auth(httpSession);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(ErrorResponse.ErrorCode.UNAUTHORIZED).toList());
        }

        usersService.localRecord(user.getId(), body);
        return ResponseEntity.ok(new SuccessResponse("Success"));
    }
}
