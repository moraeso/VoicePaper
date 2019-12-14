const express = require('express');
const router = express.Router();

const controller = require('./controller');

router.post('/create', controller.roomCreate);
router.post('/setting', controller.roomOptionModify);
router.post('/info',controller.queryRoomInformation); 

module.exports = router;
