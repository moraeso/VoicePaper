const express = require('express');
const router = express.Router();

const controller = require('./controller');

router.post('/list',controller.getMemberList);
router.post('/leave',controller.leaveRoom);
router.post('/join', controller.roomParticipate);

module.exports = router;
