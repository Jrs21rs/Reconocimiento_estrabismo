import tensorflow as tf
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Conv2D, MaxPooling2D, Flatten, Dense, Dropout, BatchNormalization
from tensorflow.keras.preprocessing.image import ImageDataGenerator
from tensorflow.keras.optimizers import Adam
from tensorflow.keras.callbacks import EarlyStopping, ModelCheckpoint
import cv2
import numpy as np
from pathlib import Path


train_dir = 'C:/Users/Sony/Documents/dataset/train'
valid_dir = 'C:/Users/Sony/Documents/dataset/validation'

train_datagen = ImageDataGenerator(
    rescale=1./255,
    rotation_range=30,
    width_shift_range=0.2,
    height_shift_range=0.2,
    shear_range=0.2,
    zoom_range=0.2,
    horizontal_flip=True,
    fill_mode='nearest'
)

validation_datagen = ImageDataGenerator(rescale=1./255)

train_generator = train_datagen.flow_from_directory(
    train_dir,
    target_size=(64, 64),
    batch_size=32,
    class_mode='binary'
)

validation_generator = validation_datagen.flow_from_directory(
    valid_dir,
    target_size=(64, 64),
    batch_size=32,
    class_mode='binary'
)

model = Sequential([
    Conv2D(32, (3, 3), activation='relu', input_shape=(64, 64, 3)),
    BatchNormalization(),
    MaxPooling2D(pool_size=(2, 2)),

    Conv2D(64, (3, 3), activation='relu'),
    BatchNormalization(),
    MaxPooling2D(pool_size=(2, 2)),

    Conv2D(128, (3, 3), activation='relu'),
    BatchNormalization(),
    MaxPooling2D(pool_size=(2, 2)),

    Flatten(),
    Dense(256, activation='relu'),
    Dropout(0.5),
    Dense(1, activation='sigmoid')
])

model.compile(optimizer=Adam(), loss='binary_crossentropy', metrics=['accuracy'])

early_stop = EarlyStopping(monitor='val_loss', patience=3, restore_best_weights=True)
checkpoint = ModelCheckpoint("best_model.keras", save_best_only=True)

history = model.fit(
    train_generator,
    epochs=20,
    validation_data=validation_generator,
    callbacks=[early_stop, checkpoint]
)

model.save('estrabismo_model.keras')


keras_model_path = Path("estrabismo_model.keras")
tflite_model_path = Path("estrabismo_model.tflite")

if keras_model_path.exists():
    converter = tf.lite.TFLiteConverter.from_keras_model(model)
    converter.optimizations = [tf.lite.Optimize.DEFAULT]
    try:
        tflite_model = converter.convert()
        with open(tflite_model_path, "wb") as f:
            f.write(tflite_model)
        print(f"Modelo convertido a: {tflite_model_path.resolve()}")
    except Exception as e:
        print("Error al convertir:", e)
else:
    print("No se encontró estrabismo_model.keras")

test_loss, test_acc = model.evaluate(validation_generator)
print(f"Precisión validación: {test_acc:.3f}")

loaded_model = tf.keras.models.load_model("estrabismo_model.keras")
cap = cv2.VideoCapture(0)
eye_cascade = cv2.CascadeClassifier(cv2.data.haarcascades + 'haarcascade_eye.xml')

if not cap.isOpened():
    print("No se pudo abrir la cámara.")
    exit()

cv2.namedWindow("Ojos", cv2.WINDOW_NORMAL)
cv2.resizeWindow("Ojos", 800, 600)

print("Presiona 'c' para capturar y analizar, 'q' para salir.")

while True:
    ret, frame = cap.read()
    if not ret:
        print("No se pudo capturar frame.")
        break

    frame = cv2.flip(frame, 1)
    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

    
    cv2.imshow("Ojos", frame)

    key = cv2.waitKey(1) & 0xFF
    if key == ord('c'):
        eyes = eye_cascade.detectMultiScale(gray, scaleFactor=1.1, minNeighbors=5, minSize=(30, 30))
        for (ex, ey, ew, eh) in eyes:
            eye_region = frame[ey: ey + eh, ex: ex + ew]
            eye_resized = cv2.resize(eye_region, (64, 64))
            eye_array = np.expand_dims(eye_resized, axis=0) / 255.0

            prediction = loaded_model.predict(eye_array)
            score = prediction[0][0]

            if score > 0.5:
                color = (0, 0, 255)  
            else:
                color = (0, 255, 0)  

            cv2.rectangle(frame, (ex, ey), (ex + ew, ey + eh), color, 2)

        cv2.imshow("Ojos", frame)
        cv2.waitKey(0)  

    elif key == ord('q'):
        break

cap.release()
cv2.destroyAllWindows()
