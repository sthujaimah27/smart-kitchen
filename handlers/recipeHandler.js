const pool = require('../db'); // Koneksi database
const fs = require('fs');
const path = require('path');
const Joi = require('joi');
const { verifyToken } = require('../utils/authUtils');
// const tf = require('@tensorflow/tfjs-node');
const { loadModel, predictImage } = require('./modelHandler')

let dataRecipes = {
  "detected_ingredient": "Ayam",
  "recommended_recipes": [
    {
      "id": 1372,
      "title": "Ayam Kecap ulalaaa",
      "image" : "https://img-global.cpcdn.com/recipes/f2aa46ae0654b804/680x964cq70/ayam-kecap-ulalaaa-foto-resep-utama.webp",
      "loves": 26,
      "ingredients": [
        "1/2 kg ayam",
        "Secukupnya kecap",
        "Secukupnya garam",
        "2 lembar daun salam",
        "3 cm laos",
        "Bumbu halus",
        "3 siung bawang merah",
        "2 siung bawang putih",
        "2 butir kemiri",
        "Merica",
        "2 cm kunyit",
        "1 cm jahe"
      ],
      "instructions": [
        "Cuci bersih ayam.",
        "Goreng ayam setengah matang dan tiriskan.",
        "Panaskan minyak 2-3 sdm dan masukan bumbu yg sudah dihaluskan.",
        "Tambahkan air secukupnya tunggu sampai mendidih.",
        "Lalu tambahkan kecap dan masukan ayam.",
        "Tambahkan garam, gula pasir, daun salam dan laos, icip rasa.",
        "Jika rasa sudah pas, tunggu hingga air menyusut.",
        "Hidangkan 😊"
      ],
      "source": "https://cookpad.com/id/resep/4359528-ayam-kecap-ulalaaa"
    },
    {
      "id": 1360,
      "title": "Ayam Suwir Khas Lombok",
      "image": "https://img-global.cpcdn.com/recipes/b26f7a78ca83efeb/680x964cq70/ayam-suwir-khas-lombok-foto-resep-utama.webp",
      "loves": 15,
      "ingredients": [
        "500 gr dada ayam fillet, rebus dan suwir",
        "2 lembar daun jeruk",
        "1 buah jeruk limau",
        "4 buah cabe merah keriting",
        "7 buah cabe rawit merah (sesuai selera)",
        "4 siung bawang putih",
        "5 buah bawang merah",
        "1 sdt terasi",
        "secukupnya Gula garam dan penyedap rasa (saya pakai totole)"
      ],
      "instructions": [
        "Tumis bumbu halus masukan daun jeruk tumis hingga matang, kemudian masukan ayam suwir.",
        "Bumbui dengan gula garam dan penyedap rasa.",
        "Beri sedikit air bilang ingin 'nyemek'.",
        "Masukan perasan jeruk limau 3 menit sebelum di angkat.",
        "Koreksi rasa dan sajikan."
      ],
      "source": "https://cookpad.com/id/resep/4358857-ayam-suwir-khas-lombok"
    },
    {
      "id": 958,
      "title": "Ayam goreng super duper simple",
      "image" : "https://img-global.cpcdn.com/recipes/093388469773f7cf/680x964cq70/ayam-goreng-super-duper-simple-foto-resep-utama.webp",
      "loves": 7,
      "ingredients": [
        "Ayam, bisa diganti ati ampela/daging kambing",
        "Asem matang",
        "Garam"
      ],
      "instructions": [
        "Cuci bersih ayam.",
        "Rebus ayam dengan asem dan garam hingga matang.",
        "Goreng hingga kecoklatan😍"
      ],
      "source": "https://cookpad.com/id/resep/4346873-ayam-goreng-super-duper-simple"
    },
    {
      "id": 15574,
      "title": "Tahu tempe telor kecap sederhana",
      "image": "https://img-global.cpcdn.com/recipes/84bfe43010587279/680x964cq70/tahu-tempe-telor-kecap-sederhana-foto-resep-utama.webp",
      "loves": 6,
      "ingredients": [
        "1 Papan Tempe",
        "3 Kotak Tahu (ukuran sedang)",
        "2 Butir Telur ayam rebus (kupas kulitnya)",
        "secukupnya Garam",
        "Secukupnya Gula",
        "Secukupnya Kecap",
        "sesuai selera Merica (optional)",
        "sesuai selera Rajarasa (optional)",
        "sesuai selera Minyak wijen (optional)",
        "2 sdm minyak goreng (utk menumis bumbu iris)",
        "1 gelas Air (secukupnya)",
        "Bumbu yang di iris",
        "5 Siung bawang putih",
        "5 pcs cabe rawit",
        "Secukupnya daun bawang"
      ],
      "instructions": [
        "Potong kecil2 tempe & tahu sesuai selera (me: potong kotak2) sisihkan.",
        "Tumis bawang putih, cabe rawit & daun bawang sampai harum.",
        "Masukan tempe & tahu kedalam tumisan, lalu masukkan rajarasa, minyak wijen, air, garam, gula, merica & kecap.",
        "Cek rasa.",
        "Kemudian masukan telur rebus. Masak hingga air nya menyusut dan telur berwarna kecoklatan.",
        "Hidangkan (me: telur di belah menjadi 2)."
      ],
      "source": "https://cookpad.com/id/resep/3945260-tahu-tempe-telor-kecap-sederhana"
    },
    {
      "id": 1373,
      "title": "Ayam Crispy Lada Hitam Saus Cabai dan Tomat",
      "image": "https://img-global.cpcdn.com/recipes/242e828ce3caf1e5/680x964cq70/ayam-crispy-lada-hitam-saus-cabai-dan-tomat-foto-resep-utama.webp",
      "loves": 2,
      "ingredients": [
        "1 potong dada ayam fillet (150 gr)",
        "1 sdm lada hitam",
        "1 bungkus tepung kobe kentucky super crispy",
        "1 buah bawang bombay uk. kecil (iris)",
        "2 siung bawang putih (geprek dan cincang)",
        "2 buah cabai besar (sesuai selera, potong serong)",
        "Secukupnya saus cabai (pedas sesuai selera)",
        "1 1/2 sdm saus tomat (sesuai selera)",
        "1 sdm margarin",
        "Secukupnya kecap manis",
        "Secukupnya kecap asin",
        "Secukupnya lada hitam",
        "Secukupnya garam, gula (bila perlu)",
        "Secukupnya air dan minyak goreng"
      ],
      "instructions": [
        "Dada ayam fillet dipotong kotak. Cuci bersih. Beri lada hitam. Pijit2, tunggu 15 menit agar lada meresap.",
        "Ambil kurleb 2 sdm tepung kobe beri air. Aduk. Tuang ke wadah ayam lalu aduk rata.",
        "Gulingkan ayam ke tepung kobe, usahakan agar potongan ayam tertutup semua dengan tepung.",
        "Goreng ke dalam minyak panas. Angkat dan tiriskan jika sudah matang dan crispy.",
        "Buat saus: panaskan sedikit minyak, tumis bawang putih sampai harum kemudian masukkan bawang bombay dan potongan cabai, beri 1 sdm margarin dan secukupnya air.",
        "Masukkan saus cabai, saus tomat, kecap asin dan manis serta lada hitam (bisa ditambahkan garam dan gula putih secukupnya, kalau saya tdk pakai). Tes rasa, jika sudah ok, matikan kompor.",
        "Tata ayam crispy pada wadah lalu siram atasnya dengan saus. Enaaaaakk 😍😍. Sajikan selagi hangat agar ayam masih crispy."
      ],
      "source": "https://cookpad.com/id/resep/4358880-ayam-crispy-lada-hitam-saus-cabai-dan-tomat"
    }
  ]
}

