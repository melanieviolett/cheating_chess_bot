import { useState, useEffect } from "react";
import { Chessboard } from "react-chessboard";
import { Chess } from "chess.js";
import '../index.css';
import requestAIMove from "../api/aiMoveHandler.js";
import CheatIndicator from "../Components/CheatIndicator.js";

function Game() {
  const [game, setGame] = useState(new Chess());
  const [fen, setFen] = useState("start");
  const [userIsWhitePiece, setUserIsWhitePiece] = useState(true);
  const [isAITurn, setIsAITurn] = useState(false);
  const [showCheatIndicator, setShowCheatIndicator] = useState(false);

  const handleRestart = () => {
    setIsAITurn(false);
    setGame(new Chess());
    setUserIsWhitePiece(true);
  }

  const showWinner = () =>
  {
    let loser = game.turn();
    let msg = 'Game Over!';

    console.log(loser, userIsWhitePiece);
    if ((loser === 'w' && userIsWhitePiece) || (loser === 'b' && !userIsWhitePiece))
    {
      msg += ' You lost.... how??';
    }
    else
    {
      msg += ' You win, good job beating the worst AI ever!';
    }
    alert(msg);
    handleRestart();
  }

  const handleSideSwitch = () =>
  {
    setIsAITurn(false);
    setUserIsWhitePiece(!userIsWhitePiece); // switch user's side
    setShowCheatIndicator(true);

    // show flashing indicator for 5s
    setTimeout(() => {
      setShowCheatIndicator(false);
    }, 5000);
  }

  useEffect(() => {
    if (game.isGameOver()) {
      showWinner();
      return;
    }
    else
    {
      if (isAITurn) { 
        // probably check for if game is in check first then switch teams, 
        // then if not have ai make their move. (ai's turn effectively gets skipped if they swap teams)
                  // game is in check when it is AI turns, switch teams (this would effectively make it the user's turn again)
        if (game.isCheck())
        {
          handleSideSwitch();
        }
        else
        {

          requestAIMove(fen).then((fenString) => {
            console.log(fenString);

            setGame(new Chess(fenString));
            setFen(fenString);
        
            // this will return the new fenString from the move made on java side
            // would need to set the game board with the new fen string
  
            setIsAITurn(false);
          }).catch((error) => {
            console.error('Error requesting AI move:', error);
            setIsAITurn(true);
          });
        }
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
    <div className="board flex flex-col justify-center items-center">
      <div className="flex flex-col justify-center items-center pt-20">
        <Chessboard 
          boardWidth={600}
          position={fen} 
          onPieceDrop={onDrop} 
          boardOrientation={userIsWhitePiece ? "white" : "black"}  
        />
      </div>
      {showCheatIndicator && <CheatIndicator show={showCheatIndicator} />}
      <div className="turn-indication flex flex-row mt-4">
        <p className="text-4xl font-bold text-center">
          {isAITurn ? "AI's turn" : "Your turn"}
        </p>
        <button className="ml-10 btn pl-4 pr-4 font-bold text-white bg-purple-600 border border-purple-700 rounded" onClick={handleRestart}> Restart </button>
      </div>
    </div>
  );
}

export default Game;
