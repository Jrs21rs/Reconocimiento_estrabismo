interface RegisterData {
  nombres: string;
  apellidos: string;
  edad: number;
  correo: string;
  password: string;
  numeroTele: string;
  
}

interface RegisterResponse {
  success?: boolean;
  error?: string;
}

const API_URL = 'http://192.168.0.17:8080/auth';

export const registerUser = async (userData: RegisterData): Promise<RegisterResponse> => {
  try {
    const response = await fetch(`${API_URL}/register`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(userData),
    });
    
    const data = await response.json();
    return data;
  } catch (error) {
    return { success: false, error: 'Error de conexión' };
  }
};
