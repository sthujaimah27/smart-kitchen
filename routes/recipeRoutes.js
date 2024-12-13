const {
    getAllRecipes,
    getRecipe,
    getRecipebySearch,
    getRecomRecipes,
    scanImages
} = require('../handlers/recipeHandler');

const recipeRoutes = [
    {
        method: 'POST',
        path: '/scan-image',
        options: {
            payload: {
                maxBytes: 10485760, // 10MB
                multipart: {
                    output: 'stream',
                },
                parse: true,
                allow: 'multipart/form-data',
            },
        },
        handler: scanImages,
    },
    {
        method: 'POST',
        path: '/recommend-recipes',
        handler: getRecomRecipes,
    },
    {
        method: 'GET',
        path: '/search-recipes',
        handler: getRecipebySearch,
    },
    {
        method: 'POST',
        path: '/get-recipe',
        handler: getRecipe,
    },
    {
        method: 'GET',
        path: '/get-all-recipes',
        handler: getAllRecipes,
    },
];

module.exports = recipeRoutes;
