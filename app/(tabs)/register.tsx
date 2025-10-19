import { Link } from "expo-router";
import { useState } from "react";
import { Alert, Button, StyleSheet, Text, TextInput, TouchableOpacity, View } from "react-native";
import { registerUser } from "../../services/userService";

export default function RegisterScreen() {
  const [nombres, setNombres] = useState("");
  const [apellidos, setApellidos] = useState("");
  const [edad, setEdad] = useState("");
  const [correo, setCorreo] = useState("");
  const [password, setPassword] = useState("");
  const [numeroTele, setNumeroTele] = useState("");
  const [rol, setRol] = useState(""); // inicialmente vacío
  const [step, setStep] = useState(1); // 1: elegir tipo, 2: formulario

  const handleSelectRol = (tipo: "paciente" | "responsable") => {
    setRol(tipo);
    setStep(2);
  };

  const handleRegister = async () => {
    try {
      const userData = {
        nombres,
        apellidos,
        edad: parseInt(edad),
        correo,
        password,
        numeroTele,
        rol,
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
      {step === 1 ? (
        <>
          <Text style={styles.title}>Selecciona el tipo de registro</Text>
          <TouchableOpacity
            style={[styles.optionButton, { backgroundColor: "#4CAF50" }]}
            onPress={() => handleSelectRol("paciente")}
          >
            <Text style={styles.optionText}>Paciente</Text>
          </TouchableOpacity>

          <TouchableOpacity
            style={[styles.optionButton, { backgroundColor: "#2196F3" }]}
            onPress={() => handleSelectRol("responsable")}
          >
            <Text style={styles.optionText}>Responsable</Text>
          </TouchableOpacity>
        </>
      ) : (
        <>
          <Text style={styles.title}>
            Registro de {rol === "paciente" ? "Paciente" : "Responsable"}
          </Text>
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
          <TouchableOpacity onPress={() => setStep(1)}>
            <Text style={styles.backText}>← Volver a selección</Text>
          </TouchableOpacity>
          <Link href="/login" style={styles.link}>
            ¿Ya tienes cuenta? Inicia sesión
          </Link>
        </>
      )}
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, justifyContent: "center", padding: 20 },
  title: {
    fontSize: 22,
    fontWeight: "bold",
    marginBottom: 25,
    textAlign: "center",
  },
  input: {
    borderWidth: 1,
    padding: 10,
    marginBottom: 10,
    borderRadius: 5,
  },
  link: { marginTop: 10, color: "blue", textAlign: "center" },
  optionButton: {
    padding: 15,
    marginVertical: 10,
    borderRadius: 10,
  },
  optionText: {
    color: "white",
    fontSize: 18,
    textAlign: "center",
  },
  backText: {
    marginTop: 15,
    textAlign: "center",
    color: "gray",
  },
});
