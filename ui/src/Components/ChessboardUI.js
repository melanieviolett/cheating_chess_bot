import React, { useState, useEffect } from 'react';
import { Chessboard } from 'react-chessboard';
import { Chess } from 'chess.js';

const ChessboardUI = () => {
  const [game, setGame] = useState(new Chess());
  const [userIsWhite, setUserIsWhite] = useState(true); // initialize the player's pieces

  const onDrop = ({ sourceSquare, targetSquare, piece }) => {
    if ((userIsWhite && piece && piece[0] === 'w') || (!userIsWhite && piece && piece[0] === 'b'))
    {
      let moveObj = {
        from: sourceSquare,
        to: targetSquare,
      };
    
      // promotion pawn logic.
      if ((piece === 'wP' && targetSquare[1] === '8') || (piece === 'bP' && targetSquare[1] === '1')) {
        moveObj.promotion = 'q';
      }
    
      console.log("Attempting move:", moveObj);
    
      const move = game.move(moveObj);
    
      if (move === null) {
        console.error("Invalid move:", moveObj);
        return false;
      }

      // After human player move, check if AI is in check
      if (game.in_check()) {
        if (userIsWhite) { setUserIsWhite(false); }
        else { setUserIsWhite(true); }
      }
    
      setGame({ ...game });
    }

    return true;
  };  

  const requestAIMove = () => {
    // do something
    console.log("do something");
  };

  useEffect(() => {
    if (game instanceof Chess && typeof game.game_over === 'function') {
      if (game.game_over()) {
        alert('Game over');
        setGame(new Chess());
        setUserIsWhite(true);
      }
      else
      {
        requestAIMove(); // ai makes move in regards to whatever piece they are.
      }
    } else {
      console.error('Game is not initialized correctly');
    }
  }, [game]);

  return (
    <>
      <div className="w-10/12 mx-auto justify-center items-center flex">
        <div className="w-1/2 items-center flex">
          <Chessboard position={game.fen()} onPieceDrop={onDrop} />
        </div>
      </div>
    </>
  );
};

export default ChessboardUI;
