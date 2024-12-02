let webcam;
let model;
let isRecognizing = false;

// Inisialisasi webcam dan model
async function init() {
  const videoElement = document.getElementById('video');
  webcam = new Webcam(videoElement);
  await webcam.setup();
  
  try {
    model = await tf.loadLayersModel('model/model.json');  // Pastikan URL model benar
    console.log("Model Loaded Successfully");
  } catch (error) {
    console.error("Error loading model: ", error);
  }
  
  // Memuat model MobileNet yang telah di-fine-tune dari Google Drive
  const modelUrl = 'https://drive.google.com/file/d/1-62v2Qbfn6LPYtWOzkE3psmlO_n3L4UB/view?usp=drive_link'; // Ganti dengan ID model Anda
  model = await tf.loadLayersModel(modelUrl);  // Memuat model TensorFlow.js dari URL
  console.log("Model Loaded Successfully");
}

// Fungsi untuk memulai pengenalan gambar
let lastPredictedTime = Date.now();
async function startRecognition() {
  isRecognizing = true;
  document.getElementById('startBtn').disabled = true;
  document.getElementById('stopBtn').disabled = false;

  while (isRecognizing) {
    if (Date.now() - lastPredictedTime > 500) { // update setiap 500ms
      const image = webcam.capture(); // Tangkap gambar dari webcam
      const prediction = await model.predict(image); // Gunakan model untuk prediksi

      // Tampilkan hasil prediksi
      const predictionText = `I see: ${prediction[0].className} with ${(prediction[0].probability * 100).toFixed(2)}% confidence.`;
      document.getElementById('prediction').innerText = predictionText;

      lastPredictedTime = Date.now(); // Setel waktu prediksi terakhir
    }

    await tf.nextFrame(); // Tunggu frame berikutnya
  }
}

async function startRecognition() {
  isRecognizing = true;
  document.getElementById('startBtn').disabled = true;
  document.getElementById('stopBtn').disabled = false;

  while (isRecognizing) {
    const image = webcam.capture(); // Tangkap gambar dari webcam
    console.log("Captured Image: ", image);

    const prediction = await model.predict(image);
    console.log(prediction);  // Lihat struktur dari output model
    
    // Sesuaikan cara menampilkan hasil prediksi
    const predictionText = `I see: ${prediction[0].label} with ${(prediction[0].confidence * 100).toFixed(2)}% confidence.`;    
  }
}

// Fungsi untuk menghentikan pengenalan
function stopRecognition() {
  isRecognizing = false;
  document.getElementById('startBtn').disabled = false;
  document.getElementById('stopBtn').disabled = true;
}

// Panggil init() saat halaman selesai dimuat
init();
