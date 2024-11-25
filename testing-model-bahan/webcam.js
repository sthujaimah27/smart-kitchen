class Webcam {
  constructor(webcamElement) {
      this.webcamElement = webcamElement;
  }

  async setup() {
      try {
          const stream = await navigator.mediaDevices.getUserMedia({ video: true });
          this.webcamElement.srcObject = stream;
          this.webcamElement.play();
          console.log("Webcam is set up successfully.");
      } catch (error) {
          console.error("Error accessing webcam: ", error);
      }
  }

  capture() {
      return tf.tidy(() => {
          const webcamImage = tf.browser.fromPixels(this.webcamElement);
          const reversedImage = webcamImage.reverse(1);  // Reversing the image if needed
          const croppedImage = this.cropImage(reversedImage);
          const batchedImage = croppedImage.expandDims(0);
          return batchedImage.toFloat().div(tf.scalar(127)).sub(tf.scalar(1));  // Normalizing
      });
  }

  cropImage(img) {
      const size = Math.min(img.shape[0], img.shape[1]);
      const centerHeight = img.shape[0] / 2;
      const beginHeight = centerHeight - (size / 2);
      const centerWidth = img.shape[1] / 2;
      const beginWidth = centerWidth - (size / 2);
      return img.slice([beginHeight, beginWidth, 0], [size, size, 3]);
  }
}

async function init() {
  try {
      await webcam.setup();  // Set up webcam
      mobilenet = await loadMobilenet();
      tf.tidy(() => mobilenet.predict(webcam.capture()));
      console.log("Mobilenet initialized successfully");
  } catch (error) {
      console.error("Error during initialization:", error);
  }
}

const webcam = new Webcam(document.getElementById('wc'));  // Setup webcam for HTML element with id 'wc'
init();  // Initialize webcam and mobilenet
