import { Picker } from '@react-native-picker/picker';
import { router } from 'expo-router';
import React, { useEffect, useState } from 'react';
import { Alert, ScrollView, StyleSheet, Text, TextInput, TouchableOpacity, View } from 'react-native';
import { useAuth } from '../../context/AuthContext';
import { PatientData, registerPatient } from '../../services/patientService';

type DocumentType = 'REGISTRO_CIVIL' | 'TI' | 'NUIP' | 'PASAPORTE';
type GenderType = 'M' | 'F' | 'O' | 'N';

export default function PatientRegistrationForm() {
  const onSuccess = () => {
    router.replace('/(tabs)');
  };
  const { user } = useAuth();
  const [isLoading, setIsLoading] = useState(false);
  const [formData, setFormData] = useState<PatientData>({
    tipoDocumento: 'TI',
    documentoIdentidad: '',
    nombres: '',
    apellidos: '',
    fechaNacimiento: '',
    genero: 'N',
    documentoIdentidadResponsable: user?.documento || '',
    parentesco: '',
    numeroTele: ''
  });

  useEffect(() => {
    if (user?.documento) {
      setFormData(prev => ({
        ...prev,
        documentoIdentidadResponsable: user.documento
      }));
    }
  }, [user]);

  const handleChange = (name: keyof PatientData, value: string) => {
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async () => {
    if (!formData.documentoIdentidad || !formData.nombres || !formData.apellidos || 
        !formData.fechaNacimiento || !formData.parentesco) {
      Alert.alert('Error', 'Por favor complete todos los campos obligatorios');
      return;
    }

    try {
      setIsLoading(true);
      const result = await registerPatient(formData, user?.token || '');
      
      if (result.success) {
        Alert.alert('Éxito', 'Paciente registrado correctamente', [
          { text: 'OK', onPress: onSuccess }
        ]);
      } else {
        Alert.alert('Error', result.error || 'Error al registrar el paciente');
      }
    } catch (error) {
      console.error('Error:', error);
      Alert.alert('Error', 'Ocurrió un error al procesar la solicitud');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <ScrollView style={styles.container}>
      <Text style={styles.title}>Registro de Paciente</Text>
      
      <View style={styles.formGroup}>
        <Text style={styles.label}>Tipo de Documento *</Text>
        <View style={styles.pickerContainer}>
          <Picker
            selectedValue={formData.tipoDocumento}
            onValueChange={(value) => handleChange('tipoDocumento', value)}
            style={styles.picker}
          >
            <Picker.Item label="Tarjeta de Identidad" value="TI" />
            <Picker.Item label="Registro Civil" value="REGISTRO_CIVIL" />
            <Picker.Item label="NUIP" value="NUIP" />
            <Picker.Item label="Pasaporte" value="PASAPORTE" />
          </Picker>
        </View>
      </View>

      <View style={styles.formGroup}>
        <Text style={styles.label}>Número de Documento *</Text>
        <TextInput
          style={styles.input}
          value={formData.documentoIdentidad}
          onChangeText={(text) => handleChange('documentoIdentidad', text)}
          placeholder="Número de documento"
          keyboardType="numeric"
        />
      </View>

      <View style={styles.formGroup}>
        <Text style={styles.label}>Nombres *</Text>
        <TextInput
          style={styles.input}
          value={formData.nombres}
          onChangeText={(text) => handleChange('nombres', text.toUpperCase())}
          placeholder="Nombres completos"
          autoCapitalize="words"
        />
      </View>

      <View style={styles.formGroup}>
        <Text style={styles.label}>Apellidos *</Text>
        <TextInput
          style={styles.input}
          value={formData.apellidos}
          onChangeText={(text) => handleChange('apellidos', text.toUpperCase())}
          placeholder="Apellidos completos"
          autoCapitalize="words"
        />
      </View>

      <View style={styles.formGroup}>
        <Text style={styles.label}>Fecha de Nacimiento *</Text>
        <TextInput
          style={styles.input}
          value={formData.fechaNacimiento}
          onChangeText={(text) => handleChange('fechaNacimiento', text)}
          placeholder="YYYY-MM-DD"
          keyboardType="numeric"
        />
      </View>

      <View style={styles.formGroup}>
        <Text style={styles.label}>Género</Text>
        <View style={styles.pickerContainer}>
          <Picker
            selectedValue={formData.genero}
            onValueChange={(value) => handleChange('genero', value)}
            style={styles.picker}
          >
            <Picker.Item label="No especifica" value="N" />
            <Picker.Item label="Masculino" value="M" />
            <Picker.Item label="Femenino" value="F" />
            <Picker.Item label="Otro" value="O" />
          </Picker>
        </View>
      </View>

      <View style={styles.formGroup}>
        <Text style={styles.label}>Documento del Responsable *</Text>
        <TextInput
          style={[styles.input, !user?.documento ? {} : styles.disabledInput]}
          value={formData.documentoIdentidadResponsable}
          onChangeText={(text) => handleChange('documentoIdentidadResponsable', text)}
          placeholder="Documento del responsable"
          keyboardType="numeric"
          editable={!user?.documento}
        />
      </View>

      <View style={styles.formGroup}>
        <Text style={styles.label}>Parentesco con el Responsable *</Text>
        <TextInput
          style={styles.input}
          value={formData.parentesco}
          onChangeText={(text) => handleChange('parentesco', text)}
          placeholder="Ej: Padre, Madre, Tío, etc."
        />
      </View>


      <View style={styles.formGroup}>
        <Text style={styles.label}>Teléfono</Text>
        <TextInput
          style={styles.input}
          value={formData.numeroTele}
          onChangeText={(text) => handleChange('numeroTele', text)}
          placeholder="Número de teléfono"
          keyboardType="phone-pad"
        />
      </View>

      <TouchableOpacity 
        style={[styles.button, isLoading && styles.buttonDisabled]} 
        onPress={handleSubmit}
        disabled={isLoading}
      >
        <Text style={styles.buttonText}>
          {isLoading ? 'Registrando...' : 'Registrar Paciente'}
        </Text>
      </TouchableOpacity>
    </ScrollView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 20,
    backgroundColor: '#fff',
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    marginBottom: 20,
    textAlign: 'center',
    color: '#333',
  },
  formGroup: {
    marginBottom: 15,
  },
  label: {
    marginBottom: 5,
    fontWeight: '500',
    color: '#444',
  },
  input: {
    borderWidth: 1,
    borderColor: '#ddd',
    padding: 12,
    borderRadius: 8,
    fontSize: 16,
    backgroundColor: '#f9f9f9',
  },
  disabledInput: {
    backgroundColor: '#eee',
    color: '#888',
  },
  pickerContainer: {
    borderWidth: 1,
    borderColor: '#ddd',
    borderRadius: 8,
    marginBottom: 10,
    overflow: 'hidden',
    backgroundColor: '#f9f9f9',
  },
  picker: {
    width: '100%',
  },
  button: {
    backgroundColor: '#4a90e2',
    padding: 15,
    borderRadius: 8,
    alignItems: 'center',
    marginTop: 20,
    marginBottom: 30,
  },
  buttonText: {
    color: 'white',
    fontWeight: 'bold',
    fontSize: 16,
  },
  buttonDisabled: {
    backgroundColor: '#a0c4ff',
  },
});
