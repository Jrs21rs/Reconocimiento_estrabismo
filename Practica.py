import tensorflow as tf
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Conv2D, MaxPooling2D, Flatten, Dense
from tensorflow.keras.preprocessing.image import ImageDataGenerator
from tensorflow.keras.optimizers import Adam


train_dir = 'C:/Users/Sala_/Documents/dataset/train'  
valid_dir = 'C:/Users/Sala_/Documents/dataset/validation'  

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
    MaxPooling2D(pool_size=(2, 2)),
    
    Conv2D(64, (3, 3), activation='relu'),
    MaxPooling2D(pool_size=(2, 2)),
    
    Flatten(),
    Dense(128, activation='relu'),
    Dense(1, activation='sigmoid')  
])


model.compile(optimizer=Adam(), loss='binary_crossentropy', metrics=['accuracy'])


history = model.fit(
    train_generator,
    steps_per_epoch=100,  
    epochs=10,
    validation_data=validation_generator,
    validation_steps=50  
)
model.save('estrabismo_model.keras')  

test_loss, test_acc = model.evaluate(validation_generator)
print(f"Precisión en conjunto de validación: {test_acc}")

# TensorFlow Lite 


converter = tf.lite.TFLiteConverter.from_keras_model(model)


converter.optimizations = [tf.lite.Optimize.DEFAULT]  
tflite_model = converter.convert()


with open('estrabismo_model.tflite', 'wb') as f:
    f.write(tflite_model)


print("Modelo convertido a TFLite y guardado como 'estrabismo_model.tflite'.")
