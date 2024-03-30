package com.example.chessgame.controller;
import java.util.ArrayList;
import java.util.List;

import com.github.bhlangonijr.chesslib.*;
// import com.github.bhlangonijr.chesslib.pgn.*;
// import com.github.bhlangonijr.chesslib.util.*;
import com.github.bhlangonijr.chesslib.move.*;
import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.Square;

public class ThreadedTreeWalker {

    private Board initialBoard;
    private int depth;
    private Move worstMove;
    private Side playerColor;
    private double alpha = Double.NEGATIVE_INFINITY;
    private double beta = Double.POSITIVE_INFINITY;
    private double lowestScore = Double.POSITIVE_INFINITY;

    public double[][] reverseArray(double[][] arr) {

        // create a new array with the same dimensions as the original
        double[][] reversedArray = new double[arr.length][];

        // reverse each sub-array individually and add to the new array ( Java :( )
        for (int i = 0; i < arr.length; i++) {
            double[] subArray = arr[i];
            double[] reversedSubArray = new double[subArray.length];
            for (int j = 0; j < subArray.length; j++) {
                reversedSubArray[j] = subArray[subArray.length - 1 - j];
            }
            reversedArray[i] = reversedSubArray;
        }

        return reversedArray;
    }

    private double[][] whitePawnEval = {
            { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 },
            { 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0 },
            { 1.0, 1.0, 2.0, 3.0, 3.0, 2.0, 1.0, 1.0 },
            { 0.5, 0.5, 1.0, 2.5, 2.5, 1.0, 0.5, 0.5 },
            { 0.0, 0.0, 0.0, 2.0, 2.0, 0.0, 0.0, 0.0 },
            { 0.5, -0.5, -1.0, 0.0, 0.0, -1.0, -0.5, 0.5 },
            { 0.5, 1.0, 1.0, -2.0, -2.0, 1.0, 1.0, 0.5 },
            { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 }
    };
    private double[][] blackPawnEval = reverseArray(whitePawnEval);

    private double[][] knightEval = {
            { -5.0, -4.0, -3.0, -3.0, -3.0, -3.0, -4.0, -5.0 },
            { -4.0, -2.0, 0.0, 0.0, 0.0, 0.0, -2.0, -4.0 },
            { -3.0, 0.0, 1.0, 1.5, 1.5, 1.0, 0.0, -3.0 },
            { -3.0, 0.5, 1.5, 2.0, 2.0, 1.5, 0.5, -3.0 },
            { -3.0, 0.0, 1.5, 2.0, 2.0, 1.5, 0.0, -3.0 },
            { -3.0, 0.5, 1.0, 1.5, 1.5, 1.0, 0.5, -3.0 },
            { -4.0, -2.0, 0.0, 0.5, 0.5, 0.0, -2.0, -4.0 },
            { -5.0, -4.0, -3.0, -3.0, -3.0, -3.0, -4.0, -5.0 }
    };

    private double[][] whiteBishopEval = {
            { -2.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -2.0 },
            { -1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -1.0 },
            { -1.0, 0.0, 0.5, 1.0, 1.0, 0.5, 0.0, -1.0 },
            { -1.0, 0.5, 0.5, 1.0, 1.0, 0.5, 0.5, -1.0 },
            { -1.0, 0.0, 1.0, 1.0, 1.0, 1.0, 0.0, -1.0 },
            { -1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, -1.0 },
            { -1.0, 0.5, 0.0, 0.0, 0.0, 0.0, 0.5, -1.0 },
            { -2.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -2.0 }
    };

    private double[][] blackBishopEval = reverseArray(whiteBishopEval);

    private double[][] whiteRookEval = {
            { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 },
            { 0.5, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.5 },
            { -0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5 },
            { -0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5 },
            { -0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5 },
            { -0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5 },
            { -0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5 },
            { 0.0, 0.0, 0.0, 0.5, 0.5, 0.0, 0.0, 0.0 }
    };

    private double[][] blackRookEval = reverseArray(whiteRookEval);

    private double[][] evalQueen = {
            { -2.0, -1.0, -1.0, -0.5, -0.5, -1.0, -1.0, -2.0 },
            { -1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -1.0 },
            { -1.0, 0.0, 0.5, 0.5, 0.5, 0.5, 0.0, -1.0 },
            { -0.5, 0.0, 0.5, 0.5, 0.5, 0.5, 0.0, -0.5 },
            { 0.0, 0.0, 0.5, 0.5, 0.5, 0.5, 0.0, -0.5 },
            { -1.0, 0.5, 0.5, 0.5, 0.5, 0.5, 0.0, -1.0 },
            { -1.0, 0.0, 0.5, 0.0, 0.0, 0.0, 0.0, -1.0 },
            { -2.0, -1.0, -1.0, -0.5, -0.5, -1.0, -1.0, -2.0 }
    };

    private double[][] whiteKingEval = {
            { -3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0 },
            { -3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0 },
            { -3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0 },
            { -3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0 },
            { -2.0, -3.0, -3.0, -4.0, -4.0, -3.0, -3.0, -2.0 },
            { -1.0, -2.0, -2.0, -2.0, -2.0, -2.0, -2.0, -1.0 },
            { 2.0, 2.0, 0.0, 0.0, 0.0, 0.0, 2.0, 2.0 },
            { 2.0, 3.0, 1.0, 0.0, 0.0, 1.0, 3.0, 2.0 }
    };

