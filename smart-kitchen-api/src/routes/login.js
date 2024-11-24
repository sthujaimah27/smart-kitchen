// src/routes/login.js

import { loginHandler } from '../handler/login.js';

const loginRoutes = [
    {
        method: 'POST',
        path: '/login',
        handler: loginHandler,
    },
];

export default loginRoutes;
