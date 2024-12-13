const mysql = require('mysql2');
const dotenv = require('dotenv');

// Create a connection pool
const pool = mysql.createPool({
    host: process.env.HOST_DB, // Update with your MySQL host
    user: process.env.USER_DB, // Update with your MySQL username
    password: process.env.PASS_DB, // Update with your MySQL password
    database: process.env.NAME_DB // The name of the database you created
});

module.exports = pool.promise();
