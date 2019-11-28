/*
const express = require('express');
const router = express.Router();
const controller = require('./controller');


var multer = require('multer');
const path = require('path');

const userimgupload = multer({
  storage: multer.diskStorage({
    destination: function (req, file, cb) {
      cb(null, './userimage');
    },
    filename: function (req, file, cb) {
      cb(null, req.body.filename + path.extname(file.originalname));
    }
  }),
});

//유식
router.post('/upload',userimgupload.single('image'),function(req, res) {
  console.log(__dirname);
  console.log(req.file);
  console.log(req.body.userID);
  console.log(req.file.filename);
  var image= req.file.filename;

  var imagePath='./userimage/'.concat(image);
  console.log(imagePath);
  res.send(req.file);
});

*/
