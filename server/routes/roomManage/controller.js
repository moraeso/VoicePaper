const sanitizeHtml = require('sanitize-html');
const models = require('../../models');
const shortID = require('shortid');
const jwt = require('jsonwebtoken');
const auth = require('../../javascripts/auth');


const {Room} = require('../../models');
const {User} = require('../../models');
const {RoomMembership} = require('../../models');

const maximumRoomNumberForUser = 16;
const maximumRoomNumber = 256;


exports.roomCreate = (request, response) => {
  const roomName = sanitizeHtml(request.body.roomName);
  const roomImage = sanitizeHtml(request.body.roomProfile);
  const roomText = sanitizeHtml(request.body.roomText);
  const roomPermission = sanitizeHtml(request.body.roomPermission);
  const userID = sanitizeHtml(request.body.userID);


  const getNumberOfRooms = () => {
    return Room.count();
  }

  const getNumberOfUsersRoom = (numberOfRoom) => {
    if(numberOfRoom < maximumRoomNumber){
      return Room.count({
        where: {'hostID': userID}
      });
    } else {
      console.log('total room number: ', numberOfRoom);
      console.log('maximum room number: ', maximumRoomNumber);
      console.log('too many rooms for server');
      return response.json({
        roomCreateStatus: 'maximumRoomNumberLimitError',
        code: 301
      })
    }
  }

  const create = (numberOfUsersRoom) => {
    if(numberOfUsersRoom < maximumRoomNumberForUser){
      console.log("roomCreate");
      return Room.create({
        hostID: userID,
        roomName: roomName,
        roomImage: roomImage,
        roomText: roomText,
        roomPermission: roomPermission,
        roomCode: shortID.generate()
      })
    } else {
      console.log('too many rooms for you')
      response.json({
        roomCreateStatus: 'maximumRoomNumberForUserLimitError',
        code: 302
      })
    }
  }

  const participate = (roomCreateResult) => {
    console.log("Participate room");

    RoomMembership.create({
      userID: userID,
      roomID: roomCreateResult.roomID
    })

    return roomCreateResult;
  }

  const respond = (roomCreateResult) => {
      return response.json({
        roomCreateStatus: "roomCreateSuccess",
        code: 300,
        roomID: roomCreateResult.roomID,
        roomCode: roomCreateResult.roomCode,
        roomName: roomCreateResult.roomName,
        roomProfile: roomCreateResult.roomImage,
        roomText: roomCreateResult.roomText,
        roomPermission: roomCreateResult.roomPermission,
        userID: roomCreateResult.hostID
      })
  }

  auth.isLoggedIn(request, response)
  .then(auth.verifyToken)
  .then(getNumberOfRooms)
  .then(getNumberOfUsersRoom)
  .then(create)
  .then(participate)
  .then(respond)
  .catch(function(error){
    console.log(error)
    response.json({
      authStatus: 'unavailable tokennn',
      code: 503
    })
    throw new Error('close');
  })
}

exports.roomParticipate = (request, response) => {
  const userID = sanitizeHtml(request.body.userID);
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
    console.log('roomParticipateResult', roomParticipateResult);
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
      comment: room.roomText,
      userID: room.userID,
      memberCount: roomMemberCountResult,
      permission: room.roomPermission,
      hostID:room.hostID
    }

    if(roomMemberCountResult){
      response.json({
        roomParticipateStatus: "roomParticipateSuccess",
        Room: roomInfo,
        code: 400
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

exports.roomOptionModify = function(request, response) {
  const roomName = sanitizeHtml(request.body.roomName);
  const roomText = sanitizeHtml(request.body.roomText);
  const roomPermission = sanitizeHtml(request.body.roomPermission);
  const roomID = sanitizeHtml(request.body.roomID);

  let roomFromDB;

  const getRoomFromDB = (roomUpdateResult) => {
    console.log('roomUpdateResult: ', roomUpdateResult);

    return Room.findRoomByRoomID(roomID);
  }

  const respond = (getRoomFromDBResult) => {
    console.log(getRoomFromDBResult)
    console.log('getRoomFromDBResult: ', getRoomFromDBResult[0]);
    if(getRoomFromDBResult[0] != undefined) {
      response.json({
        'roomSettingStatus': 'success',
        code: 200,
        roomName: roomFromDB[0].roomName,
        roomText: roomFromDB[0].roomText,
        roomPermission: roomFromDB[0].roomPermission
      })
    } else {
      response.json({
        'roomSettingStatus': 'fail',
        code: 601
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

  Room.updateRoomInfo(roomID, roomName, roomText, roomPermission)
  .then(getRoomFromDB)
  .then(respond)
}


//유식
exports.memberlist = function(request, response) {
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
}// /memberlistquerytest

//group list
exports.getGroupList = (request, response) => {

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

//get group uploaded voice data
exports.getVoiceData = (request, response) => {
  const query = 'SELECT voiceData.*, user.userName FROM voiceData join user on user.userID=voiceData.userID WHERE roomID=?;'

  const getGroupUploadedVoiceData = () => {
      console.log('**** get Group Uploaded Voice Data process****');
      return models.sequelize.query(query,
        {
          replacements: [request.body.roomID],
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


    const respond = (getGroupUploadedVoiceData) => {
    response.json({voiceList:getGroupUploadedVoiceData,
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

  getGroupUploadedVoiceData()
  .then(respond)
}

//Delete voice data in DB.
exports.deleteVoiceDataIndex = (request, response) => {
  const query = 'DELETE FROM voiceData WHERE roomID=? AND userID=?;'

  const delteVoiceData = () => {
      console.log('**** Delete voice data index ****');
      return models.sequelize.query(query,
        {
          replacements: [request.body.roomID,request.body.userID],
          type: models.sequelize.QueryTypes.DELETE,
          raw: true
        })
        .catch(function(error){
          response.json({
            msg:'query error occcured',
            code:500
          })
        })
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
  delteVoiceData()
  .then(respond)
}

//exit group
exports.exitGroup = (request, response) => {
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


  const exitGroup = (host) => {
      console.log('**** EXIT GROUP process****');
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
  .then(exitGroup)
  .then(respond)
}


exports.getroominfo = (request, response) => {
    const respond = (roomInfo) => {
    response.json({roomInfo:roomInfo,
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
    .then(respond)
}
