var express = require("express");
var bodyParser = require('body-parser');
var mysql      = require('mysql');
var dbconfig = require('./config/dbconfig.js');
var fs =require('fs');
//resource
/*********file upload***************/

/*********file upload***************/

var app = express();
app.use( bodyParser.urlencoded({ extended: true }) );
app.use( bodyParser.json() );


app.use(function(req, res, next) {
    res.header('Access-Control-Allow-Origin', '*');
    res.header('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE');
    res.header('Access-Control-Allow-Headers', 'content-type, x-access-token');
    res.header('Access-Control-Allow-Headers', 'content-type, id');
    next();
});

app.use('/static', express.static('./'));
// streaming test
app.get('/', function(req, res) {
    //stream 파일 생성
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
app.get('/grouplistquerytest', (req, res) => {
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
    var item = ['CountMember','test']; //test=>req.body.userID
    sql = mysql.format(sql, item);

    connection.query(sql, function(err, results, field){
      res.send(results);
    });//query

    connection.end();
});// /querytest

//member list query test
app.get('/memberlistquerytest', (req, res) => {
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

    var sql = 'SELECT userID,roomID FROM roomMembership WHERE roomID=?';
    var item = [1]; //1=>req.body.roomID
    sql = mysql.format(sql, item);

    connection.query(sql, function(err, results, field){
      res.send(results);
    });//query

    connection.end();
});// /memberlistquerytest

//upload file quert test
app.get('/uploadfilequerytest', (req, res) => {
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

    var sql = 'INSERT INTO voiceData (userID,roomID,filePath) VALUE(?,?,?);';
    var item = ['test',1,'testPath'];//=> req.body.userID, req.body.roomID, <real filePath>
    sql = mysql.format(sql, item);

    connection.query(sql, function(err, results, field){
      if(!err){
        console.log(results);
        res.send({code : 200});
      }
      else{
        console.log("upload err");
        res.send({code : 400});
      }
    });//query

    connection.end();
});// /uploadfilequerytest

//group exit query test
app.get('/groupexitquerytest', (req, res) => {
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

    var sql = 'DELETE roomMembership, voiceData FROM roomMembership LEFT JOIN voiceData ON roomMembership.userID = voiceData.userID AND roomMembership.roomID=voiceData.roomID WHERE roomMembership.userID=? AND roomMembership.roomID=?;';
    var item = ['test',1];//=>req.body.userID,req.body.roomID
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
app.get('/deletefilequerytest', (req, res) => {
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
    var item = [1,'test']; //=> req.body.roomID, req.body.userID
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
app.get('/voicedataquerytest', (req, res) => {
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
    var item = [1];//=> req.body.roomID
    sql = mysql.format(sql, item);

    connection.query(sql, function(err, results, field){
        console.log(results);
        res.send(results);
    });//query

    connection.end();
});// /voicedataquerytest

//upload user img
app.get('/uploaduserimgquerytest', (req, res) => {
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

    var sql = 'UPDATE user SET userImage=? WhERE userID=?';
    var item = ['test query','a'];//=>real image file path, req.body.userID
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


//test port
app.listen(8123);
console.log('server start');
