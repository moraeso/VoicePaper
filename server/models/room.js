module.exports = (sequelize, Datatypes) => {
  var room = sequelize.define('room', {
    roomID: {
      type: Datatypes.INTEGER,
      allowNull: false,
      unique: true,
      autoIncrement: true,
      primaryKey: true
    },
    hostID: {
      type: Datatypes.INTEGER,
      allowNull: false
    },
    roomName: {
      type: Datatypes.STRING(45),
      allowNull: false
    },
    roomImage: {
      type: Datatypes.STRING(45),
      allowNull: false
    },
    roomText: {
      type: Datatypes.STRING(100),
      allowNull: false
    },
    roomPermission: {
      type: Datatypes.STRING(45)
    },
    roomCode: {
      type: Datatypes.STRING(45)
    }

  }, {
      classMethods: {},
      tableName: 'room',
      freezeTableName: true,
      underscored: false,
      timestamps: false
  });

  room.findRoomByRoomID = function(roomID) {
    console.log('function \'findRoomByRoomID\' start!', roomID);
    return room.findOne({
      where: {roomID: roomID}
    })
  }

  room.findRoomByRoomCode = function(roomCode) {
    console.log('function \'findRoomByRoomCode\' start!');
    return room.findOne({
      where: {roomCode: roomCode}
    })
  }

  room.updateRoomInfo = function(roomID, roomName, roomText, roomPermission) {
    console.log('****room update start****');

    return new Promise((resolve, reject)=> {
      room.update({
        roomName: roomName,
        roomText: roomText,
        roomPermission: roomPermission
      },
      {
        where: {roomID: roomID}
      })

      resolve();
    })
  }

  return room;
};