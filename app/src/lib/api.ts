const baseUrl: string = import.meta.env.VITE_API_BASE_URL;

const api = (path: string, init?: RequestInit): Promise<string> => {
    return new Promise((resolve, reject) => {
        fetch(`${baseUrl}/${path}`, {
            headers: {
                'Content-Type': 'application/json',
            },
            ...init,
        })
        .then((response) => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then((data) => resolve(data))
        .catch((error) => reject(error));
    });
};

export { api };