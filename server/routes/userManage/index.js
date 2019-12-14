const express = require('express');
const router = express.Router();

const controller = require('./controller');

router.post('/info', controller.getUserInfo);
router.post('/change', controller.modifyUserPassword);
router.post('/myroomlist',controller.getEnteredRoomList);
router.post('/register', controller.register);
router.post('/login', controller.login);

module.exports = router;
