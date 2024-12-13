const { registerHandler, loginHandler, loginHandlerv2 } = require('../handlers/authHandler');

const authRoutes = [
    {
        method: 'POST',
        path: '/register',
        handler: registerHandler,
    },
    {
        method: 'POST',
        path: '/login',
        handler: loginHandler,
    },
];

module.exports = authRoutes;
