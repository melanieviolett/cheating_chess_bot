import { useState, useEffect } from "react";
import { Chessboard } from "react-chessboard";
import { Chess } from "chess.js";
import requestAIMove from '../api/aiMoveHandler.js';

function Game() {
  const [game, setGame] = useState(new Chess());
  const [fen, setFen] = useState("start");
  const [userIsWhitePiece, setUserIsWhitePiece] = useState(true);
  const [isAITurn, setIsAITurn] = useState(false);

  useEffect(() => {
    if (game.isCheckmate() || game.isDraw() || game.isStalemate() || game.isThreefoldRepetition() || game.isInsufficientMaterial()) {
      alert('Game over');
      setGame(new Chess());
      setUserIsWhitePiece(true);
    }
    else
    {
      if (isAITurn) { 
        // probably check for if game is in check first then switch teams, 
        // then if not have ai make their move. (ai's turn effectively gets skipped if they swap teams)
        requestAIMove(fen).then((bool) => {
          console.log(bool);
          // this will return the new fenString from the move made on java side
          // would need to set the game board with the new fen string
          setIsAITurn(false);
        }).catch((error) => {
          console.error('Error requesting AI move:', error);
        });
      }

    }

    setFen(game.fen());
  }, [game]);

  function onDrop(sourceSquare, targetSquare) {
    try {
      const isWhitePieceTurn = game.turn() === "w";

      if (isAITurn || ((userIsWhitePiece && !isWhitePieceTurn) || (!userIsWhitePiece && isWhitePieceTurn)))
      {
        alert("It's not your turn to move");
        return false;
      }

      // make the user's move
      const move = game.move({
        from: sourceSquare,
        to: targetSquare,
        promotion: "q"
      });
  
      if (move === null) return false;
  
      const newFen = game.fen();
      setGame(new Chess(newFen));
      setFen(newFen);
  
      console.log("Current FEN:", newFen);

      // set up for AI to make their turn
      setIsAITurn(true);
      return true;
    } catch (error) {
      console.error("Error making move: ", error);
      return false;
    }
  }
  

  return (
    <div className="board">
      <Chessboard position={fen} onPieceDrop={onDrop} />
    </div>
  );
}

export default Game;
