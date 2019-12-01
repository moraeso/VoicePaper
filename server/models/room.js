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
    roomTag: {
      type: Datatypes.STRING(45)
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
    console.log('function \'findRoomByRoomID\' start!');
    return room.findOne({
      where: {roomID: roomID}
    })
  }

  return room;
};