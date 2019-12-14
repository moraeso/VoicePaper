const sanitizeHtml = require('sanitize-html');
const jwt = require('jsonwebtoken');
const bcrypt = require('bcrypt');
const models = require('../../models');
const auth = require('../../javascripts/auth');

const {User} = require('../../models');
const {Room} = require('../../models');
const {RoomMembership} = require('../../models');

const saltRounds = 10;

exports.roomParticipate = (request, response) => {
  const userID = sanitizeHtml(request.body.userID);
  console.log("userID: ", userID);
  const roomCode = sanitizeHtml(request.body.roomCode);
  let room;

  const isHost = (checkRoomResult) => {

    room = checkRoomResult;

    if(checkRoomResult != undefined){
      if(checkRoomResult.hostID != userID){
        console.log("ok, you are not the host");
        return 1;
      } else {
        response.json({
          roomParticipateStatus: "youAreTheHost",
          code: 401
        })
      }
    } else {
      response.json({
        roomParticipateStatus: "noRoomFound",
        code: 402
      })
    }
  }

  const checkIfAlreadyParticipating = (hostCheckResult) => {
    if(hostCheckResult){
      return RoomMembership.findParticipatingUserByID(userID, room.roomID);
    }
  }

  const create = (checkIfAlreadyParticipatingResult) => {
    console.log('checkIfAlreadyParticipatingResult', checkIfAlreadyParticipatingResult);

    if(checkIfAlreadyParticipatingResult == null){
      return RoomMembership.create({
        userID: userID,
        roomID: room.roomID
      })
    } else {
      response.json({
        roomParticipateStatus: "alreadyParticipating",
        code: 403
      })
    }
  }

  const getMemberCount = (roomParticipateResult) => {
  //  console.log('roomParticipateResult', roomParticipateResult);
    if(roomParticipateResult != undefined) {
      return RoomMembership.countRoomMember(roomParticipateResult.roomID);
    } else {
      response.json({
        roomParticipateStatus: "roomParticipateFailed",
        code: 404
      })
    }
  }

  const respond = (roomMemberCountResult) => {

    var roomInfo = {
      id: room.roomID,
      roomName: room.roomName,
      profileString: room.roomImage,
      roomText: room.roomText,
      userID: room.userID,
      memberCount: roomMemberCountResult,
      roomPermission: room.roomPermission,
      hostID:room.hostID
    }

    if(roomMemberCountResult){
      response.json({
        roomParticipateStatus: "roomParticipateSuccess",
        Room: roomInfo,
        code: 200
      });
    } else {
      response.json({
        roomParticipateStatus: "roomParticipateFailed",
        code: 404
      })
    }
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

  Room.findRoomByRoomCode(roomCode)
  .then(isHost)
  .then(checkIfAlreadyParticipating)
  .then(create)
  .then(getMemberCount)
  .then(respond)

}

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

exports.getMemberList = function(request, response) {
//  console.log(request);

    var sql = 'select roomMembership.roomID,roomMembership.userID, user.userName from roomMembership JOIN user on roomMembership.userID=user.userID where roomMembership.roomID=?';
    var item = [request.body.roomID]; //1=>request.body.roomID

    const getMemberlist=()=>{
      return models.sequelize.query(sql,
      {
        replacements: item,
        type:models.sequelize.QueryTypes.SELECT,
        raw:true
      })
      .catch(function(error){
        response.json({
          msg:'query error occcured',
          code:500
        })
      })
    }

    const respond =(getMemberlist)=>{
      response.json({memberlist:getMemberlist,
                code : 200});
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

    getMemberlist()
    .then(respond);
}// getMemberList getMem

exports.leaveRoom = (request, response) => {
  const notHostQuery =`
                DELETE roomMembership, voiceData
                FROM roomMembership
                LEFT JOIN voiceData ON roomMembership.userID = voiceData.userID
                AND roomMembership.roomID=voiceData.roomID
                WHERE roomMembership.userID=? AND roomMembership.roomID=?;
                `;

  const hostQuery1='DELETE FROM voiceData WHERE roomID=?'; //음성정보먼저 지우고
  const hostQuery2='DELETE FROM roomMembership WHERE roomID=?'; //참여자 지우고
  const hostQuery3='DELETE FROM room WHERE roomID=?'; //방을지운다.

  const isHost = (checkRoomResult) => {

    room = checkRoomResult;
    if(checkRoomResult != undefined){
      if(checkRoomResult.hostID != request.body.userID){
        return true;
      } else {
        return false;
        }
    } else {
      response.json({
        roomParticipateStatus: "noRoomFound",
        code: 402
      })
    }
  }


  const exitRoom = (host) => {
      console.log('**** EXIT ROOM process****');
      if(host){
        console.log("not host");
        return models.sequelize.query(notHostQuery,
          {
            replacements: [request.body.userID,request.body.roomID],
            type: models.sequelize.QueryTypes.DELETE,
            raw: true
          })
          .catch(function(error){
            response.json({
              msg:'query error occcured',
              code:500
            })
          })
      } else {
        console.log("host");
        models.sequelize.query(hostQuery1,
          {
          replacements: [request.body.roomID],
          type: models.sequelize.QueryTypes.DELETE,
          raw: true
        })
        .catch(function(error){
          response.json({
            msg:'query error occcured',
            code:500
          })
        })
        models.sequelize.query(hostQuery2,
          {
          replacements: [request.body.roomID],
          type: models.sequelize.QueryTypes.DELETE,
          raw: true
        })
        .catch(function(error){
          response.json({
            msg:'query error occcured',
            code:500
          })
        })
        models.sequelize.query(hostQuery3,
          {
          replacements: [request.body.roomID],
          type: models.sequelize.QueryTypes.DELETE,
          raw: true
        })
        .catch(function(error){
          response.json({
            msg:'query error occcured',
            code:500
          })
        })
        return 1;
      }
    }

    const respond = () => {
    response.json({msg:"success",
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

  Room.findRoomByRoomID(request.body.roomID)
  .catch(function(error){
    response.json({
      msg:'query error occcured',
      code:500
    })
  })
  .then(isHost)
  .then(exitRoom)
  .then(respond)
}
