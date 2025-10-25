import * as ImagePicker from 'expo-image-picker';
import { useState } from 'react';
import { Image, StyleSheet, Text, TouchableOpacity, View } from 'react-native';

export default function HomeScreen() {
  const [selectedImage, setSelectedImage] = useState<string | null>(null);

  const handleTakePhoto = async () => {
    try {
      console.log('Solicitando permisos de cámara...');
      // Solicitar permisos de cámara
      const { status } = await ImagePicker.requestCameraPermissionsAsync();
      console.log('Estado de permisos de cámara:', status);
      if (status !== 'granted') {
        alert('Se necesitan permisos de cámara para continuar');
        return;
      }

      // Abrir la cámara
      const result = await ImagePicker.launchCameraAsync({
        mediaTypes: ImagePicker.MediaTypeOptions.Images,
        allowsEditing: true,
        aspect: [4, 3],
        quality: 1,
      });

      if (!result.canceled && result.assets && result.assets.length > 0) {
        console.log('URI de la imagen:', result.assets[0].uri);
        setSelectedImage(result.assets[0].uri);
        // Aquí posteriormente enviaremos la imagen al backend para el análisis
      }
    } catch (error) {
      console.error('Error al tomar la foto:', error);
      alert('Hubo un error al tomar la foto. Por favor intente nuevamente.');
    }
  };

  const handleSelectPhoto = async () => {
    try {
      console.log('Solicitando permisos de galería...');
      // Solicitar permisos de galería
      const { status } = await ImagePicker.requestMediaLibraryPermissionsAsync();
      console.log('Estado de permisos de galería:', status);
      
      if (status !== 'granted') {
        alert('Se necesitan permisos de galería para continuar');
        return;
      }

      console.log('Abriendo selector de imágenes...');
      // Abrir selector de imágenes
      const result = await ImagePicker.launchImageLibraryAsync({
        mediaTypes: ImagePicker.MediaTypeOptions.Images,
        allowsEditing: true,
        aspect: [4, 3],
        quality: 1,
      });
      console.log('Resultado de la selección:', result);

      if (!result.canceled && result.assets && result.assets.length > 0) {
        console.log('URI de la imagen:', result.assets[0].uri);
        setSelectedImage(result.assets[0].uri);
        
        // Enviar imagen al API de FastAPI
        const formData = new FormData();
        formData.append('file', {
          uri: result.assets[0].uri,
          type: 'image/jpeg',
          name: selectedImage,
        }as any);

        console.log('Enviando imagen al API...');
        const response = await fetch('https://fastapi-tppn.onrender.com/predict', {
          method: 'POST',
          body: formData,
          headers: {
            'Content-Type': 'multipart/form-data',
          },
        });

        const data = await response.json();
        console.log('Respuesta del API:', data);
        
        if (data.tieneEstrabismo) {
          alert(`Estrabismo detectado\nConfianza: ${(data.confianza * 100).toFixed(2)}%`);
        } else {
          alert(`No se detectó estrabismo\nConfianza: ${(data.confianza * 100).toFixed(2)}%`);
        }
      }
    } catch (error) {
      console.error('Error al seleccionar la foto:', error);
      alert('Hubo un error al seleccionar la foto. Por favor intente nuevamente.');
    }
    const limpiarResultado = () => {
    setSelectedImage(null);
  };
    
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Detección de Estrabismo</Text>
      
      <TouchableOpacity 
        style={styles.imageContainer} 
        onPress={handleSelectPhoto}
        activeOpacity={0.7}
      >
        {selectedImage ? (
          <Image 
            source={{ uri: selectedImage }} 
            style={styles.selectedImage}
            onError={(error) => {
              console.error('Error al cargar la imagen:', error.nativeEvent.error);
              alert('Error al cargar la imagen');
              setSelectedImage(null);
            }}
          />
        ) : (
          <View style={styles.placeholderContainer}>
            <Text style={styles.placeholderText}>
              Toca aquí para seleccionar una imagen{'\n'}o usa el botón para tomar una foto
            </Text>
          </View>
        )}
      </TouchableOpacity>

      <View style={styles.buttonContainer}>
        <TouchableOpacity style={styles.button} onPress={handleTakePhoto}>
          <Text style={styles.buttonText}>Tomar Foto</Text>
        </TouchableOpacity>

        <TouchableOpacity style={styles.button} onPress={handleSelectPhoto}>
          <Text style={styles.buttonText}>Subir Foto</Text>
        </TouchableOpacity>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 20,
    backgroundColor: '#4c669f',
  },
  title: {
    fontSize: 32,
    fontWeight: 'bold',
    textAlign: 'center',
    marginVertical: 20,
    color: '#ffffff',
    textShadowColor: 'rgba(0, 0, 0, 0.3)',
    textShadowOffset: { width: 2, height: 2 },
    textShadowRadius: 5,
  },
  imageContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#ffffff',
    borderRadius: 10,
    marginVertical: 20,
    borderWidth: 2,
    borderColor: '#3b5998',
    borderStyle: 'dashed',
    overflow: 'hidden',
  },
  selectedImage: {
    width: '100%',
    height: '100%',
    borderRadius: 8,
    resizeMode: 'contain',
  },
  placeholderContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: 20,
  },
  placeholderText: {
    color: '#4c669f',
    fontSize: 16,
    textAlign: 'center',
    paddingHorizontal: 20,
  },
  buttonContainer: {
    flexDirection: 'row',
    justifyContent: 'space-around',
    marginBottom: 20,
  },
  button: {
    backgroundColor: '#ffffff',
    padding: 15,
    borderRadius: 25,
    width: '45%',
    elevation: 3,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.25,
    shadowRadius: 3.84,
  },
  buttonText: {
    color: '#4c669f',
    textAlign: 'center',
    fontSize: 16,
    fontWeight: 'bold',
  },
})