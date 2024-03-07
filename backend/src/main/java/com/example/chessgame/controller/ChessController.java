package com.example.chessgame.controller;

import org.springframework.web.bind.annotation.*;
import com.github.bhlangonijr.chesslib.move.*;
import com.github.bhlangonijr.chesslib.*;

@RestController
@RequestMapping("/api/chess")
@CrossOrigin(origins = "http://localhost:3000", methods = { RequestMethod.GET, RequestMethod.POST })
public class ChessController {
    private int count = 0;

    // this was just for Counter.js example, we can remove later
    @GetMapping("/counter")
    public int getCount()
    {
        return count;
    }
    
    // this was just for Counter.js example, we can remove later
    @PostMapping("/increment")
    public void incrementCount()
    {
        count++;
    }

    @PostMapping("/move")
    public String getMove(@RequestBody FenRequest fenRequest) {
        String fenString = fenRequest.getFenString();
        String retString = fenString;
        int depth = 5; // we can change this to whatever depth we want. Higher -> closer to the worst move possible.


        // need to know what player they currently are --> we can pass in game.turn() which would be 'w' / 'b'.
        // ^^^ apparently there is a function that can determine which side the current turn is.
        // from that position, we need to know all legal moves AI can make based on who they are.

        // Creates a new chessboard in the standard initial position
        Board board = new Board();

        // Load a FEN position into the chessboard, get their side, and create our threaded walking system
        board.loadFromFen(fenString);
        Side playerColor = board.getSideToMove();
        System.out.println(playerColor);

        // ThreadedTreeWalker walker = new ThreadedTreeWalker(board, playerColor, depth);
        // walker.start();

        // Move worstMove = walker.getWorstMove();
        
        // Apply the worst move to the board
        // if (worstMove != null) {
        //     board.doMove(worstMove);
        //     retString = board.getFen();
        // }

        return retString;
    }
}

class FenRequest {
    private String fenString;

    public String getFenString()
    {
        return fenString;
    }

    public void setFenString(String fenString)
    {
        this.fenString = fenString;
    }
}
