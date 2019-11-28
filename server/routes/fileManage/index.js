const express = require('express');
const router = express.Router();
var multer = require('multer');
const path = require('path');
const controller = require('./controller');
const models = require('../../models');



const userimgupload = multer({
  storage: multer.diskStorage({
    destination: function (req, file, cb) {
      cb(null, './userimage');
    },
    filename: function (req, file, cb) {
      cb(null, new Date().valueOf() + path.extname(file.originalname));
    }
  }),
});

const voiceupload = multer({
  storage: multer.diskStorage({
    destination: function (req, file, cb) {
      cb(null, './voice/');
    },
    filename: function (req, file, cb) {
      cb(null, new Date().valueOf() + path.extname(file.originalname));
    }
  }),
});

const groupimgupload = multer({
  storage: multer.diskStorage({
    destination: function (req, file, cb) {
      cb(null, './groupimage/');
    },
    filename: function (req, file, cb) {
      cb(null, new Date().valueOf() + path.extname(file.originalname));
    }
  }),
});

//유식
router.post('/uploaduserimage',userimgupload.single('image'),function(req, res) {
  console.log(__dirname);
  console.log(req.file);
  console.log(req.body.userID);
  console.log(req.file.filename);
  var image= req.file.filename;

  var imagePath='./userimage/'.concat(image);
  console.log(imagePath);

  var query = 'UPDATE user SET userImage=? WhERE userID=?';

  const uploadUserImage = () => {
      console.log('****UPLOAD USER Image process****');
      return models.sequelize.query(query,
        {
          replacements: [imagePath,req.body.userID],
          type: models.sequelize.QueryTypes.UPDATE,
          raw: true
        });
  }


    const respond = () => {
    res.json({msg:'success',
              code:200});
  }

   uploadUserImage()
  .then(respond)

});

router.post('/uploadgroupimage',groupimgupload.single('image'),function(req, res) {
  console.log(__dirname);
  console.log(req.file);
  console.log(req.body.roomID);
  console.log(req.file.filename);
  var image= req.file.filename;

  var imagePath='./groupimage/'.concat(image);
  console.log(imagePath);

  var query = 'UPDATE room SET roomImage=? WhERE roomID=?';

  const uploadGroupImage = () => {
      console.log('****UPLOAD ROOM Image process****');
      return models.sequelize.query(query,
        {
          replacements: [imagePath,req.body.roomID],
          type: models.sequelize.QueryTypes.UPDATE,
          raw: true
        });
  }

    const respond = () => {
    res.json({msg:'success',
              code:200});
  }

   uploadGroupImage()
  .then(respond)

});

router.post('/uploadvoice',voiceupload.single('voice'),function(req, res) {
  console.log(__dirname);
  console.log(req.file);
  console.log(req.body.userID);
  console.log(req.file.filename);
  var voice= req.file.filename;

  var voicePath='./voice/'.concat(voice);
  console.log(voicePath);

  var query = 'INSERT INTO voiceData (userID,roomID,filePath) VALUE(?,?,?);';

  const uploadVoice = () => {
      console.log('****upload Voice process****');
      return models.sequelize.query(query,
        {
          replacements: [req.body.userID,req.body.roomID,voicePath],
          type: models.sequelize.QueryTypes.INSERT,
          raw: true
        });
  }


    const respond = () => {
    res.json({msg:'success',
              code:200});
  }

   uploadVoice()
  .then(respond)

});


router.post('/',function(req,res){
  res.send("hhh");
})

router.post('/testupload', controller.testupload);

module.exports = router;
