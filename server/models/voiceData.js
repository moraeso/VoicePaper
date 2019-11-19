module.exports = (sequelize, Datatypes) => {
  var voiceData = sequelize.define('voiceData', {  
    fileID: {
      type: Datatypes.INTEGER,
      allowNull: false,
      unique: true,
      autoIncrement: true,
      primaryKey: true
    },
    userID: {
      type: Datatypes.STRING(45),
    },
    roomID: {
      type: Datatypes.INTEGER
    },
    filePath: {
      type: Datatypes.STRING(45)
    },
    recordedTime: {
      type: Datatypes.STRING(45)
    }
  }, {
      classMethods: {},
      tableName: 'voiceData',
      freezeTableName: true,
      underscored: false,
      timestamps: false
  });

  return voiceData;
};