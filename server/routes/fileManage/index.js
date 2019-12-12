const express = require('express');
const router = express.Router();
var multer = require('multer');
const path = require('path');
const controller = require('./controller');
const models = require('../../models');


const userimgupload = multer({
  storage: multer.diskStorage({
    destination: function (request, file, cb) {
      cb(null, './userimage');
    },
    filename: function (request, file, cb) {
      cb(null, new Date().valueOf() + path.extname(file.originalname));
    }
  }),
});

const voiceupload = multer({
  storage: multer.diskStorage({
    destination: function (request, file, cb) {
      cb(null, './voice/');
    },
    filename: function (request, file, cb) {
      cb(null, new Date().valueOf() + path.extname(file.originalname));
    }
  }),
});

const groupimgupload = multer({
  storage: multer.diskStorage({
    destination: function (request, file, cb) {
      cb(null, './groupimage/');
    },
    filename: function (request, file, cb) {
      cb(null, new Date().valueOf() + path.extname(file.originalname));
    }
  }),
});

//유식
router.post('/uploaduserimage',userimgupload.single('image'),function(request, response) {
  console.log(__dirname);
  console.log(request.file);
  console.log(request.body.userID);
  console.log(request.file.filename);
  var image= request.file.filename;

  var imagePath='./userimage/'.concat(image);
  console.log(imagePath);

  var query = 'UPDATE user SET userImage=? WhERE userID=?';

  const uploadUserImage = () => {
      console.log('****UPLOAD USER Image process****');
      return models.sequelize.query(query,
        {
          replacements: [imagePath,request.body.userID],
          type: models.sequelize.QueryTypes.UPDATE,
          raw: true
        })
        .catch(function(error){
          response.json({
            msg:'query error occcured',
            code:500
          })
        })
  }


    const respond = () => {
    response.json({msg:'success',
              code:200});
  }

   uploadUserImage()
  .then(respond)

});

router.post('/uploadgroupimage',groupimgupload.single('image'),function(request, response) {
  console.log(__dirname);
  console.log(request.file);
  console.log(request.body.roomID);
  console.log(request.file.filename);
  var image= request.file.filename;

  var imagePath='./groupimage/'.concat(image);
  console.log(imagePath);

  var query = 'UPDATE room SET roomImage=? WhERE roomID=?';

  const uploadGroupImage = () => {
      console.log('****UPLOAD ROOM Image process****');
      return models.sequelize.query(query,
        {
          replacements: [imagePath,request.body.roomID],
          type: models.sequelize.QueryTypes.UPDATE,
          raw: true
        })
        .catch(function(error){
          response.json({
            msg:'query error occcured',
            code:500
          })
        })
  }

    const respond = () => {
    response.json({msg:'success',
              code:200});
  }

   uploadGroupImage()
  .then(respond)

});

router.post('/uploadvoice',voiceupload.single('voice'),function(request, response) {
  console.log(__dirname);
  console.log(request.file);
  console.log(request.body.userID);
  console.log(request.file.filename);
  var voice= request.file.filename;

  var voicePath='./voice/'.concat(voice);
  console.log(voicePath);

  var query = 'INSERT INTO voiceData (userID,roomID,filePath) VALUE(?,?,?);';

  const uploadVoice = () => {
      console.log('****upload Voice process****');
      return models.sequelize.query(query,
        {
          replacements: [request.body.userID,request.body.roomID,voicePath],
          type: models.sequelize.QueryTypes.INSERT,
          raw: true
        })
        .catch(function(error){
          response.json({
            msg:'query error occcured',
            code:500
          })
        })
  }


    const respond = () => {
    response.json({msg:'success',
              code:200});
  }

   uploadVoice()
  .then(respond)

});


module.exports = router;
