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

const API_URL = process.env.NEXT_PUBLIC_API_AUTH_URL || '';


const fetchWithTimeout = async (url: string, options: RequestInit, timeout = 15000) => {  // Aumentado a 15 segundos
  const controller = new AbortController();
  const id = setTimeout(() => controller.abort(), timeout);
  
  try {
    console.log('Intentando conectar a:', url);
    console.log('Opciones de la petición:', JSON.stringify(options));

    // Verificar si la URL es accesible con más detalles
    console.log('Verificando accesibilidad del servidor...');
    const checkResponse = await fetch(url, { 
      method: 'HEAD',
      headers: {
        'Accept': '*/*',
        'Connection': 'keep-alive'
      }
    }).catch((error) => {
      console.error('Error en la verificación inicial:', error);
      return null;
    });

    if (!checkResponse) {
      console.error('El servidor no responde a la verificación inicial');
      throw new Error('No se puede acceder al servidor. Verifica la conexión y la dirección IP');
    }

    console.log('Servidor accesible, realizando petición principal...');
    const response = await fetch(url, {
      ...options,
      signal: controller.signal,
      headers: {
        ...options.headers,
        'Connection': 'keep-alive'
      }
    });
    clearTimeout(id);
    return response;
  } catch (error) {
    clearTimeout(id);
    if (error instanceof Error && error.name === 'AbortError') {
      throw new Error('La conexión tardó demasiado tiempo');
    }
    throw error;
  }
};

export const registerUser = async (userData: RegisterData): Promise<RegisterResponse> => {
  try {
    console.log('Iniciando registro...');
    console.log('URL del servidor:', API_URL);
    console.log('Enviando datos:', userData);

    const response = await fetchWithTimeout(`${API_URL}/register/paciente`, {
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(userData),
    });
    
    if (!response.ok) {
      const errorText = await response.text();
      console.error('Respuesta del servidor no válida:', response.status, errorText);
      return { 
        success: false, 
        error: `Error del servidor (${response.status}): ${errorText}` 
      };
    }

    const data = await response.json();
    console.log('Respuesta exitosa:', data);
    return data;
  } catch (error) {
    console.error('Error detallado:', error);
    return { 
      success: false, 
      error: error instanceof Error ? error.message : 'Error de conexión desconocido'
    };
  }
};
