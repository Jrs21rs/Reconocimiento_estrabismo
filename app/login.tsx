import { LinearGradient } from 'expo-linear-gradient';
import { Link, router } from "expo-router";
import { useState } from "react";
import { Alert, StyleSheet, Text, TextInput, TouchableOpacity, View } from "react-native";
import { useAuth } from "../services/authContext";
import { loginUser } from "../services/authService";

export default function LoginScreen() {
  const [correo, setCorreo] = useState("");
  const [password, setPassword] = useState("");
  const { login } = useAuth();

  const handleLogin = async () => {
    try {
      if (!correo || !password) {
        Alert.alert("Error", "Por favor ingrese correo y contraseña");
        return;
      }

      const response = await loginUser(correo, password);
      console.log("Respuesta del servidor:", response);

      if (response.error === "Bad credentials") {
        Alert.alert(
          "Error de credenciales",
          "El correo o la contraseña son incorrectos. Por favor verifica tus datos."
        );
        return;
      }

      if (response.error) {
        Alert.alert("Error", response.error || "Error en el servidor. Por favor intente nuevamente.");
        return;
      }

      if (response.token) {
        await login(response.token);
        Alert.alert("Login exitoso", "Has iniciado sesión correctamente");
        router.replace("/(tabs)/home");
      } else {
        Alert.alert("Error", "Respuesta del servidor inválida");
      }
    } catch (error) {
      console.error("Error en login:", error);
      Alert.alert("Error", "Ocurrió un error durante el login. Por favor intente nuevamente.");
    }
  };

  return (
    <LinearGradient
      colors={['#4c669f', '#3b5998', '#192f6a']}
      style={styles.container}
    >
      <View style={styles.content}>
        <Text style={styles.title}>Iniciar Sesión</Text>
        
        <TextInput
          style={styles.input}
          placeholder="Correo"
          placeholderTextColor="#666"
          value={correo}
          onChangeText={setCorreo}
          keyboardType="email-address"
          autoCapitalize="none"
        />
        
        <TextInput
          style={styles.input}
          placeholder="Contraseña"
          placeholderTextColor="#666"
          secureTextEntry
          value={password}
          onChangeText={setPassword}
          autoCapitalize="none"
        />
        
        <TouchableOpacity style={styles.button} onPress={handleLogin}>
          <Text style={styles.buttonText}>Ingresar</Text>
        </TouchableOpacity>
        
        <Link href="/register" asChild>
          <TouchableOpacity style={styles.linkButton}>
            <Text style={styles.linkText}>¿No tienes cuenta? Regístrate</Text>
          </TouchableOpacity>
        </Link>
      </View>
    </LinearGradient>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  content: {
    flex: 1,
    padding: 20,
    justifyContent: "center",
  },
  title: {
    fontSize: 32,
    fontWeight: "bold",
    marginBottom: 30,
    textAlign: "center",
    color: "#ffffff",
    textShadowColor: 'rgba(0, 0, 0, 0.3)',
    textShadowOffset: { width: 1, height: 1 },
    textShadowRadius: 3,
  },
  input: {
    backgroundColor: "rgba(255, 255, 255, 0.9)",
    padding: 15,
    marginBottom: 15,
    borderRadius: 10,
    fontSize: 16,
  },
  button: {
    backgroundColor: "#ffffff",
    padding: 15,
    borderRadius: 10,
    marginVertical: 20,
    elevation: 3,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.25,
    shadowRadius: 3.84,
  },
  buttonText: {
    color: "#4c669f",
    textAlign: "center",
    fontSize: 18,
    fontWeight: "bold",
  },
  linkButton: {
    marginTop: 10,
  },
  linkText: {
    color: "#ffffff",
    textAlign: "center",
    fontSize: 16,
  },
});