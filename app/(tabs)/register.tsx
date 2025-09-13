import { Link } from "expo-router";
import { useState } from "react";
import { Alert, Button, StyleSheet, Text, TextInput, View } from "react-native";
import { registerUser } from "../../services/userService";

export default function RegisterScreen() {
  const [nombres, setNombres] = useState("");
  const [apellidos, setApellidos] = useState("");
  const [edad, setEdad] = useState("");
  const [correo, setCorreo] = useState("");
  const [password, setPassword] = useState("");
  const [numeroTele, setNumeroTele] = useState("");
  const [rol, setRol] = useState("paciente");

  const handleRegister = async () => {
    try {
      const userData = {
        nombres,
        apellidos,
        edad: parseInt(edad),
        correo,
        password,
        numeroTele,
        
      };

      const response = await registerUser(userData);
      if (response.error) {
        Alert.alert("Error", response.error);
        return;
      }
      if (response.success) {
        Alert.alert("Éxito", "Registro completado correctamente");
        // Aquí puedes navegar a la pantalla de login
      }
    } catch (error) {
      Alert.alert("Error", "Ocurrió un error durante el registro");
    }
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Registro de Pacientes</Text>
      <TextInput
        style={styles.input}
        placeholder="Nombres"
        value={nombres}
        onChangeText={setNombres}
      />
      <TextInput
        style={styles.input}
        placeholder="Apellidos"
        value={apellidos}
        onChangeText={setApellidos}
      />
      <TextInput
        style={styles.input}
        placeholder="Edad"
        value={edad}
        onChangeText={setEdad}
        keyboardType="numeric"
      />
      <TextInput
        style={styles.input}
        placeholder="Correo"
        value={correo}
        onChangeText={setCorreo}
        keyboardType="email-address"
      />
      <TextInput
        style={styles.input}
        placeholder="Contraseña"
        value={password}
        onChangeText={setPassword}
        secureTextEntry
      />
      <TextInput
        style={styles.input}
        placeholder="Número de Teléfono"
        value={numeroTele}
        onChangeText={setNumeroTele}
        keyboardType="phone-pad"
      />
      <Button title="Registrarse" onPress={handleRegister} />
      <Link href="/login" style={styles.link}>
        ¿Ya tienes cuenta? Inicia sesión
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
