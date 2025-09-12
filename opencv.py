import cv2
import numpy as np
import tensorflow as tf


model = tf.lite.Interpreter(model_path='estrabismo_model.tflite') 
model.allocate_tensors()  


input_details = model.get_input_details()
output_details = model.get_output_details()


cap = cv2.VideoCapture(0) 


eye_cascade = cv2.CascadeClassifier(cv2.data.haarcascades + 'haarcascade_eye.xml')


if not cap.isOpened():
    print("No se pudo abrir la cámara.")
    exit()


cv2.namedWindow("Ojos", cv2.WINDOW_NORMAL)  
cv2.resizeWindow("Ojos", 800, 600)  

while True:
    
    ret, frame = cap.read()

    if not ret:
        print("No se pudo obtener el fotograma.")
        break

    
    frame = cv2.flip(frame, 1)

    
    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

    
    eyes = eye_cascade.detectMultiScale(gray, scaleFactor=1.1, minNeighbors=5, minSize=(30, 30))

    
    for (ex, ey, ew, eh) in eyes:
        
        eye_region = frame[ey: ey + eh, ex: ex + ew]
        
        eye_region_resized = cv2.resize(eye_region, (64, 64))

        
        eye_region_array = np.expand_dims(eye_region_resized, axis=0) / 255.0  

        
        model.set_tensor(input_details[0]['index'], eye_region_array.astype(np.float32))
        model.invoke()

        prediction = model.get_tensor(output_details[0]['index'])

        
        if prediction[0] > 0.5:
            
            cv2.rectangle(frame, (ex, ey), (ex + ew, ey + eh), (0, 0, 255), 2)  
        else:
           
            cv2.rectangle(frame, (ex, ey), (ex + ew, ey + eh), (0, 255, 0), 2)  

    
    cv2.imshow("Ojos", frame)  

  
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break


cap.release()
cv2.destroyAllWindows()
