const Hapi = require('@hapi/hapi');
const Joi = require('@hapi/joi');
const fs = require('fs');
const path = require('path');

// Simulated ML Model Service (would be replaced with actual ML integration)
class MLModelService {
    async scanImage(imagePath) {
        // Simulated image scanning logic
        // In a real scenario, this would call a computer vision model
        const ingredients = ['ayam', 'wortel', 'kentang'];
        return {
            detectedIngredients: ingredients,
            confidence: 0.85
        };
    }

    async recommendRecipes(ingredients) {
        // Simulated recipe recommendation logic
        // In a real scenario, this would query an ML model or database
        return [
            {
                id: 'recipe1',
                name: 'Ayam Goreng Sayur',
                mainIngredients: ingredients,
                difficulty: 'Mudah'
            },
            {
                id: 'recipe2',
                name: 'Sup Ayam Sayur',
                mainIngredients: ingredients,
                difficulty: 'Sedang'
            }
        ];
    }

    async searchRecipesByIngredients(ingredients) {
        // Simulated search logic
        return [
            {
                id: 'recipe3',
                name: 'Ayam Crispy',
                ingredients: ingredients,
                difficulty: 'Mudah'
            }
        ];
    }
}

const init = async () => {
    const server = Hapi.server({
        port: 3000,
        host: 'localhost',
        routes: {
            cors: true,
            payload: {
                maxBytes: 10 * 1024 * 1024, // 10MB max file size
                multipart: true
            }
        }
    });

    const mlModelService = new MLModelService();

    // Image Scanning API
    server.route({
        method: 'POST',
        path: '/api/scan-image',
        options: {
            payload: {
                output: 'stream',
                parse: true,
                multipart: true
            },
            validate: {
                payload: Joi.object({
                    image: Joi.any().meta({ swaggerType: 'file' }).required()
                })
            }
        },
        const scani async (request, h) => {
            const { image } = request.payload;
            
            // Save uploaded image temporarily
            const filename = `${Date.now()}_${image.hapi.filename}`;
            const uploadPath = path.join(__dirname, 'uploads', filename);
            
            const file = fs.createWriteStream(uploadPath);
            image.pipe(file);

            try {
                const scanResult = await mlModelService.scanImage(uploadPath);
                
                // Clean up uploaded file
                fs.unlinkSync(uploadPath);

                return {
                    status: 'success',
                    data: scanResult
                };
            } catch (error) {
                return h.response({
                    status: 'error',
                    message: error.message
                }).code(500);
            }
        }
    });

    // Recipe Recommendation API
    server.route({
        method: 'POST',
        path: '/api/recommend-recipes',
        options: {
            validate: {
                payload: Joi.object({
                    ingredients: Joi.array().items(Joi.string()).required()
                })
            }
        },
        handler: async (request, h) => {
            const { ingredients } = request.payload;

            try {
                const recipes = await mlModelService.recommendRecipes(ingredients);
                return {
                    status: 'success',
                    data: recipes
                };
            } catch (error) {
                return h.response({
                    status: 'error',
                    message: error.message
                }).code(500);
            }
        }
    });

    // Recipe Detail API
    server.route({
        method: 'GET',
        path: '/api/recipe/{recipeId}',
        handler: async (request, h) => {
            const { recipeId } = request.params;

            // Simulated recipe detail retrieval
            const recipeDetails = {
                id: recipeId,
                name: 'Ayam Goreng Sayur',
                ingredients: ['ayam', 'wortel', 'kentang'],
                steps: [
                    'Potong ayam',
                    'Siapkan bumbu',
                    'Goreng ayam'
                ],
                cookingTime: '30 menit',
                difficulty: 'Mudah'
            };

            return {
                status: 'success',
                data: recipeDetails
            };
        }
    });

    // Recipe Search API
    server.route({
        method: 'GET',
        path: '/api/search-recipes',
        options: {
            validate: {
                query: Joi.object({
                    ingredients: Joi.string().required()
                })
            }
        },
        handler: async (request, h) => {
            const { ingredients } = request.query;
            const ingredientList = ingredients.split(',');

            try {
                const recipes = await mlModelService.searchRecipesByIngredients(ingredientList);
                return {
                    status: 'success',
                    data: recipes
                };
            } catch (error) {
                return h.response({
                    status: 'error',
                    message: error.message
                }).code(500);
            }
        }
    });

    await server.start();
    console.log(`Server running on ${server.info.uri}`);
};

process.on('unhandledRejection', (err) => {
    console.log(err);
    process.exit(1);
});

init();

// Dependency installation instructions
// npm install @hapi/hapi @hapi/joi