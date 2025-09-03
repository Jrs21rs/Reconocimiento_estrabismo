import tensorflow as tf
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Conv2D, MaxPooling2D, Flatten, Dense
from tensorflow.keras.preprocessing.image import ImageDataGenerator
from tensorflow.keras.optimizers import Adam


train_dir = 'C:/Users/Sala_/Documents/dataset/train'  
valid_dir = 'C:/Users/Sala_/Documents/dataset/validation'  


# Preprocesamiento de imágenes con ImageDataGenerator
train_datagen = ImageDataGenerator(
    rescale=1./255,  # Normalizar las imágenes
    rotation_range=30, 
    width_shift_range=0.2,
    height_shift_range=0.2, 
    shear_range=0.2, 
    zoom_range=0.2, 
    horizontal_flip=True, 
    fill_mode='nearest'
)

validation_datagen = ImageDataGenerator(rescale=1./255)  # Solo normalización para validación

# Cargar las imágenes desde las carpetas
train_generator = train_datagen.flow_from_directory(
    train_dir,  # Ruta de las imágenes de entrenamiento
    target_size=(64, 64),  # Redimensionar las imágenes a 64x64
    batch_size=32,  # Tamaño de los lotes
    class_mode='binary'  # Clasificación binaria (estrabismo vs. no estrabismo)
)

validation_generator = validation_datagen.flow_from_directory(
    valid_dir,  # Ruta de las imágenes de validación
    target_size=(64, 64),  # Redimensionar las imágenes a 64x64
    batch_size=32,  # Tamaño de los lotes
    class_mode='binary'  # Clasificación binaria
)

# Definir el modelo de red neuronal convolucional (CNN)
model = Sequential([
    Conv2D(32, (3, 3), activation='relu', input_shape=(64, 64, 3)),
    MaxPooling2D(pool_size=(2, 2)),
    
    Conv2D(64, (3, 3), activation='relu'),
    MaxPooling2D(pool_size=(2, 2)),
    
    Flatten(),
    Dense(128, activation='relu'),
    Dense(1, activation='sigmoid')  # Salida binaria: 0 para sin estrabismo, 1 para con estrabismo
])

# Compilar el modelo
model.compile(optimizer=Adam(), loss='binary_crossentropy', metrics=['accuracy'])

# Entrenar el modelo
history = model.fit(
    train_generator,
    steps_per_epoch=100,  # Ajusta según el tamaño de tu conjunto de datos
    epochs=10,
    validation_data=validation_generator,
    validation_steps=50  # Ajusta según el tamaño de tu conjunto de validación
)
model.save('estrabismo_model.keras')  # Usar el formato nativo de Keras

test_loss, test_acc = model.evaluate(validation_generator)
print(f"Precisión en conjunto de validación: {test_acc}")


############################################################################
import cv2
import numpy as np
from tensorflow.keras.models import load_model

# Cargar el modelo entrenado
model = load_model('estrabismo_model.keras')  # Asegúrate de usar la ruta correcta

# Usar la cámara del PC
cap = cv2.VideoCapture(0)  # 0 es el índice de la cámara por defecto

# Cargar el clasificador en cascada para ojos
eye_cascade = cv2.CascadeClassifier(cv2.data.haarcascades + 'haarcascade_eye.xml')

# Verifica que la cámara está abierta
if not cap.isOpened():
    print("No se pudo abrir la cámara.")
    exit()

# Configurar la ventana para que sea redimensionable y con nombre "Ojos"
cv2.namedWindow("Ojos", cv2.WINDOW_NORMAL)  # Hacer la ventana redimensionable
cv2.resizeWindow("Ojos", 800, 600)  # Establecer tamaño inicial (puedes ajustarlo si quieres)

while True:
    # Capturar un fotograma del video
    ret, frame = cap.read()

    if not ret:
        print("No se pudo obtener el fotograma.")
        break

    # Voltear la imagen para efecto espejo (horizontal)
    frame = cv2.flip(frame, 1)

    # Convertir el fotograma a escala de grises para mejorar la detección
    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

    # Detectar los ojos en el fotograma
    eyes = eye_cascade.detectMultiScale(gray, scaleFactor=1.1, minNeighbors=5, minSize=(30, 30))

    # Dibujar rectángulos alrededor de los ojos detectados
    for (ex, ey, ew, eh) in eyes:
        # Realizar la predicción de estrabismo para cada ojo detectado
        eye_region = frame[ey: ey + eh, ex: ex + ew]
        # Redimensionar la región de los ojos para que coincida con el tamaño de entrada del modelo
        eye_region_resized = cv2.resize(eye_region, (64, 64))

        # Normalizar la imagen antes de pasarla al modelo
        eye_region_array = np.expand_dims(eye_region_resized, axis=0) / 255.0  # Normalizar la imagen

        # Realizar la predicción en los ojos detectados
        prediction = model.predict(eye_region_array)

        # Cambiar el color de los cuadros dependiendo de la predicción
        if prediction[0] > 0.5:
            # Estrabismo - Cuadro rojo
            cv2.rectangle(frame, (ex, ey), (ex + ew, ey + eh), (0, 0, 255), 2)  # Rojo
        else:
            # No estrabismo - Cuadro verde
            cv2.rectangle(frame, (ex, ey), (ex + ew, ey + eh), (0, 255, 0), 2)  # Verde

    # Mostrar el fotograma con el color adecuado (rojo o verde)
    cv2.imshow("Ojos", frame)  # Nombre de la ventana "Ojos"

    # Salir si presionas la tecla 'q'
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

# Liberar la cámara y cerrar las ventanas
cap.release()
cv2.destroyAllWindows()
