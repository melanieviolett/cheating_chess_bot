import React from "react";
import { Chessboard } from "react-chessboard";
const ChessboardUI = () => {
  return (
    <>
      <div className="w-10/12 mx-auto justify-center items-center flex">
        <div className="w-1/2 items-center flex">
          <Chessboard />
        </div>
      </div>
    </>
  );
};

export default ChessboardUI;
