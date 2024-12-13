import tensorflow as tf
import tensorflowjs as tfjs

# Load your Keras model
model = tf.keras.models.load_model('./fine_tuned_ingredients.h5')

# Convert to TensorFlow.js format
tfjs.converters.save_keras_model(model, 'model_tfjs')