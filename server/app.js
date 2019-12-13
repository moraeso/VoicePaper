const express = require('express');
const bodyParser = require('body-parser');
const morgan = require('morgan');
const authRouter = require('./routes/auth/index');
const htmlRouter = require('./routes/html/html');
const roomManageRouter = require('./routes/roomManage/index');
const config = require('./javascripts/config');
const models = require('./models');
const fileManageRouter = require('./routes/fileManage/index');
const userManageRouter=require('./routes/userManage/index');

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
app.use('/user',userManageRouter);

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



app.listen(3333);
console.log('server start');
