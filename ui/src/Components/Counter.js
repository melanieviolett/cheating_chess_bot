import React, { useState, useEffect } from 'react';

const Counter = () => {
  const [count, setCount] = useState(0);

  // We can find these "links" in the controller module in the backend folder, "ChessController.java"
  // This shows you how we create these api links to fetch what we need.
  const getCount = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/chess/counter');
      const result = await response.json();
      setCount(result);
    } catch (error) {
      console.error('Error:', error);
    }
  };

  const incrementCount = async () => {
    try {
        await fetch('http://localhost:8080/api/chess/increment', { 
            method: 'POST',
        });
        getCount();
    } catch (error) {
        console.error('Error:', error);
    }
  }

  useEffect(() => {
    getCount(); // Fetch initial count when component mounts
  }, []); // Empty dependency array ensures it runs only once

  return (
    <div>
      <p>Count: {count}</p>
      <button onClick={incrementCount}>Increment</button>
    </div>
  );
};

export default Counter;
