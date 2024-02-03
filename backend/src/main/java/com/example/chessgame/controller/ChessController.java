package com.example.chessgame.controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chess")
@CrossOrigin(origins = "http://localhost:3000", methods = { RequestMethod.GET, RequestMethod.POST })
public class ChessController {
    private int count = 0;

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
