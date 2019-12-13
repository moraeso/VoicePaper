const sanitizeHtml = require('sanitize-html');
const jwt = require('jsonwebtoken');
const bcrypt = require('bcrypt');
const models = require('../../models');
const auth = require('../../javascripts/auth');


const {User} = require('../../models');
const saltRounds = 10;

exports.getUserInfo=(request,response) =>{

  userID=request.body.userID;

  const respond = (userInfo) => {
  response.json({userInfo:userInfo,
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
  User.findUserByID(userID)
  .catch(function(error){
    response.json({
      msg:'query error occcured',
      code:500
    })
  })
  .then(respond);
}

exports.modifyUserPassword =(request,response)=>{
  newPassword=request.body.newPW;

  let user;

  const decrypt = (foundResult) => {
    user=foundResult;
    console.log('****compare newPW to oldPW****');

    return bcrypt.compareSync(newPassword, foundResult.userPassword)
  }

  const hashUserPassword = (authenticated) => {
    if(authenticated){
      response.json({
        modifyStatus: "old passowrd and new passowrd are same",
        code: 202
      })
    } else {
      return bcrypt.hash(newPassword, saltRounds);
    }//else
  }

  const updateUserTable=(hashedPassword)=>{
    User.update({ userPassword: hashedPassword },
      {
        where: {userID: request.body.userID}
    })
    .catch(function(error){
      response.json({
        msg:'query error occcured',
        code:500
      })
    })
  }

  const respond=()=>{
    response.json({
      msg:'success',
      code:200
    });
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

  User.findUserByID(request.body.userID)
  .then(decrypt)
  .then(hashUserPassword)
  .then(updateUserTable)
  .then(respond)
}
