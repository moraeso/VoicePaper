const express = require('express');
const router = express.Router();
const sanitizeHtml = require('sanitize-html');

router.get('/', function(request, response){
  const html = `
  <p>This is the authenticaiton main page</p>
  <a href='/html/register'>register</a>
  <a href='/html/login'>log in</a>
  `

  response.send(html);
});

router.get('/register', function(request, response){
  const html = `
  <html>
  <head>
    <title>register Page</title>
  </head>
  <body>
    <form action='/auth/register' name='register' method='post'>
    <input type='text' name='ID' placeholder='ID'>
    <input type='password' name='password' placeholder='password'>
    <input type='text' name='name' placeholder='name'>
    <input type='text' name='image' placeholder='image'>
    <input type='submit' value='register'>
  </body>
  </html>
  `
  response.send(html);
});


router.get('/login', function(request, response){
  const html = `
    <html>
    <head>
      <title>login Page</title>
    </head>
    <body>
      <form action='/auth/login' name='login' method='post'>
      <input type='text' name='ID' placeholder='ID'>
      <input type='password' name='password' placeholder='password'>
      <input type='submit' value='log in'>
      <a href='/html/register'>register</a>
    </body>
    </html>
  `
  response.send(html);
});

router.get('/main', function(request, response){
  const userID = sanitizeHtml(request.body.ID);

  const html = `
  <html>
  <head>
    <title>main page</title>
  </head>
  <body>
    <h2>Welcome ${userID}<h2>
    <a href="/room/roomCreate">create room</a>
    <a href="/room/roomParticipate">participate room</a>
  </body>
  </html>
  `
})

module.exports = router;