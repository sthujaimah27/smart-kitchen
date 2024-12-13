const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');
const Joi = require('joi');
const pool = require('../db'); // Import the database connection
require('dotenv').config();

// Register Handler
const registerHandler = async (request, h) => {
    const { nama, username, email, password, rePassword } = request.payload;

    // Validasi input menggunakan Joi
    const schema = Joi.object({
        nama: Joi.string().min(3).required(),
        username: Joi.string().alphanum().min(3).required(),
        email: Joi.string().email().required(),
        password: Joi.string().min(6).required(),
        rePassword: Joi.valid(Joi.ref('password')).required(),
    });

    const { error } = schema.validate({ nama, username, email, password, rePassword });
    if (error) {
        return h.response({ status: 'fail', message: error.message }).code(400);
    }

    // Cek apakah username/email sudah terdaftar
    const [userExists] = await pool.query('SELECT * FROM users WHERE username = ? OR email = ?', [username, email]);
    if (userExists.length > 0) {
        return h.response({ status: 'fail', message: 'Username atau email sudah digunakan.' }).code(400);
    }

    // Hash password
    const hashedPassword = await bcrypt.hash(password, 10);

    // Tambahkan pengguna baru ke database
    const [newUser] = await pool.query(
        'INSERT INTO users (id, nama, username, email, password) VALUES (?, ?, ?, ?, ?)',
        [require('nanoid').nanoid(), nama, username, email, hashedPassword]
    );

    return h.response({ status: 'success', message: 'Registrasi berhasil!' }).code(201);
};

// Login Handler
const loginHandler = async (request, h) => {
    const { username, password } = request.payload;
    
    // Validasi input menggunakan Joi
    const schema = Joi.object({
        username: Joi.string().required(),
        password: Joi.string().required(),
    });
    
    const { error } = schema.validate({ username, password });
    if (error) {
        return h.response({ status: 'fail', message: error.message }).code(400);
    }

    try {
        // Cari pengguna di database
        const [user] = await pool.query('SELECT * FROM users WHERE username = ?', [username]);
        if (user.length === 0) {
            return h.response({ status: 'fail', message: 'Username tidak ditemukan.' }).code(404);
        }
    
        // Verifikasi password
        const isValidPassword = await bcrypt.compare(password, user[0].password);
        if (!isValidPassword) {
            return h.response({ status: 'fail', message: 'Password salah.' }).code(401);
        }
    
        // Buat JWT token
        const token = jwt.sign({ id: user[0].id, username: user[0].username }, process.env.JWT_SECRET, {
            expiresIn: '1h',
        });
    
        return h.response({ status: 'success', message: 'Login berhasil!', token }).code(200);
    } catch (error) {
        return h.response({ status: 'fail', message: error.message }).code(404);
    }
    
};
module.exports = { registerHandler, loginHandler };
