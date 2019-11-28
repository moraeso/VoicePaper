const sanitizeHtml = require('sanitize-html');
const models = require('../../models');
const shortID = require('shortid');
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
  const hostID = sanitizeHtml(request.body.hostID);

  const getNumberOfRooms = () => {
    return Room.count();
  }

  const getNumberOfUsersRoom = (numberOfRoom) => {
    if(numberOfRoom < maximumRoomNumber){
      return Room.count({
        where: {'hostID': hostID}
      });
    } else {
      response.json({
        roomCreateStatus: 'maximumRoomNumberLimitError',
        code: 301
      })
    }
  }

  const create = (numberOfUsersRoom) => {
    if(numberOfUsersRoom < maximumRoomNumberForUser){
      console.log("roomCreate");
      return Room.create({
        hostID: hostID,
        roomName: roomName,
        roomImage: roomImage,
        roomText: roomText,
        roomPermission: roomPermission,
        roomCode: shortID.generate()
      })
    } else {
      response.json({
        roomCreateStatus: 'maximumRoomNumberForUserLimitError',
        code: 302
      })
    }
  }

  const participate = (roomCreateResult) => {
    console.log("Participate room");
    console.log(hostID);
    console.log(roomCreateResult.roomID);
    RoomMembership.create({
      userID: hostID,
      roomID: roomCreateResult.roomID
    })

    return roomCreateResult;
  }

  const respond = (roomCreateResult) => {
    console.log(roomCreateResult);
      response.send({
        roomCreateStatus: "roomCreateSuccess",
        code: 300,
        roomID: roomCreateResult.roomID,
        roomCode: roomCreateResult.roomCode,
        roomName: roomCreateResult.roomName,
        roomProfile: roomCreateResult.roomImage,
        roomText: roomCreateResult.roomText,
        roomPermission: roomCreateResult.roomPermission,
        hostID: roomCreateResult.hostID
      })

  }

  getNumberOfRooms()
  .then(getNumberOfUsersRoom)
  .then(create)
  .then(participate)
  .then(respond)
}

exports.roomParticipate = (request, response) => {
  const userID = sanitizeHtml(request.body.id);
  const roomCode = sanitizeHtml(request.body.roomCode);
  let room;


  const isHost = (checkRoomResult) => {

    room = checkRoomResult;

    if(checkRoomResult != undefined){
      if(checkRoomResult.hostID != userID){
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
    console.log(room.roomID);
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
    console.log(roomParticipateResult.roomID);
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
      hostID: room.hostID,
      memberCount: roomMemberCountResult,
      permission: room.roomPermission,
    }

    console.log(roomInfo);


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

  Room.findRoomByRoomCode(roomCode)
  .then(isHost)
  .then(checkIfAlreadyParticipating)
  .then(create)
  .then(getMemberCount)
  .then(respond)
}


//member list query test
exports.memberlist = function(req, res) {
//  console.log(req);

    var sql = 'select roomMembership.roomID,roomMembership.userID, user.userName from roomMembership JOIN user on roomMembership.userID=user.userID where roomMembership.roomID=?';
    var item = [req.body.roomID]; //1=>req.body.roomID

    const getMemberlist=()=>{
      return models.sequelize.query(sql,
      {
        replacements: item,
        type:models.sequelize.QueryTypes.SELECT,
        raw:true
      });
    }

    const respond =(getMemberlist)=>{
      res.json({memberlist:getMemberlist,
                code : 200});
    }

    getMemberlist()
    .then(respond);
}// /memberlistquerytest

//group list
exports.getGroupList = (req, res) => {

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
          replacements: ['CountMember', req.body.userID],
          type: models.sequelize.QueryTypes.SELECT,
          raw: true
        });
  }


  const respond = (getParticipatingRoomAndCount) => {
    res.json({
              roomList:getParticipatingRoomAndCount,
              code:200
            });
  }

  getParticipatingRoomAndCount()
  .then(respond)
}

//get group uploaded voice data
exports.getVoiceData = (req, res) => {

  const query = 'SELECT * FROM voiceData WHERE roomID=?;'

  const getGroupUploadedVoiceData = () => {
      console.log('**** get Group Uploaded Voice Data process****');
      return models.sequelize.query(query,
        {
          replacements: [req.body.roomID],
          type: models.sequelize.QueryTypes.SELECT,
          raw: true
        });
  }


    const respond = (getGroupUploadedVoiceData) => {
    res.json({voiceList:getGroupUploadedVoiceData,
              code:200});
  }

  getGroupUploadedVoiceData()
  .then(respond)
}

//Delete voice data in DB.
exports.deleteVoiceDataIndex = (req, res) => {

  const query = 'DELETE FROM voiceData WHERE roomID=? AND userID=?;'

  const delteVoiceData = () => {
      console.log('**** Delete voice data index ****');
      return models.sequelize.query(query,
        {
          replacements: [req.body.roomID,req.body.userID],
          type: models.sequelize.QueryTypes.DELETE,
          raw: true
        });
  }

    const respond = () => {
    res.json({msg:"success",
              code:200});
  }

  delteVoiceData()
  .then(respond)
}

//exit group
exports.exitGroup = (req, res) => {

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
      if(checkRoomResult.hostID != req.body.userID){
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
            replacements: [req.body.userID,req.body.roomID],
            type: models.sequelize.QueryTypes.DELETE,
            raw: true
          });
      } else {
        console.log("host");
        models.sequelize.query(hostQuery1,
          {
          replacements: [req.body.roomID],
          type: models.sequelize.QueryTypes.DELETE,
          raw: true
        });
        models.sequelize.query(hostQuery2,
          {
          replacements: [req.body.roomID],
          type: models.sequelize.QueryTypes.DELETE,
          raw: true
        });
        models.sequelize.query(hostQuery3,
          {
          replacements: [req.body.roomID],
          type: models.sequelize.QueryTypes.DELETE,
          raw: true
        });
        return 1;
      }
    }

    const respond = () => {
    res.json({msg:"success",
              code:200});
  }

  Room.findRoomByRoomID(req.body.roomID)
  .then(isHost)
  .then(exitGroup)
  .then(respond)
}
