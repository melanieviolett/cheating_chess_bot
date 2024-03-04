import { useState, useEffect } from "react";
import { Chessboard } from "react-chessboard";
import { Chess } from "chess.js";

function Game() {
  const [game, setGame] = useState(new Chess());
  const [fen, setFen] = useState("start");

  useEffect(() => {
    setFen(game.fen());
  }, [game]);

  function onDrop(sourceSquare, targetSquare) {
    try {
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
