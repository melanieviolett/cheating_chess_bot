package com.example.chessgame.controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chess")
@CrossOrigin(origins = "http://localhost:3000", methods = { RequestMethod.GET, RequestMethod.POST })
public class ChessController {
    private int count = 0;
    // 8 x 8 matrix to keep track of current state (maybe a list of 8x8 matrix for possible future states as well)
    // do computing methods in here, takes a current game state, returns a move
    // accomplish this through multi-threading

    @GetMapping("/counter")
    public int getCount()
    {
        return count;
    }

    @PostMapping("/increment")
    public void incrementCount()
    {
        count++;
    }
}
