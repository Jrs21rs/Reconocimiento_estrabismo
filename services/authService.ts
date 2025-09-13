const API_URL = 'http://192.168.0.17:8080/auth';

interface LoginResponse {
  token?: string;
  error?: string;
}

export const loginUser = async (correo: string, password: string): Promise<LoginResponse> => {
  try {
    const response = await fetch(`${API_URL}/login`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ correo, password }),
    });
    
    const data = await response.json();
    return data;
  } catch (error) {
    return { error: 'Error de conexión' };
  }
};