    private double[][] blackKingEval = reverseArray(whiteKingEval);

    public ThreadedTreeWalker(Board initialBoard, Side playerColor, int depth) {
        this.initialBoard = initialBoard;
        this.depth = depth;
        this.playerColor = playerColor;
    }

    public double getWorstScore()
    {
        return lowestScore;
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
            if (initialBoard.getPiece(move.getFrom()).getPieceSide() == playerColor) {
                // each thread will follow one legal move that can be made from the current
                // position.
                Thread thread = new Thread(() -> {

                    // Create a copy of the board for this thread, and make the specific move.
                    Board boardCopy = new Board();
                    boardCopy.loadFromFen(initialBoard.getFen());
                    boardCopy.doMove(move);

                    // Explore our subtrees of finding the worst move, get that score
                    double score = minimax(depth, alpha, beta, false, boardCopy);

                    // Update worst move from comparing the score of the most current move we have
                    // found.
                    synchronized (this) {
                        lowestScore = Math.min(lowestScore, score);
                        if (worstMove == null || score < lowestScore) {
                            worstMove = move;
                        }
                    }

                });
                threads.add(thread);
                thread.start();
            }
        }

        // Wait for all threads to finish
        long timeout = 5000;
        for (Thread thread : threads) {
            try {
                thread.join(timeout);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Lowest score among all threads: " + lowestScore);
    }

    // the evaluation function for minimax
    public double evaluateBoard(Board board) {

        double totalEvaluation = 0.0;

        for (Square square : Square.values()) {
            Piece piece = board.getPiece(square);

            // Get the x and y coordinates of the square
            int x = square.getFile().ordinal();
            int y = square.getRank().ordinal();

            double pieceValue = getPieceValue(piece, x, y);
            totalEvaluation += pieceValue;
        }

        return totalEvaluation;
    };


    public double getPieceValue(Piece piece, int x, int y) {
        if (piece == null) {
            return 0.0;
        }

        // get the absolute value of the piece based on its type and position
        double absoluteValue = getAbsoluteValue(piece, x, y);

        return piece.getPieceSide() == Side.WHITE ? absoluteValue : -absoluteValue;
    }

    public double getAbsoluteValue(Piece piece, int x, int y) {

        if (piece == null || piece.getPieceType() == null) {
            return 0.0;
        }

        switch (piece.getPieceType()) {
            case PAWN:
                return 10.0 + (piece.getPieceSide() == Side.WHITE ? whitePawnEval[y][x] : blackPawnEval[y][x]);
            case ROOK:
                return 50.0 + (piece.getPieceSide() == Side.WHITE ? whiteRookEval[y][x] : blackRookEval[y][x]);
            case KNIGHT:
                return 30.0 + knightEval[y][x];
            case BISHOP:
                return 30.0 + (piece.getPieceSide() == Side.WHITE ? whiteBishopEval[y][x] : blackBishopEval[y][x]);
            case QUEEN:
                return 90.0 + evalQueen[y][x];
            case KING:
                return 900.0 + (piece.getPieceSide() == Side.WHITE ? whiteKingEval[y][x] : blackKingEval[y][x]);
            default:
                return 0.0;
        }
    }

    // alpha beta pruning reduces the number of nodes evaluated by the minimax algo
    // maximising player -> the player whose goal is to maximize their score or evaluation function
    public double minimax(int depth, double alpha, double beta, boolean isMaximisingPlayer, Board board) {
        
        if (depth == 0) {
            return -evaluateBoard(board);
        }

        List<Move> possibleNextMoves = board.legalMoves();
        if (possibleNextMoves.isEmpty())
        {
            return -evaluateBoard(board);
        }
        double bestMove;

        if (isMaximisingPlayer) {
            bestMove = Double.NEGATIVE_INFINITY;
            for (Move move : possibleNextMoves) {          
                    board.doMove(move);
                    double someMove = minimax(depth - 1, alpha, beta, !isMaximisingPlayer, board);
                    bestMove = Math.max(bestMove, someMove);
                    board.undoMove();
                    alpha = Math.min(alpha, bestMove);
                    if (beta <= alpha) {
                        return bestMove;
                    }
            }
        } else {
            bestMove = Double.POSITIVE_INFINITY;
            for (Move move : possibleNextMoves) {
                    board.doMove(move);
                    double someMove = minimax(depth - 1, alpha, beta, !isMaximisingPlayer, board);
                    bestMove = Math.min(bestMove, someMove);
                    board.undoMove();
                    beta = Math.max(beta, bestMove);
                    if (beta <= alpha) {
                        return bestMove;
                    }
            }
        }
        return bestMove;
    }

    // err idk, i dont think this is needed if were just doing regular minimax and finding the lowest score of that
    // public double inverseMinimax(Board board, int depth) {
    // return minimax(depth, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY,
    //     true, board);
    // }


}
