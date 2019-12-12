const sanitizeHtml = require('sanitize-html');
const jwt = require('jsonwebtoken');
const bcrypt = require('bcrypt');
const models = require('../../models');

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
        registerStatus: 'existingEmail',
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
      code: 100
    })
  }

  User.findUserByID(userID)
  .then(encrypt)
  .then(create)
  .then(respond)
}

exports.login = (request, response) => {
  console.log(request);
  const userID = sanitizeHtml(request.body.id);//*
  const userPassword = sanitizeHtml(request.body.pw);

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
      code: 200,
      id: user.userID,
      pw: user.userPassword,
      name: user.userName,
      profileString: user.userImage,
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

exports.getGroupList = (req, res) => {

  const userID = sanitizeHtml(request.body.id);//*
  const userPassword = sanitizeHtml(request.body.pw);

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
      code: 200,
      id: user.userID,
      pw: user.userPassword,
      name: user.userName,
      profileString: user.userImage,
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
