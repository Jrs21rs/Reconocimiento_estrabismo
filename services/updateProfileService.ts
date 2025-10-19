import AsyncStorage from "@react-native-async-storage/async-storage"; // si necesitas el token



// Definimos el tipo de userData
export interface UserData {
    nombres: string;
    apellidos: string;
    edad: number;
    correo: string;
    numeroTele: string;
}
export const updateProfile = async (userData: UserData) => {
    try {
        const token = await AsyncStorage.getItem("userToken"); //  authContext
        const response = await fetch("http://reconocimiento-estrabismo-env.eba-qemqhkeh.us-east-2.elasticbeanstalk.com/api/Pacientes/Update", {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                ...(token && { Authorization: `Bearer ${token}` }),
            },
            body: JSON.stringify(userData),
        });

        const data = await response.json();

        if (!response.ok) {
            return { error: data.message || "Error al actualizar el perfil" };
        }

        return data; // Devuelve la respuesta del backend
    } catch (error) {
        console.error("Error en updateProfileService:", error);
        return { error: "Error al conectar con el servidor" };
    }
};
