import './App.css';
import ChessboardUI from './Components/ChessboardUI';
import Counter from './Components/Counter';

function App() {
  return (
    <>
    <div className="App">
      <h1>Increment me :)</h1>
      <Counter />
    </div>
     <ChessboardUI/>
     </>
  );
}

export default App;
