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

// look into implementing logic, probably want to split up logic in other js files and then just import them in here, keep the UI looking clean
// however our App is what loads this, but take a gander.

export default ChessboardUI;