const scanImages = async (request, h) => {
    // const token = request.headers.authorization;
    // const decoded = verifyToken(token);
    const { image } = request.payload;

    // if (!decoded) {
    //     return h.response({ status: 'fail', message: 'Unauthorized' }).code(401);
    // }

    try {
        // Validate image exists
        if (!image) {
            return h.response({ status: 'fail', message: 'No image uploaded' }).code(400);
        }

        // const modelPath = path.join(__dirname, '../models/model.json');
        
        const filename = `${decoded.id}_${Date.now()}.jpg`;
        const uploadDir = path.join(__dirname, '../uploads/scanimage');

        // // Ensure upload directory exists
        if (!fs.existsSync(uploadDir)) {
            fs.mkdirSync(uploadDir, { recursive: true });
        }

        const uploadPath = path.join(uploadDir, filename);

        // // Save the uploaded file
        await new Promise((resolve, reject) => {
            const file = fs.createWriteStream(uploadPath);
            image.pipe(file);
            file.on('finish', resolve);
            file.on('error', reject);
        });

        // // Load the model
        // const model = await loadModel(modelPath);   

        // // Predict the image
        // const prediction = await predictImage(model, uploadPath);

        // // Clean up uploaded file
        // fs.unlinkSync(uploadPath);

        // return h.response({ 
        //     status: 'success', 
        //     data: {
        //         classIndex: prediction.classIndex,
        //         className: prediction.className,
        //         confidence: prediction.confidence
        //     } 
        // }).code(200);

        return h.response({
            status: 'success', data: { ingredient: "ayam" }
        }).code(200)
    } catch (error) {
        console.error('Error in image scanning:', error);
        return h.response({ 
            status: 'error', 
            message: error.message 
        }).code(500);
    }
};

