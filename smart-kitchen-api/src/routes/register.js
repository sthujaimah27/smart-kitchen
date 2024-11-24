// src/routes/register.js

import { registerHandler } from '../handler/register.js';  // Pastikan path handler benar

const registerRoutes = [
    {
        method: 'POST',
        path: '/register',
        handler: registerHandler,
    },
];

export default registerRoutes;  // Menggunakan export default untuk ekspor ES Modules
