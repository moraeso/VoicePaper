const express = require('express');
const bodyParser = require('body-parser');
const morgan = require('morgan');
const memberManageRouter = require('./routes/memberManage/index');
const roomManageRouter = require('./routes/roomManage/index');
const config = require('./javascripts/config');
const models = require('./models');
const fileManageRouter = require('./routes/fileManage/controller');
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


app.use(function(req, res, next) {
    res.header('Access-Control-Allow-Origin', '*');
    res.header('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE');
    res.header('Access-Control-Allow-Headers', 'content-type, x-access-token');
    res.header('Access-Control-Allow-Headers', 'content-type, id');
    next();
});


app.use(morgan('dev'));

app.set('jwt-secret', config.secret);

app.use('/member', memberManageRouter);
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
    <p>Welcome to Voice Paper</p>
    </body>
    </html>
    `)
    //res.json({ message: 'welcome to Vocie Paper!' });
});


app.listen(3333);
console.log('server start');
