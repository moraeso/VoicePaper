const express = require('express');
const bodyParser = require('body-parser');
const morgan = require('morgan');
const authRouter = require('./routes/auth/index');
const htmlRouter = require('./routes/html/html');
const roomManageRouter = require('./routes/roomManage/index');
const config = require('./javascripts/config');
const models = require('./models');
const fileManageRouter = require('./routes/fileManage/index')
////////2019.11.25 수정
var mysql = require('mysql');
var dbconfig = require('./config/dbconfig.js');
var fs =require('fs');
var multer = require('multer');
const path = require('path');

////////2019.11.25 수정

const app = express();

models.sequelize.sync()
    .then(() => {
        console.log('✓ DB connection success.');
        console.log(' Press CTRL-C to stop\n');
    })
    .catch(err => {
        console.error(err);
        console.log('✗ DB connection error. Please make sure DB is running');
        process.exit();
    });


/////////////2019.11.25 수정
    const uservoiceupload = multer({
      storage: multer.diskStorage({
        destination: function (req, file, cb) {
          cb(null, './voice/');
        },
        filename: function (req, file, cb) {
          cb(null, req.body.filename + path.extname(file.originalname));
        }
      }),
    });

    const userimgupload = multer({
      storage: multer.diskStorage({
        destination: function (req, file, cb) {
          cb(null, './userimage/');
        },
        filename: function (req, file, cb) {
          cb(null, req.body.filename + path.extname(file.originalname));
        }
      }),
    });
    const groupimgupload = multer({
      storage: multer.diskStorage({
        destination: function (req, file, cb) {
          cb(null, './groupimage/');
        },
        filename: function (req, file, cb) {
          cb(null, req.body.filename + path.extname(file.originalname));
        }
      }),
    });
/////////////2019.11.25 수정

app.use(bodyParser.urlencoded({extended: false}));
app.use(bodyParser.json());

///////////////2019.11.25 수정
app.use(function(req, res, next) {
    res.header('Access-Control-Allow-Origin', '*');
    res.header('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE');
    res.header('Access-Control-Allow-Headers', 'content-type, x-access-token');
    res.header('Access-Control-Allow-Headers', 'content-type, id');
    next();
});
////////////////////2019.11.25 수정


app.use(morgan('dev'));

app.set('jwt-secret', config.secret);

app.use('/auth', authRouter);
app.use('/html', htmlRouter);
app.use('/room', roomManageRouter);
app.use('/file',fileManageRouter);

// test route
app.get('/', function(req, res) {
    res.send(`
    <html>
    <head>
        <title>default page</title>
    </head>
    <body>
    <p>Voice Paper</p>
    <a href='/html/login'>start app</a>
    </body>
    </html>
    `)
    //res.json({ message: 'welcome to Vocie Paper!' });
});


////////////////////2019.11.25 수정
app.use('/voicefile', express.static('./voice'));
app.use('/userimage', express.static('./userimage'));
app.use('/groupimage', express.static('./groupimage'));

app.use('/static', express.static('./'));
// streaming test
app.get('/stream', function(req, res) {
    //1. stream 파일 생성
      var stream = fs.createReadStream('./testmusic4.mp3'); //path => <real voice file path
    // 2. 잘게 쪼개진 stream 이 몇번 전송되는지 확인하기 위한 count
    var count = 0;
    // 3. 잘게 쪼개진 data를 전송할 수 있으면 data 이벤트 발생
    stream.on('data', function(data) {
    count = count + 1;
    console.log('data count='+count);
    // 3.1. data 이벤트가 발생되면 해당 data를 클라이언트로 전송
    res.write(data);
  });

    stream.on('end', function () {
      console.log('end streaming');
      // 4.1. 클라이언트에 전송완료를 알림
      res.end();
    });

});// get '/'



//group list query test
app.post('/grouplist', (req, res) => {
//  console.log(req);
  var connection=mysql.createConnection(dbconfig);

  connection.connect(function(err){
    if(!err) {
        console.log("Database is connected ...");
    }
    else {
        console.log("Error connecting database ...");
    }
  }); //database connect

    var sql = 'SELECT room.*,roomMembership.* , (SELECT count(*) FROM VoicePaper.roomMembership WHERE roomMembership.roomID = room.roomID) as ? FROM VoicePaper.roomMembership JOIN VoicePaper.room ON roomMembership.userID=? AND roomMembership.roomID = room.roomID;';
    var item = ['CountMember',req.body.userID]; //test=>req.body.userID
    sql = mysql.format(sql, item);

    connection.query(sql, function(err, results, field){
      res.send(results);
    });//query

    connection.end();
});// /querytest

//member list query test
app.post('/memberlist', (req, res) => {
//  console.log(req);
  var connection=mysql.createConnection(dbconfig);

  connection.connect(function(err){
    if(!err) {
        console.log("Database is connected ...");
    }
    else {
        console.log("Error connecting database ...");
    }
  }); //database connect

    var sql = 'select roomMembership.roomID,roomMembership.userID, user.userName from roomMembership JOIN user on roomMembership.userID=user.userID where roomMembership.roomID=?';
    var item = [req.body.roomID]; //1=>req.body.roomID
    sql = mysql.format(sql, item);

    connection.query(sql, function(err, results, field){
      res.send(results);
    });//query

    connection.end();
});// /memberlistquerytest



