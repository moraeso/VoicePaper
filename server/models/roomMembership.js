module.exports = (sequelize, Datatypes) => {
  var roomMembership = sequelize.define('roomMembership', {  
    userID: {
      type: Datatypes.STRING(45),
      primaryKey: true
    },
    roomID: {
      type: Datatypes.INTEGER,
      primaryKey: true
    }
  }, {
      classMethods: {},
      tableName: 'roomMembership',
      freezeTableName: true,
      underscored: false,
      timestamps: false
  });

  roomMembership.findParticipatingUserByID = function(userID, roomID) {
    console.log('****finding process****');
    return roomMembership.findOne({
      where: { 
        userID: userID,
        roomID: roomID
      }
    })
  }

  roomMembership.countRoomMember = function(roomID) {
    console.log("****count process****")
    console.log(roomID);
    return roomMembership.count({
      where: {
        roomID: roomID
      }
    })
  }

  return roomMembership;
};