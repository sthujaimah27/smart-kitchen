let mobilenet;
let model;
let isPredicting = false; // Flag untuk kontrol prediksi
let dataset = new RPSDataset(); // Dataset untuk menyimpan contoh gambar dan label
const webcam = new Webcam(document.getElementById('wc')); // Webcam untuk capture gambar
const classNames = ['Avocado', 'Chili Powder', 'Onion', 'Garlic', 'Eggs']; // Daftar kategori bahan makanan

// Fungsi untuk memulai prediksi
async function startPredicting() {
    isPredicting = true;
    await predict(); // Memulai prediksi
}

// Fungsi untuk menghentikan prediksi
function stopPredicting() {
    isPredicting = false;
}

// Fungsi prediksi untuk gambar yang diambil dari webcam
async function predict() {
    while (isPredicting) {
        const img = webcam.capture();  // Capture gambar dari webcam
        const predictions = model.predict(img);  // Melakukan prediksi menggunakan model
        const predictedClass = predictions.as1D().argMax().dataSync()[0];  // Ambil kelas dengan probabilitas tertinggi
        const predictionLabel = classNames[predictedClass];  // Ambil label prediksi

        document.getElementById('prediction').innerText = `Prediction: ${predictionLabel}`;  // Tampilkan prediksi ke UI
        await tf.nextFrame();  // Tunggu hingga frame berikutnya
    }
}

// Fungsi untuk menangani upload file gambar
const fileInput = document.getElementById('fileInput');
fileInput.addEventListener('change', (event) => {
    const files = event.target.files;
    if (files) {
        Array.from(files).forEach(file => {
            const reader = new FileReader();
            reader.onload = function(e) {
                const imgElement = new Image();
                imgElement.src = e.target.result;
                imgElement.onload = () => {
                    handleFileUpload(imgElement, file.name);  // Proses gambar yang di-upload
                };
            };
            reader.readAsDataURL(file);
        });
    }
});

// Fungsi untuk memproses gambar yang di-upload
function handleFileUpload(img, fileName) {
    const label = fileName.split('.')[0];  // Ambil label dari nama file (misal: avocado.jpg → avocado)
    const classElement = document.getElementById(label + 'Samples');
    let currentSamples = parseInt(classElement.innerText.split(': ')[1]);
    classElement.innerText = `${label} samples: ${currentSamples + 1}`;

    // Proses gambar yang di-upload untuk prediksi
    predictImage(img);
}

// Fungsi untuk memprediksi gambar yang di-upload
function predictImage(img) {
    // Proses gambar dan prediksi menggunakan model
    const imgTensor = tf.browser.fromPixels(img).resizeNearestNeighbor([224, 224]).toFloat().expandDims();
    model.predict(imgTensor).then(predictions => {
        const predictedClass = predictions.argMax(-1).dataSync()[0];
        const predictionLabel = classNames[predictedClass];  // Ambil label berdasarkan prediksi
        document.getElementById('prediction').innerText = `Predicted: ${predictionLabel}`;  // Tampilkan hasil prediksi
    });
}

// Fungsi untuk melatih model
async function train() {
    dataset.ys = null;
    dataset.encodeLabels(5); // Encode label menjadi format one-hot

    // Definisikan model menggunakan MobileNet sebagai feature extractor
    model = tf.sequential({
        layers: [
            tf.layers.flatten({ inputShape: mobilenet.outputs[0].shape.slice(1) }),
            tf.layers.dense({ units: 128, activation: 'relu' }),
            tf.layers.dense({ units: 64, activation: 'relu' }),
            tf.layers.dense({ units: 5, activation: 'softmax' })  // 5 kelas bahan
        ]
    });

    const optimizer = tf.train.adam(0.0001);  // Optimizer Adam
    model.compile({ optimizer: optimizer, loss: 'categoricalCrossentropy', metrics: ['accuracy'] });

    // Melatih model
    await model.fit(dataset.xs, dataset.ys, {
        epochs: 10,
        callbacks: {
            onBatchEnd: async (batch, logs) => {
                console.log(`Loss: ${logs.loss.toFixed(5)}`);
            }
        }
    });

    alert('Training completed!');
}

// Fungsi untuk menyimpan model
function saveModel() {
    model.save('downloads://dapur_pintar_model');  // Simpan model ke lokal
}

// Fungsi untuk memuat model
async function loadMobilenet() {
    const mobilenet = await tf.loadLayersModel('https://storage.googleapis.com/tfjs-models/tfjs/mobilenet_v1_1.0_224/model.json');
    const layer = mobilenet.getLayer('conv_pw_13_relu');
    return tf.model({ inputs: mobilenet.inputs, outputs: layer.output });
}

// Fungsi untuk memulai inisialisasi
async function init() {
    try {
        await webcam.setup();
        mobilenet = await loadMobilenet();  // Memuat MobileNet model
        tf.tidy(() => mobilenet.predict(webcam.capture()));  // Mengambil gambar pertama untuk memulai
        console.log("Mobilenet initialized successfully.");
    } catch (error) {
        console.error("Error during initialization:", error);
    }
}

// Panggil fungsi inisialisasi
init();
