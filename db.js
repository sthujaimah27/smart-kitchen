const mysql = require('mysql2');
const dotenv = require('dotenv');

// Create a connection pool
const pool = mysql.createPool({
    host: 'localhost', // Update with your MySQL host
    user: 'root', // Update with your MySQL username
    password: '', // Update with your MySQL password
    database: 'smartkitchen_db' // The name of the database you created
});

module.exports = pool.promise();
