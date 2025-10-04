import { LinearGradient } from 'expo-linear-gradient';
import { useState } from "react";
import { Alert, ScrollView, StyleSheet, Text, TextInput, TouchableOpacity, View } from "react-native";
import { updateProfile } from "../services/updateProfileService";

export default function UpdateProfileScreen() {
  const [nombres, setNombres] = useState("");
  const [apellidos, setApellidos] = useState("");
  const [edad, setEdad] = useState("");
  const [correo, setCorreo] = useState("");
  const [numeroTele, setNumeroTele] = useState("");

  // @ts-ignore
  const handleUpdateProfile = async () => {
    try {
      // Validar todos los campos
      if (!nombres || !apellidos || !edad || !correo || !numeroTele) {
        Alert.alert("Error", "Por favor complete todos los campos");
        return;
      }

      // Validar que la edad sea un número válido
      const edadNum = parseInt(edad, 10);
      if (isNaN(edadNum) || edadNum <= 0) {
        Alert.alert("Error", "Por favor ingrese una edad válida");
        return;
      }

      // Validar formato de correo electrónico
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      if (!emailRegex.test(correo)) {
        Alert.alert("Error", "Por favor ingrese un correo electrónico válido");
        return;
      }

      // Validar formato de número de teléfono (10 dígitos)
      const phoneRegex = /^\d{10}$/;
      if (!phoneRegex.test(numeroTele)) {
        Alert.alert("Error", "Por favor ingrese un número de teléfono válido (10 dígitos)");
        return;
      }

      const userData = {
        nombres,
        apellidos,
        edad: edadNum,
        correo,
        numeroTele
      };
      const response = await updateProfile(userData);

      if (response.error) {
        Alert.alert("Error", response.error);
      } else {
        Alert.alert("Éxito", "Tus datos han sido actualizados correctamente");
        console.log("Respuesta del backend:", response);
      }
    } catch (error) {
      console.error("Error al actualizar perfil:", error);
      Alert.alert("Error", "Ocurrió un error al actualizar tus datos");
    }
  };
  return (
      <LinearGradient
          colors={['#4c669f', '#3b5998', '#192f6a']}
          style={styles.container}
      >
        <ScrollView contentContainerStyle={styles.scrollContent}>
          <View style={styles.content}>
            <Text style={styles.title}>Actualizar Datos</Text>

            <TextInput
                style={styles.input}
                placeholder="Nombres"
                placeholderTextColor="#666"
                value={nombres}
                onChangeText={setNombres}
            />

            <TextInput
                style={styles.input}
                placeholder="Apellidos"
                placeholderTextColor="#666"
                value={apellidos}
                onChangeText={setApellidos}
            />

            <TextInput
                style={styles.input}
                placeholder="Edad"
                placeholderTextColor="#666"
                value={edad}
                onChangeText={setEdad}
                keyboardType="numeric"
            />

            <TextInput
                style={styles.input}
                placeholder="Correo electrónico"
                placeholderTextColor="#666"
                value={correo}
                onChangeText={setCorreo}
                keyboardType="email-address"
                autoCapitalize="none"
            />

            <TextInput
                style={styles.input}
                placeholder="Número de teléfono"
                placeholderTextColor="#666"
                value={numeroTele}
                onChangeText={setNumeroTele}
                keyboardType="phone-pad"
            />

            <TouchableOpacity style={styles.button} onPress={handleUpdateProfile}>
              <Text style={styles.buttonText}>Guardar Cambios</Text>
            </TouchableOpacity>
          </View>
        </ScrollView>
      </LinearGradient>
  )
}

  const styles = StyleSheet.create({
    container: {
      flex: 1,
    },
    scrollContent: {
      flexGrow: 1,
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
      textShadowOffset: {width: 1, height: 1},
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
      shadowOffset: {width: 0, height: 2},
      shadowOpacity: 0.25,
      shadowRadius: 3.84,
    },
    buttonText: {
      color: "#4c669f",
      textAlign: "center",
      fontSize: 18,
      fontWeight: "bold",
    },
  });
