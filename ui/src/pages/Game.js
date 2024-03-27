import { useState, useEffect } from "react";
import { Chessboard } from "react-chessboard";
import { Chess } from "chess.js";
import '../index.css';
import {requestAIMove, getWorstScoreAverage} from "../api/aiMoveHandler.js";
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

  const showWinner = async () =>
  {
    let loser = game.turn();
    let msg = 'Game Over!';

    if ((loser === 'w' && userIsWhitePiece) || (loser === 'b' && !userIsWhitePiece))
    {
      msg += ' You lost.... how??';
    }
    else
    {
      msg += ' You win, good job beating the worst AI ever!';
    }

    alert(msg);

    try {
      console.log("The AI had an average move score of: ", await getWorstScoreAverage());
    } catch (error)
    {
      console.error(error);
    }

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
      setTimeout(() => {
        // allow animation to finish.
      }, 5000);
      showWinner();
      return;
    }
    else
    {
      if (isAITurn) { 
        // if game is in check on Ai's turn... initiate cheat mode!
        if (game.isCheck())
        {
          handleSideSwitch();
        }
        else
        {

          requestAIMove(fen).then((fenString) => {

            // set the new fen string returned as the game board.
            setGame(new Chess(fenString));
            setFen(fenString);
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
          {isAITurn ? "AI's turn" : game.inCheck() ? "Your turn, currently in check..." : "Your turn"}
        </p>
        <button className="ml-10 btn pl-4 pr-4 font-bold text-white bg-purple-600 border border-purple-700 rounded" onClick={handleRestart}> Restart </button>
      </div>
    </div>
  );
}

export default Game;
