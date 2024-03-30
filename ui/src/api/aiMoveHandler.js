const BASE_URL = 'http://localhost:8080/api/chess';

export const requestAIMove = async (fenString) => {
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

export const getWorstScoreAverage = async () => {
    try {
        const response = await fetch(BASE_URL + '/worstScoreAverage');
        if (response.ok)
        {
            const worstScoreAverage = await response.json();
            return worstScoreAverage;
        }
        else 
        {
            console.error('Error fetching worst score average:', response.status);
            return null;
        }
    } catch (error) {
        console.error('Error:', error);
    }
};

export const getTimeAverage = async () => {
    try {
        const response = await fetch(BASE_URL + '/timeAverage');
        if (response.ok)
        {
            const timeAverage = await response.json();
            return timeAverage;
        }
        else 
        {
            console.error('Error fetching worst score average:', response.status);
            return null;
        }
    } catch (error) {
        console.error('Error:', error);
    }
};

export const resetCounts = async () => {
    try {
        const response = await fetch(BASE_URL + '/resetCounts');
        return null;
    } catch (error) {
        console.error('Error:', error);
    }
};


