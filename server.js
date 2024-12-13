const Hapi = require('@hapi/hapi');
const Inert = require('@hapi/inert'); // Plugin untuk handling file
const path = require('path');
const fs = require('fs');
const dotenv = require('dotenv');
const authRoutes = require('./routes/authRoutes');
const profileRoutes = require('./routes/profileRoutes');
const recipeRoutes = require('./routes/recipeRoutes');

dotenv.config(); // Load environment variables

// Pastikan direktori uploads tersedia
const uploadDir = path.join(__dirname, 'uploads');
if (!fs.existsSync(uploadDir)) {
    fs.mkdirSync(uploadDir);
    console.log(`Created upload directory at ${uploadDir}`);
}

const init = async () => {
    const server = Hapi.server({
        port: process.env.PORT || 5000,
        host: 'localhost',
    });

    // Registrasi plugin Inert (jika dibutuhkan untuk file handling)
    await server.register(Inert);

    // Tambahkan routes
    server.route([...authRoutes, ...profileRoutes, ...recipeRoutes]);

    // Menjalankan server
    try {
        await server.start();
        console.log(`Server running on ${server.info.uri}`);
    } catch (err) {
        console.error('Failed to start server:', err.message);
        process.exit(1);
    }
};

// Handle unhandled promise rejections
process.on('unhandledRejection', (err) => {
    console.error('Unhandled Rejection:', err.message);
    process.exit(1);
});

init();
