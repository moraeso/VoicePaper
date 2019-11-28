const express = require('express');
const router = express.Router();

const controller = require('./controller');

router.get('/roomCreate', controller.roomCreate);
router.get('/roomParticipate', controller.roomParticipate);

module.exports = router;