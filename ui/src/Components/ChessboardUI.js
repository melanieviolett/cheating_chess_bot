import React, { useState, useEffect } from 'react';
import { Chessboard } from 'react-chessboard';
import { Chess } from 'chess.js';

const ChessboardUI = () => {
  const [game, setGame] = useState(new Chess());

  const onDrop = ({ sourceSquare, targetSquare, piece }) => {
    let moveObj = {
      from: sourceSquare,
      to: targetSquare,
    };
  
    if ((piece === 'wP' && targetSquare[1] === '8') || (piece === 'bP' && targetSquare[1] === '1')) {
      moveObj.promotion = 'q';
    }
  
    console.log("Attempting move:", moveObj);
  
    const move = game.move(moveObj);
  
    if (move === null) {
      console.error("Invalid move:", moveObj);
      return false;
    }
  
    setGame({ ...game });
    return true;
  };  

  useEffect(() => {
    if (game instanceof Chess && typeof game.game_over === 'function') {
      if (game.game_over()) {
        alert('Game over');
        setGame(new Chess());
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
