import { Link } from "expo-router";
import { useState } from "react";
import { Alert, Button, StyleSheet, Text, TextInput, View } from "react-native";
import { loginUser } from "../../services/authService";

export default function LoginScreen() {
  const [correo, setCorreo] = useState("");
  const [password, setPassword] = useState("");

  const handleLogin = async () => {
    try {
      const response = await loginUser(correo, password);
      if (response.error) {
        Alert.alert("Error", response.error);
        return;
      }
      if (response.token) {
        // Aquí puedes manejar el token, por ejemplo guardarlo en AsyncStorage
        Alert.alert("Login exitoso", response.token);
      }
    } catch (error) {
      Alert.alert("Error", "Ocurrió un error durante el login");
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
