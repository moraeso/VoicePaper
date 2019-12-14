const sanitizeHtml = require('sanitize-html');
const models = require('../../models');
const shortID = require('shortid');
const jwt = require('jsonwebtoken');
const auth = require('../../javascripts/auth');


const {Room} = require('../../models');
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
        code: 200,
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

exports.roomOptionModify = function(request, response) {
  const roomName = sanitizeHtml(request.body.roomName);
  const roomText = sanitizeHtml(request.body.roomText);
  const roomPermission = sanitizeHtml(request.body.roomPermission);
  const roomID = sanitizeHtml(request.body.roomID);

  const respond = (updatedResult) => {
    console.log("updated: ", updatedResult[1]);
    if(updatedResult[1] == 1) {
      response.json({
        'roomSettingStatus': 'success',
        code: 200,
        roomName: roomName,
        roomText: roomText,
        roomPermission: roomPermission
      })
    } else if(updatedResult[1] == 0) {
      console.log("****no changes!****");
      response.json({
        'roomSettingStatus': 'noChanges',
        code: 601
      })
    } else {
      response.json({
        'roomSettingStatus': 'fail',
        code: 602
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
  .then(respond);

}

//룸
exports.queryRoomInformation = (request, response) => {
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
