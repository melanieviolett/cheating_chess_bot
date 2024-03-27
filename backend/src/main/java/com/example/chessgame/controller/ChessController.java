package com.example.chessgame.controller;

import org.springframework.web.bind.annotation.*;
import com.github.bhlangonijr.chesslib.move.*;
import com.github.bhlangonijr.chesslib.*;

@RestController
@RequestMapping("/api/chess")
@CrossOrigin(origins = "http://localhost:3000", methods = { RequestMethod.GET, RequestMethod.POST })
public class ChessController {
    int worstScoreSum = 0;
    int moveCounter = 0;

    @PostMapping("/move")
    public String getMove(@RequestBody FenRequest fenRequest) {
        String fenString = fenRequest.getFenString();
        String retString = fenString;
        int depth = 2; // we can change this to whatever depth we want. Higher -> closer to the worst move possible.

        // Creates a new chessboard in the standard initial position
        Board board = new Board();

        // Load a FEN position into the chessboard, get their side, and create our threaded walking system
        board.loadFromFen(fenString);
        Side playerColor = board.getSideToMove();

        ThreadedTreeWalker walker = new ThreadedTreeWalker(board, playerColor, depth);
        walker.start();

        Move worstMove = walker.getWorstMove();
        System.out.println(worstMove);

        // Apply the worst move to the board
        if (worstMove != null) {
            board.doMove(worstMove);
            System.out.println("Worst move: " + worstMove);
            System.out.println("Lowest score associated to move: " + walker.getWorstScore());
            worstScoreSum += walker.getWorstScore();
            moveCounter++;
            retString = board.getFen();
        }

        return retString;
    }

    @GetMapping("/worstScoreAverage")
    public double getWorstScore()
    {
        return worstScoreSum / moveCounter;
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
