package com.example.chessgame.controller;
import java.util.List;
import java.util.ArrayList;
import com.github.bhlangonijr.chesslib.*;
import com.github.bhlangonijr.chesslib.move.*;
// import com.github.bhlangonijr.chesslib.pgn.*;
// import com.github.bhlangonijr.chesslib.util.*;

public class ThreadedTreeWalker {

    private Board initialBoard;
    private int depth;
    private Move worstMove;
    private Side playerColor;

    public ThreadedTreeWalker(Board initialBoard, Side playerColor, int depth) {
        this.initialBoard = initialBoard;
        this.depth = depth;
        this.playerColor = playerColor;
    }

    public Move getWorstMove() {
        return worstMove;
    }

    public void start() {
        // Make a thread for each tree walking decide-inator
        List<Thread> threads = new ArrayList<>();
        List<Move> allPossibleMoves = initialBoard.legalMoves();

        // for each move, that is currently legal to be made
        for (Move move : allPossibleMoves) {
            
            // ensures move is what the AI can move.
            if (initialBoard.getPiece(move.getFrom()).getPieceSide() == playerColor)
            {
                // each thread will follow one legal move that can be made from the current position.
                Thread thread = new Thread(() -> {

                    // Create a copy of the board for this thread, and make the specific move.
                    Board boardCopy = new Board();
                    boardCopy.loadFromFen(initialBoard.getFen());
                    boardCopy.doMove(move);

                    // Explore our subtrees of finding the worst move, get that score
                    int score = inverseMinimax(boardCopy, depth);

                    // Update worst move from comparing the score of the most current move wehave found.
                    synchronized (this) {
                        if (worstMove == null || score < evaluateMove(worstMove)) {
                            worstMove = move;
                        }
                    }
                });
                threads.add(thread);
                thread.start();
            }
        }

        // Wait for all threads to finish
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private int inverseMinimax(Board board, int depth) {
        // if a regular Minimax algo returns the best move, the inverse should return the worse right?
        // Higher scores could be the worst move, or maybe lower scores?

        if (depth == 0)
        {
            // return some evaluation score.........
        }

        // Kind of repeat the main loop
        // -> for each move in LegalMoves
        // -> recursively call inverseMinimax with depth - 1

        return 0;
    }

    private int evaluateMove(Move move) {
        // Need to find a way to evaluate the score of the current move, possibly may not need to do this?
        // As the minimax algorithm can also just update the score it found when it updates the move.
        // or maybe this is connected to the minimax algo we make?
        return 0;
    }
}
