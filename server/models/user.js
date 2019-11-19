module.exports = (sequelize, Datatypes) => {
  var user = sequelize.define('user', {
    userID: {
      type: Datatypes.STRING(45),
      allowNull: false,
      unique: true,
      primaryKey: true
    },
    userPassword: {
      type: Datatypes.STRING(100),
      allowNull: false
    },
    userName: {
      type: Datatypes.STRING(45),
      allowNull: false
    },
    userImage: {
      type: Datatypes.STRING(45)
    }
  }, {
      classMethods: {},
      tableName: 'user',
      freezeTableName: true,
      underscored: false,
      timestamps: false
  });

  user.findUserByID = function(userID) {
    console.log('****finding process****');
    return user.findOne({
      where: { userID: userID}
    })
  }

  return user;
};