const getRecomRecipes = async (request, h) => {
    const { ingredients } = request.payload;
    // const token = request.headers.authorization;
    // const decoded = verifyToken(token);

    
      
    return h.response({ status: 'success', data: dataRecipes }).code(200);

    if (!decoded) {
        return h.response({ status: 'fail', message: 'Unauthorized' }).code(401);
    }
    console.log(dataRecipes.detected_ingredient);

    try {
        // Waiting for Model ML
        // const recipes = await mlModelService.recommendRecipes(ingredients);

        if (ingredients ==  dataRecipes.detected_ingredient) {
        }

    } catch (error) {
        return h.response({ status: 'error', message: error.message }).code(500);
    }
}

const getAllRecipes = async (request, h) => {
    const token = request.headers.authorization;
    const decoded = verifyToken(token);

    if (!decoded) {
        return h.response({ status: 'fail', message: 'Unauthorized' }).code(401);
    }

    try {
        const [rows] = await pool.query('SELECT * FROM recipes');
        if (rows.length === 0) {
            return h.response({ status: 'fail', message: 'Recipes not found' }).code(404);
        }

        const recipes = rows[0];
        return h.response({ status: 'success', data: recipes }).code(200);
    } catch (error) {
        console.error(error.message);
        return h.response({ status: 'fail', message: 'Failed to retrieve recipes' }).code(500);
    }
};

const getRecipe = async (request, h) => {
    // const token = request.headers.authorization;
    // const decoded = verifyToken(token);
    const { recipe_id } = request.payload;
    
    // if (!decoded) {
    //     return h.response({ status: 'fail', message: 'Unauthorized' }).code(401);
    // }


    try {
        // const [rows] = await pool.query('SELECT * FROM recipes WHERE id = ?', [recipe_id]);
        // if (rows.length === 0) {
        //     return h.response({ status: 'fail', message: 'Recipes not found' }).code(404);
        // }

        // const recipe = rows[0];
        const recipe = dataRecipes.recommended_recipes.find(r => r.id == recipe_id);

        return h.response({ status: 'success', data: recipe }).code(200);
    } catch (error) {
        console.error(error.message);
        return h.response({ status: 'fail', message: 'Failed to retrieve recipes' }).code(500);
    }
};

const getRecipebySearch = async (request, h) => {
    const { ingredients } = request.query;
    const ingredientList = ingredients.split(',');

    try {
        // Waiting for Model ML
        // const recipes = await mlModelService.searchRecipesByIngredients(ingredientList);
        
        return h.response({ status: 'success', data: recipes }).code(200);
    } catch (error) {
        return h.response({ status: 'error', message: error.message }).code(500);
    }
};

module.exports = {
    scanImages,
    getRecomRecipes,
    getAllRecipes,
    getRecipe,
    getRecipebySearch
};