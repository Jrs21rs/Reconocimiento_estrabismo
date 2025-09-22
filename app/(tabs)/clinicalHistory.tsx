import { LinearGradient } from 'expo-linear-gradient';
import { ScrollView, StyleSheet, Text, View } from 'react-native';

// Datos de ejemplo - Reemplazar con datos reales del backend
const historialEjemplo = [
  {
    id: 1,
    fecha: '2023-09-21',
    resultado: 'Positivo',
    observaciones: 'Se detectó estrabismo en ojo derecho',
    gravedad: 'Moderada'
  },
  {
    id: 2,
    fecha: '2023-08-15',
    resultado: 'Negativo',
    observaciones: 'No se detectaron anomalías',
    gravedad: 'Ninguna'
  },
  // Agregar más registros según sea necesario
];

export default function ClinicalHistoryScreen() {
  return (
    <LinearGradient
      colors={['#4c669f', '#3b5998', '#192f6a']}
      style={styles.container}
    >
      <ScrollView style={styles.scrollView}>
        <Text style={styles.title}>Historial Clínico</Text>
        
        {historialEjemplo.map((registro) => (
          <View key={registro.id} style={styles.card}>
            <View style={styles.cardHeader}>
              <Text style={styles.fecha}>{registro.fecha}</Text>
              <Text 
                style={[
                  styles.resultado, 
                  { color: registro.resultado === 'Positivo' ? '#ff4444' : '#4CAF50' }
                ]}
              >
                {registro.resultado}
              </Text>
            </View>
            
            <View style={styles.cardBody}>
              <Text style={styles.label}>Observaciones:</Text>
              <Text style={styles.text}>{registro.observaciones}</Text>
              
              <Text style={styles.label}>Gravedad:</Text>
              <Text style={styles.text}>{registro.gravedad}</Text>
            </View>
          </View>
        ))}
      </ScrollView>
    </LinearGradient>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  scrollView: {
    flex: 1,
    padding: 20,
  },
  title: {
    fontSize: 32,
    fontWeight: 'bold',
    color: '#ffffff',
    textAlign: 'center',
    marginBottom: 20,
    textShadowColor: 'rgba(0, 0, 0, 0.3)',
    textShadowOffset: { width: 1, height: 1 },
    textShadowRadius: 3,
  },
  card: {
    backgroundColor: 'rgba(255, 255, 255, 0.95)',
    borderRadius: 10,
    padding: 15,
    marginBottom: 15,
    elevation: 3,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.25,
    shadowRadius: 3.84,
  },
  cardHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 10,
    borderBottomWidth: 1,
    borderBottomColor: '#eee',
    paddingBottom: 10,
  },
  fecha: {
    fontSize: 16,
    color: '#4c669f',
    fontWeight: 'bold',
  },
  resultado: {
    fontSize: 16,
    fontWeight: 'bold',
  },
  cardBody: {
    gap: 5,
  },
  label: {
    fontSize: 14,
    color: '#666',
    marginTop: 5,
  },
  text: {
    fontSize: 16,
    color: '#333',
    marginBottom: 5,
  },
});