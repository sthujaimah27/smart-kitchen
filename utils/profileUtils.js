const path = require('path');
const fs = require('fs');
const { nanoid } = require('nanoid');

// Tempat penyimpanan file foto profil
const PROFILE_PIC_DIR = path.join(__dirname, '..', 'profile_pics');

// Membuat folder profile_pics jika belum ada
if (!fs.existsSync(PROFILE_PIC_DIR)) {
  fs.mkdirSync(PROFILE_PIC_DIR);
}

// Fungsi untuk menyimpan foto profil
const saveProfilePic = async (file) => {
  const { filename, payload } = file;
  const fileExtension = path.extname(filename).toLowerCase();
  const allowedExtensions = ['.jpg', '.jpeg', '.png', '.gif'];

  // Cek ekstensi file
  if (!allowedExtensions.includes(fileExtension)) {
    throw new Error('File format not allowed. Only .jpg, .jpeg, .png, .gif are allowed.');
  }

  // Generate unique filename
  const uniqueName = nanoid() + fileExtension;
  const filePath = path.join(PROFILE_PIC_DIR, uniqueName);

  // Simpan file ke direktori
  fs.writeFileSync(filePath, payload);

  return uniqueName;
};

// Fungsi untuk menghapus foto profil
const deleteProfilePic = (fileName) => {
  const filePath = path.join(PROFILE_PIC_DIR, fileName);
  if (fs.existsSync(filePath)) {
    fs.unlinkSync(filePath);
  }
};

// Fungsi untuk melihat foto profil
const getProfilePic = (fileName) => {
  const filePath = path.join(PROFILE_PIC_DIR, fileName);
  if (fs.existsSync(filePath)) {
    return filePath;
  }
  return null;
};

module.exports = {
  saveProfilePic,
  deleteProfilePic,
  getProfilePic
};
