import { Link, router } from "expo-router";
import { useState } from "react";
import { Alert, Button, StyleSheet, Text, TextInput, View } from "react-native";
import { useAuth } from "../../services/authContext";
import { loginUser } from "../../services/authService";

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
        // Aquí puedes manejar el token, por ejemplo guardarlo en AsyncStorage
        // Redirigir al usuario a la pantalla de inicio
        router.replace("/(tabs)");
        Alert.alert("Login exitoso", "Has iniciado sesión correctamente");
      } else {
        Alert.alert("Error", "Respuesta del servidor inválida");
      }
    } catch (error) {
      console.error("Error en login:", error);
      Alert.alert("Error", "Ocurrió un error durante el login. Por favor intente nuevamente.");
    }
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Login de Pacientes</Text>
      <TextInput
        style={styles.input}
        placeholder="Correo"
        value={correo}
        onChangeText={setCorreo}
      />
      <TextInput
        style={styles.input}
        placeholder="Contraseña"
        secureTextEntry
        value={password}
        onChangeText={setPassword}
      />
      <Button title="Ingresar" onPress={handleLogin} />
      <Link href="/register" style={styles.link}>
        ¿No tienes cuenta? Regístrate
      </Link>
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, justifyContent: "center", padding: 20 },
  title: {
    fontSize: 24,
    fontWeight: "bold",
    marginBottom: 20,
    textAlign: "center",
  },
  input: {
    borderWidth: 1,
    padding: 10,
    marginBottom: 10,
    borderRadius: 5,
  },
  link: { marginTop: 10, color: "blue", textAlign: "center" },
});
