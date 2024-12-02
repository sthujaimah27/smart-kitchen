class Webcam {
  constructor(videoElement) {
    this.videoElement = videoElement;
  }

  // Setup webcam
  async setup() {
  return new Promise((resolve, reject) => {
    navigator.mediaDevices.getUserMedia({ video: { width: 224, height: 224 } })
      .then(stream => {
        this.videoElement.srcObject = stream;
        this.videoElement.onloadeddata = () => {
          console.log("Webcam is streaming");
          resolve();
        };
      })
      .catch(error => {
        console.error("Error accessing webcam: ", error);
        reject(error);
      });
  });
}


  // Capture image from the webcam
  capture() {
    const webcamImage = tf.browser.fromPixels(this.videoElement);
    return webcamImage.expandDims(0).toFloat().div(tf.scalar(127)).sub(tf.scalar(1)); // Normalisasi
  }
}
