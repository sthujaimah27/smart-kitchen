// src/handler/login.js

import Joi from 'joi';
import bcrypt from 'bcrypt';
import jwt from 'jsonwebtoken';
import dotenv from 'dotenv';

dotenv.config();

const users = []; // Data pengguna sementara

const loginHandler = async (request, h) => {
    const schema = Joi.object({
        email: Joi.string().email().required(),
        password: Joi.string().min(6).required(),
    });

    const { error, value } = schema.validate(request.payload);

    if (error) {
        return h.response({ message: error.details[0].message }).code(400);
    }

    // Cari pengguna berdasarkan email
    const user = users.find(user => user.email === value.email);
    if (!user) {
        return h.response({ message: 'Invalid credentials' }).code(401);
    }

    // Verifikasi password
    const isValidPassword = await bcrypt.compare(value.password, user.password);
    if (!isValidPassword) {
        return h.response({ message: 'Invalid credentials' }).code(401);
    }

    // Buat token JWT
    const token = jwt.sign({ id: user.id }, process.env.JWT_SECRET, { expiresIn: '1h' });

    return h.response({
        message: 'Login successful',
        token,
    }).code(200);
};

export { loginHandler };
