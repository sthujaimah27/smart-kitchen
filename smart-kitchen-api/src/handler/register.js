// src/handler/register.js

import Joi from 'joi';
import bcrypt from 'bcrypt';
import { nanoid } from 'nanoid';
import dotenv from 'dotenv';

dotenv.config();

const users = []; // Data pengguna sementara

const registerHandler = async (request, h) => {
    const schema = Joi.object({
        name: Joi.string().min(3).required(),
        username: Joi.string().min(3).required(),
        email: Joi.string().email().required(),
        password: Joi.string().min(6).required(),
        rePassword: Joi.string().valid(Joi.ref('password')).required()
    });

    const { error, value } = schema.validate(request.payload);

    if (error) {
        return h.response({ message: error.details[0].message }).code(400);
    }

    // Cek apakah email sudah ada
    const existingUser = users.find(user => user.email === value.email);
    if (existingUser) {
        return h.response({ message: 'Email already registered' }).code(400);
    }

    // Hash password
    const hashedPassword = await bcrypt.hash(value.password, 10);

    // Buat user baru
    const newUser = {
        id: nanoid(),
        name: value.name,
        username: value.username,
        email: value.email,
        password: hashedPassword,
    };

    // Simpan user baru ke array sementara
    users.push(newUser);

    return h.response({
        message: 'User registered successfully',
        user: {
            id: newUser.id,
            name: newUser.name,
            username: newUser.username,
            email: newUser.email,
        }
    }).code(201);
};

export { registerHandler };
