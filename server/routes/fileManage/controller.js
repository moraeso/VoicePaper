const express = require('express');
const router = express.Router();
var multer = require('multer');
const path = require('path');
const controller = require('./controller');
const models = require('../../models');
const auth = require('../../javascripts/auth');

//업로드 후 핸들러 테스트
//multer 모듈에서 사용되는 request는 file과 files뿐임
//클라이언트에서부터 전송되는 request와는 다름.
//handler로 등록이 안됨

router.use('/voice',express.static('./voice'));
router.use('/userimage',express.static('./userimage'));
router.use('/roomimage',express.static('./roomimage'));

const userImgUpload = multer({
  storage: multer.diskStorage({
    destination: function (request, file, cb) {
      cb(null, './userimage/');
    },
    filename: function (request, file, cb) {
      cb(null, new Date().valueOf() + path.extname(file.originalname));
    }
  }),
});

const voiceUpload = multer({
  storage: multer.diskStorage({
    destination: function (request, file, cb) {
      cb(null, './voice/');
    },
    filename: function (request, file, cb) {
      cb(null, new Date().valueOf() + path.extname(file.originalname));
    }
  }),
});

const roomImgUpload = multer({
  storage: multer.diskStorage({
    destination: function (request, file, cb) {
      cb(null, './roomimage/');
    },
    filename: function (request, file, cb) {
      cb(null, new Date().valueOf() + path.extname(file.originalname));
    }
  }),
});

//유식
router.post('/uploaduserimage',userImgUpload.single('image'),function(request, response) {
/*
  console.log(__dirname);
  console.log(request.file);
  console.log(request.body.userID);
  console.log(request.file.filename);
  */
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
              path:imagePath,
              code:200});
  }

   uploadUserImage()
  .then(respond)

});

router.post('/uploadroomimage',roomImgUpload.single('image'),function(request, response) {
  /*
  console.log(__dirname);
  console.log(request.file);
  console.log(request.body.roomID);
  console.log(request.file.filename);
  */
  var image= request.file.filename;

  var imagePath='./roomimage/'.concat(image);
  console.log(imagePath);

  var query = 'UPDATE room SET roomImage=? WhERE roomID=?';

  const uploadRoomImage = () => {
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
              path:imagePath,
              code:200});
  }

   uploadRoomImage()
  .then(respond)

});

router.post('/uploadvoice',voiceUpload.single('voice'),function(request, response) {
  /*
  console.log(__dirname);
  console.log(request.file);
  console.log(request.body.userID);
  console.log(request.file.filename);
  */
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
              path:voicePath,
              code:200});
  }

   uploadVoice()
  .then(respond)
});

//Delete voice data in DB.
router.post('/deletevoice',function(request,response){
    const query = 'DELETE FROM voiceData WHERE roomID=? AND userID=?;'

    const delteVoiceData = () => {
        console.log('**** Delete voice data index ****');
        return models.sequelize.query(query,
          {
            replacements: [request.body.roomID,request.body.userID],
            type: models.sequelize.QueryTypes.DELETE,
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
      response.json({msg:"success",
                code:200});
    }

    auth.isLoggedIn(request, response)
    .then(auth.verifyToken)
    .catch(function(error){
      console.log('unavailable token')
      console.log(error);
      response.json({
        authStatus: 'unavailable tokennn',
        code: 503
      })
      throw new Error('close');
    })
    delteVoiceData()
    .then(respond)
});

router.post('/voicelist',function(request,response){

  const query = 'SELECT voiceData.*, user.userName FROM voiceData join user on user.userID=voiceData.userID WHERE roomID=?;'

  const uploadedVoiceDataList = () => {
      console.log('**** Uploaded Voice Data List Querying process****');
      return models.sequelize.query(query,
        {
          replacements: [request.body.roomID],
          type: models.sequelize.QueryTypes.SELECT,
          raw: true
        })
        .catch(function(error){
          response.json({
            msg:'query error occcured',
            code:500
          })
        })
      }


    const respond = (voiceList) => {
    response.json({voiceList:voiceList,
              code:200});
            }

  auth.isLoggedIn(request, response)
  .then(auth.verifyToken)
  .catch(function(error){
    console.log('unavailable token')
    console.log(error);
    response.json({
        authStatus: 'unavailable tokennn',
        code: 503
      })
    throw new Error('close');
    })

  uploadedVoiceDataList()
  .then(respond)
});



module.exports = router;
