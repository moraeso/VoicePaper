const sanitizeHtml = require('sanitize-html');
const jwt = require('jsonwebtoken');
const bcrypt = require('bcrypt');
const models = require('../../models');

const {User} = require('../../models');

const saltRounds = 10;

exports.register = function(request, response) {
  const userID = sanitizeHtml(request.body.ID);
  const userPassword = sanitizeHtml(request.body.password);
  const userName = sanitizeHtml(request.body.name)
  const userImage = sanitizeHtml(request.body.image);
  
  let user;

  const encrypt = (foundResult) => {
    if(foundResult){
      response.json({
        registerStatus: 'existingEmail'
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
      registerStatus: 'registerSuccess'
    })
  }

  User.findUserByID(userID)
  .then(encrypt)
  .then(create)
  .then(respond)
}

exports.login = (request, response) => {
  const userID = sanitizeHtml(request.body.ID);
  const userPassword = sanitizeHtml(request.body.password);
 
  let user;
  let participatingRoomList;

  const query = `
  SELECT room.*, (SELECT count(*) FROM VoicePaper.roomMembership WHERE roomMembership.roomID = room.roomID) as countMember 
  FROM VoicePaper.roomMembership 
  WHERE room.roomID IN (
    SELECT roomID 
    FROM roomMembership 
    WHERE 
  )
  `

  const query2 = `
  SELECT room.*,roomMembership.* , (SELECT count(*) FROM VoicePaper.roomMembership 
  WHERE roomMembership.roomID = room.roomID) as ? 
  FROM VoicePaper.roomMembership JOIN VoicePaper.room 
  ON roomMembership.userID=? AND roomMembership.roomID = room.roomID
  `

  const decrypt = (foundResult) => {
    if(foundResult){
      user = foundResult;

      console.log('****decrypting process****');

      return bcrypt.compareSync(userPassword, foundResult.userPassword)
    } else {
      response.json({
        loginStatus: "invalidEmail"
      })
    }
  }

  const getParticipatingRoomAndCount = (authenticated) => {
    if(authenticated){
      console.log('****participating room getting process****');

      return models.sequelize.query(query2, 
        {
          replacements: ['CountMember', user.userID],
          type: models.sequelize.QueryTypes.SELECT,
          raw: true
        });
    } else {
      response.json({
        loginStatus: "inccorectPassword"
      })
    }
  }

  const createToken = (participatingRoomAndCountResult) => {
    console.log('****token creating process****');

    console.log(participatingRoomAndCountResult);

    participatingRoomList = participatingRoomAndCountResult;

    var token = jwt.sign(
      {
        ID: user.userID,
        name: user.userName
      },
      'secret',
      {
        expiresIn: '1h',
        issuer: 'http://15011066.iptime.org',
        subject: 'userInfo'
      })

    console.log('token successfuly created');

    return token
  }

  const respond = (token) => {
    response.json({
      loginStatus: "success",
      ID: user.userID,
      name: user.userName,
      pw: user.userPassword,
      token: token,
      roomList: participatingRoomList
    })
  }

  User.findUserByID(userID)
  .then(decrypt)
  .then(getParticipatingRoomAndCount)
  .then(createToken)
  .then(respond)
}