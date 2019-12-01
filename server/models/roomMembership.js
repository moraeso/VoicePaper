module.exports = (sequelize, Datatypes) => {
  var roomMembership = sequelize.define('roomMembership', {  
    userID: {
      type: Datatypes.STRING(45),
    },
    roomID: {
      type: Datatypes.INTEGER
    },
    filePath: {
      type: Datatypes.STRING(45)
    }
  }, {
      classMethods: {},
      tableName: 'roomMembership',
      freezeTableName: true,
      underscored: false,
      timestamps: false
  });

  return roomMembership;
};