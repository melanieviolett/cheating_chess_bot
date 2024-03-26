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
            if (response.ok)
            {
                const result = await response.text();
                return result;
            }
            else
            {
                throw new Error('Request failed');
            }
        } catch (error) {
          console.error('Error:', error);
        }
    }
};

export default requestAIMove;
