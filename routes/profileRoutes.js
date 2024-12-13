const {
    getProfileHandler,
    uploadProfilePictureHandler,
    deleteProfilePictureHandler,
    logoutHandler,
} = require('../handlers/profileHandler');

const profileRoutes = [
    {
        method: 'GET',
        path: '/profile',
        handler: getProfileHandler,
    },
    {
        method: 'POST',
        path: '/profile/upload',
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
        handler: uploadProfilePictureHandler,
    },
    {
        method: 'DELETE',
        path: '/profile/delete',
        handler: deleteProfilePictureHandler,
    },
    {
        method: 'POST',
        path: '/logout',
        handler: logoutHandler,
    },
];

module.exports = profileRoutes;
