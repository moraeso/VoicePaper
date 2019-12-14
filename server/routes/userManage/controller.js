const sanitizeHtml = require('sanitize-html');
const jwt = require('jsonwebtoken');
const bcrypt = require('bcrypt');
const models = require('../../models');
const auth = require('../../javascripts/auth');


const {User} = require('../../models');
const saltRounds = 10;

exports.register = function(request, response) {
  const userID = sanitizeHtml(request.body.id);
  const userPassword = sanitizeHtml(request.body.pw);
  const userName = sanitizeHtml(request.body.name)
  const userImage = sanitizeHtml(request.body.profileString);

  let user;

  const encrypt = (foundResult) => {
    if(foundResult != undefined){
      response.json({
        registerStatus: 'existingID',
        code : 101
      })
    }

    return bcrypt.hash(userPassword, saltRounds);
  }

  const create = (hashedPassword) => {
    console.log('****creating process****');

    return User.create({
      userID: userID,
      userPassword: hashedPassword,
      userName: userName,
      userImage: userImage
    })
  }

  const count = (createdResult) => {
    console.log('****counting process****');

    user = createdResult;

    return User.count();
  }

  const assign = (count) => {
    console.log('****assigning process****');
    if(count){
      return User.update({ admin: 1 },
        {
          where: {
            userID: user.userID
          }
      })
    }
  }

  const respond = () => {
    response.json({
      registerStatus: 'registerSuccess',
      code: 200
    })
  }

  User.findUserByID(userID)
  .then(encrypt)
  .then(create)
  .then(respond)
}

exports.login = (request, response) => {
  const userID = sanitizeHtml(request.body.id);//*
  const userPassword = sanitizeHtml(request.body.pw);
  const secret = request.app.get('jwt-secret');

  let user;
  let participatingRoomList;

  const query = `
  SELECT room.*,roomMembership.* , (SELECT count(*) FROM VoicePaper.roomMembership
  WHERE roomMembership.roomID = room.roomID) as ?
  FROM VoicePaper.roomMembership JOIN VoicePaper.room
  ON roomMembership.userID=? AND roomMembership.roomID = room.roomID
  `

  const decrypt = (foundResult) => {
    if(foundResult != undefined){
      user = foundResult;
      console.log('****decrypting process****');

      return bcrypt.compareSync(userPassword, foundResult.userPassword)
    } else {
      response.json({
        loginStatus: "invalidEmail",
        code: 201
      })
      console.log('invalid email');
    }
  }

  const getParticipatingRoomAndCount = (authenticated) => {
    if(authenticated){
      console.log('****participating room getting process****');

      return models.sequelize.query(query,
        {
          replacements: ['CountMember', user.userID],
          type: models.sequelize.QueryTypes.SELECT,
          raw: true
        });
    } else {
      response.json({
        loginStatus: "inccorectPassword",
        code: 202
      })
    }
  }

  const createToken = (participatingRoomAndCountResult) => {
    console.log('****token creating process****');

    participatingRoomList = participatingRoomAndCountResult;

    //console.log("user : " , user);

    var token = jwt.sign(
      {
        ID: user.userID,
        name: user.userName
      },
      secret,
      {
        expiresIn: '5h',
        issuer: 'http://15011066.iptime.org',
        subject: 'userInfo'
      })

    console.log('token successfuly created');

    return token
  }

  const respond = (token) => {
    response.json({
      loginStatus: "success",
      code: 200,
      id: user.userID,
      pw: user.userPassword,
      name: user.userName,
      profileString: user.userImage,
      token: token,
      roomList: participatingRoomList
    })
    console.log('login succeed');
  }

  User.findUserByID(userID)
  .then(decrypt)
  .then(getParticipatingRoomAndCount)
  .then(createToken)
  .then(respond)
}

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
  oldPassword=request.body.oldPW;
  console.log(oldPassword);

  let user;

  const decrypt = (foundResult) => {
    user=foundResult;
    console.log('****compare currentPW to oldPW****');

    return bcrypt.compareSync(oldPassword, foundResult.userPassword)
  }

  const hashUserPassword = (authenticated) => {
    if(!authenticated){
      response.json({
        modifyStatus: "old passowrd and current passowrd are the same",
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
exports.getEnteredRoomList = (request, response) => {

  const query = `
  SELECT room.*,roomMembership.* , (SELECT count(*) FROM VoicePaper.roomMembership
  WHERE roomMembership.roomID = room.roomID) as ?
  FROM VoicePaper.roomMembership JOIN VoicePaper.room
  ON roomMembership.userID=? AND roomMembership.roomID = room.roomID
  `
  const getParticipatingRoomAndCount = () => {
      console.log('****participating room getting process****');
      return models.sequelize.query(query,
        {
          replacements: ['CountMember', request.body.userID],
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

  const respond = (getParticipatingRoomAndCount) => {
    response.json({
              roomList:getParticipatingRoomAndCount,
              code:200
            })
            .catch(function(error){
              response.json({
                msg:'query error occcured',
                code:500
              })
            })
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

  getParticipatingRoomAndCount()
  .then(respond)
}
