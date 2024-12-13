const tf = require('@tensorflow/tfjs-node');
const fs = require('fs');
const path = require('path');
const sharp = require('sharp');

const classNames = [
    "butter", "beetroot", "black_beans", "bay_leaves", "black_pepper", "baking_powder", "avocado", "cabbage",
    "brown_sugar", "beef", "chili_powder", "capsicum", "carrots", "cauliflower", "chopped_onion", "chicken",
    "cucumber", "corn_starch", "corn", "cilantro_leaves", "diced_tomatoes", "fresh_parsley", "eggplant",
    "garlic_powder", "flour", "fish", "garam_masala", "eggs", "garlic", "cumin", "green_onions",
    "grated_parmesan_cheese", "lettuce", "honey", "ground_cinnamon", "lemon", "ginger", "oil", "goat",
    "ground_turmeric", "shrimp", "salt", "raddish", "soy_beans", "potatoes", "plum_tomatoes", "sour_cream",
    "purple_onion", "peas", "spinach", "tempeh", "yellow_onion", "sugar", "turnip", "tofu"
];

async function loadModel(modelPath) {
    try {
        // Check if the model path exists
        if (!fs.existsSync(modelPath)) {
            throw new Error(`Model path does not exist: ${modelPath}`);
        }

        // Log the exact path being used
        console.log(`Attempting to load model from: file://${modelPath}`);
        const model = await tf.loadLayersModel(`file://${modelPath}`);
        
        // Additional verification
        if (!model) {
            throw new Error('Model loaded is undefined');
        }

        console.log('Model loaded successfully');
        return model;
    } catch (error) {
        console.error('Detailed model loading error:', error);
        throw error;
    }
}

async function preprocessImage(imagePath) {
    try {
        // Use sharp to resize image to 150x150
        const imageBuffer = await sharp(imagePath)
            .resize(150, 150)
            .toBuffer();

        // Convert image to tensor
        const tensor = tf.node.decodeImage(imageBuffer)
            .toFloat()
            .expandDims(0)
            .div(255.0);

        return tensor;
    } catch (error) {
        console.error('Error preprocessing image:', error);
        throw error;
    }
}

async function predictImage(model, imagePath) {
    try {
        // Preprocess image
        const imageTensor = await preprocessImage(imagePath);

        // Make prediction
        const prediction = model.predict(imageTensor);
        const predictedClassIndex = prediction.argMax(1).dataSync()[0];
        const predictedClassName = classNames[predictedClassIndex];

        // Clean up tensor to prevent memory leaks
        imageTensor.dispose();
        prediction.dispose();

        return {
            classIndex: predictedClassIndex,
            className: predictedClassName,
            confidence: prediction.max().dataSync()[0]
        };
    } catch (error) {
        console.error('Error predicting image:', error);
        throw error;
    }
}

module.exports = {
    loadModel,
    predictImage
}