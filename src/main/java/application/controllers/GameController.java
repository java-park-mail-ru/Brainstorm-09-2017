package application.controllers;

import application.servicies.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin(origins = {"https://bubblerise-front.herokuapp.com", "https://bubblerise.herokuapp.com", "https://brainst0rm.herokuapp.com/"})
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
}
