const API_URL = 'http://reconocimiento-estrabismo-env.eba-qemqhkeh.us-east-2.elasticbeanstalk.com/auth';

interface AuthResponse {
  success?: boolean;
  error?: string;
}

export const testServerConnection = async (): Promise<AuthResponse> => {
  try {
    const controller = new AbortController();
    const timeoutId = setTimeout(() => controller.abort(), 5000); // 5 second timeout
    
    console.log('Probando conexión al servidor:', API_URL);
    
    const response = await fetch(API_URL, {
      method: 'GET',
      headers: {
        'Accept': 'application/json',
      },
      signal: controller.signal
    });
    
    clearTimeout(timeoutId);
    
    if (response.ok) {
      console.log('Conexión exitosa al servidor');
      return {
        success: true
      };
    } else {
      console.error('El servidor respondió con estado:', response.status);
      return {
        success: false,
        error: `El servidor respondió con estado ${response.status}`
      };
    }
  } catch (error) {
    console.error('Error de conexión:', error);
    if (error instanceof Error) {
      if (error.name === 'AbortError') {
        return {
          success: false,
          error: 'La conexión al servidor excedió el tiempo de espera (5 segundos)'
        };
      }
      return {
        success: false,
        error: `Error de conexión: ${error.message}`
      };
    }
    return {
      success: false,
      error: 'Error de conexión desconocido'
    };
  }
};