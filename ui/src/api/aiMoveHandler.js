const BASE_URL = 'http://localhost:8080/api/chess';

const requestAIMove = async (fenString) => {
    if (fenString)
    {
        try {
            const response = await fetch(BASE_URL + '/move', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ fenString: fenString })
            });
            const result = await response.json();
            if (result) { return true; }
        } catch (error) {
          console.error('Error:', error);
        }
    }
};

export default requestAIMove;
