// src/server.js

import Hapi from '@hapi/hapi';
import dotenv from 'dotenv';
import registerRoutes from './routes/register.js';  // Rute registrasi
import loginRoutes from './routes/login.js';       // Rute login

dotenv.config();

const server = Hapi.server({
    port: 3000,
    host: 'localhost'
});

server.route({
    method: 'GET',
    path: '/',
    handler: (request, h) => {
        return { message: "Welcome to the API" };
    }
});

// Daftarkan rute dari registerRoutes dan loginRoutes
server.route(registerRoutes);
server.route(loginRoutes);

const start = async () => {
    try {
        await server.start();
        console.log('Server running on %s', server.info.uri);
    } catch (err) {
        console.log(err);
    }
};

start();
