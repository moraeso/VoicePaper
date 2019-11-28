const express = require('express');
const bodyParser = require('body-parser');
const morgan = require('morgan');

const authRouter = require('./routes/auth/index');
const htmlRouter = require('./routes/html/html');
const config = require('./javascripts/config');
const models = require('./models');

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

app.use(morgan('dev'));

app.set('jwt-secret', config.secret);

app.use('/auth', authRouter);
app.use('/html', htmlRouter);

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

app.listen(3333);
console.log('server start');
