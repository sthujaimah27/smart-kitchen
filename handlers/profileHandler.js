const pool = require('../db'); // Koneksi database
const fs = require('fs');
const path = require('path');
const Joi = require('joi');
const { verifyToken } = require('../utils/authUtils');

const getProfileHandler = async (request, h) => {
    // console.log(request.headers.authorization);
    // const token = request.headers.authorization?.split(' ')[1];
    const token = request.headers.authorization;
    const decoded = verifyToken(token);

    if (!decoded) {
        return h.response({ status: 'fail', message: 'Unauthorized' }).code(401);
    }

    try {
        const [rows] = await pool.query('SELECT nama, username, email, profile_picture FROM users WHERE id = ?', [decoded.id]);
        if (rows.length === 0) {
            return h.response({ status: 'fail', message: 'User not found' }).code(404);
        }

        const user = rows[0];
        return h.response({ status: 'success', data: user }).code(200);
    } catch (error) {
        console.error(error.message);
        return h.response({ status: 'fail', message: 'Failed to retrieve profile' }).code(500);
    }
};

const uploadProfilePictureHandler = async (request, h) => {
    // const token = request.headers.authorization?.split(' ')[1];
    const token = request.headers.authorization;
    const decoded = verifyToken(token);

    if (!decoded) {
        return h.response({ status: 'fail', message: 'Unauthorized' }).code(401);
    }

    // Pastikan file ada
    if (!request.payload || !request.payload.file) {
        return h.response({ status: 'fail', message: 'No file uploaded' }).code(400);
    }

    const { file } = request.payload;

    // Validasi format file
    const mimeType = file.hapi.headers['content-type'];
    if (!['image/jpeg', 'image/png'].includes(mimeType)) {
        return h.response({ status: 'fail', message: 'Invalid file type' }).code(400);
    }

    const uploadDir = path.join(__dirname, '../uploads/profiles');
    if (!fs.existsSync(uploadDir)) {
        fs.mkdirSync(uploadDir, { recursive: true });
    }

    const filename = `${decoded.id}_${Date.now()}.jpg`; // Bisa gunakan ekstensi sesuai format
    const filepath = path.join(uploadDir, filename);

    const fileStream = fs.createWriteStream(filepath);
    file.pipe(fileStream);

    const filenameDir = path.join('/uploads/profiles', filename)

    fileStream.on('finish', () => {
        console.log(`File saved to ${filepath}`);
    });

    try {
        await pool.query('UPDATE users SET profile_picture = ? WHERE id = ?', [filenameDir, decoded.id]);
        return h.response({ status: 'success', message: 'Profile picture uploaded successfully' }).code(200);
    } catch (error) {
        console.error(error.message);
        return h.response({ status: 'fail', message: 'Failed to update profile picture' }).code(500);
    }

};

const deleteProfilePictureHandler = async (request, h) => {
    // const token = request.headers.authorization?.split(' ')[1];
    const token = request.headers.authorization
    const decoded = verifyToken(token);

    if (!decoded) {
        return h.response({ status: 'fail', message: 'Unauthorized' }).code(401);
    }

    try {
        const [rows] = await pool.query('SELECT profile_picture FROM users WHERE id = ?', [decoded.id]);
        if (rows.length === 0 || !rows[0].profile_picture) {
            return h.response({ status: 'fail', message: 'No profile picture found' }).code(404);
        }

        const filepath = path.join(__dirname, '..', rows[0].profile_picture);
        fs.unlinkSync(filepath);

        await pool.query('UPDATE users SET profile_picture = NULL WHERE id = ?', [decoded.id]);
        return h.response({ status: 'success', message: 'Profile picture deleted successfully' }).code(200);
    } catch (error) {
        console.error(error.message);
        return h.response({ status: 'fail', message: 'Failed to delete profile picture' }).code(500);
    }
};

const logoutHandler = (request, h) => {
    return h.response({ status: 'success', message: 'Logout successful' }).code(200);
};

module.exports = {
    getProfileHandler,
    uploadProfilePictureHandler,
    deleteProfilePictureHandler,
    logoutHandler,
};