//group exit query test
app.post('/groupexit', (req, res) => {
//  console.log(req);
  var connection=mysql.createConnection(dbconfig);

  connection.connect(function(err){
    if(!err) {
        console.log("Database is connected ...");
    }
    else {
        console.log("Error connecting database ...");
    }
  }); //database connect

    var sql = 'DELETE roomMembership, voiceData FROM roomMembership LEFT JOIN voiceData ON roomMembership.userID = voiceData.userID AND roomMembership.roomID=voiceData.roomID WHERE roomMembership.userID=? AND roomMembership.roomID=?'

    var item = [req.body.userID,req.body.roomID];//=>req.body.userID,req.body.roomID
    sql = mysql.format(sql, item);

    connection.query(sql, function(err, results, field){
      if(!err){
        console.log(results);
        res.send({code : 200});
      }
      else{
        console.log("group exit err");
        res.send({code : 400});
        console.log(err);
      }
    });//query

    connection.end();
});// /gruopexitquerytest

//delete file query test
app.post('/deletevoicefile', (req, res) => {
//  console.log(req);
  var connection=mysql.createConnection(dbconfig);

  connection.connect(function(err){
    if(!err) {
        console.log("Database is connected ...");
    }
    else {
        console.log("Error connecting database ...");
    }
  }); //database connect

    var sql = 'DELETE FROM voiceData WHERE roomID=? AND userID=?;';
    var item = [req.body.roomID,req.body.userID]; //=> req.body.roomID, req.body.userID
    sql = mysql.format(sql, item);

    connection.query(sql, function(err, results, field){
      if(!err){
        console.log(results);
        res.send({code : 200});
      }
      else{
        console.log("delete voice file err");
        res.send({code : 400});
        console.log(err);
      }
    });//query

    connection.end();
});// /deletefilequerytest

// voice data list
app.post('/voicedatalist', (req, res) => {
//  console.log(req);
  var connection=mysql.createConnection(dbconfig);

  connection.connect(function(err){
    if(!err) {
        console.log("Database is connected ...");
    }
    else {
        console.log("Error connecting database ...");
    }
  }); //database connect

    var sql = 'SELECT * FROM voiceData WHERE roomID=?;';
    var item = [req.body.roomID];//=> req.body.roomID
    sql = mysql.format(sql, item);

    connection.query(sql, function(err, results, field){
        console.log(results);
        res.send(results);
    });//query

    connection.end();
});// /voicedataquerytest

//upload user img
app.post('/userimgupload',userimgupload.single('image'), (req, res) => {
//  console.log(req);
console.log(req.file);
console.log(req.body.id);
console.log(req.file.filename);
var id = req.body.id;
var image= req.file.filename;
var connection=mysql.createConnection(dbconfig);

var imagePath='./image/'.concat(image);

  var connection=mysql.createConnection(dbconfig);

  connection.connect(function(err){
    if(!err) {
        console.log("Database is connected ...");
    }
    else {
        console.log("Error connecting database ...");
    }
  }); //database connect

    var sql = 'UPDATE user SET userImage=? WhERE userID=?';
    var item = [imagePath,req.body.userID];//=>real image file path, req.body.userID
    sql = mysql.format(sql, item);

    connection.query(sql, function(err, results, field){
      if(!err){
        console.log(results);
        res.send({code : 200});
      }
      else{
        console.log("user img upload err");
        res.send({code : 400});
        console.log(err);
      }
    });//query

    connection.end();
});// /uploaduserimgquerytest

//upload user img
app.post('/groupimgupload',groupimgupload.single('image'), (req, res) => {
//  console.log(req);
console.log(req.file);
console.log(req.body.id);
console.log(req.file.filename);
var id = req.body.id;
var image= req.file.filename;
var connection=mysql.createConnection(dbconfig);

var imagePath='./groupimage/'.concat(image);

  var connection=mysql.createConnection(dbconfig);

  connection.connect(function(err){
    if(!err) {
        console.log("Database is connected ...");
    }
    else {
        console.log("Error connecting database ...");
    }
  }); //database connect

    var sql = 'UPDATE room SET roomImage=? WhERE roomID=?';
    var item = [imagePath,req.body.roomID];//=>real image file path, req.body.userID
    sql = mysql.format(sql, item);

    connection.query(sql, function(err, results, field){
      if(!err){
        console.log(results);
        res.send({code : 200});
      }
      else{
        console.log("group img upload err");
        res.send({code : 400});
        console.log(err);
      }
    });//query

    connection.end();
});// /uploaduserimgquerytest


app.post('/uservoiceupload', uservoiceupload.single('voice'), (req, res) => {
  console.log(req.file);
  console.log(req.body.id);
  console.log(req.file.filename);
  var id = req.body.id;
  var voice= req.file.filename;
  var connection=mysql.createConnection(dbconfig);

  var voicePath='./voice/'.concat(voice);

  connection.connect(function(err){
    if(!err) {
        console.log("Database is connected ...");
    }
    else {
        console.log("Error connecting database ...");
    }
  }); //database connect

  var sql = 'INSERT INTO voiceData (userID,roomID,filePath) VALUE(?,?,?);';
  var item = [req.body.userID,req.body.roomID,voicePath];//=> req.body.userID, req.body.roomID, <real filePath>
  sql = mysql.format(sql, item);

  connection.query(sql, function(err, results, field){
    if(!err){
      console.log(results);
      res.send({code : 200});
    }
    else{
      console.log(err);
      console.log("upload err");
      res.send({code : 400});
    }
  });//query

  connection.end();

});
////////////////////2019.11.25 수정

app.listen(3333);
console.log('server start');
