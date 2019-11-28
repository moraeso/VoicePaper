const express = require('express');
const router = express.Router();

const controller = require('./controller');
////형수
router.post('/roomCreate', controller.roomCreate);
router.post('/roomParticipate', controller.roomParticipate);


//유식
router.post('/memberlist',controller.memberlist);
router.post('/grouplist',controller.getGroupList);
router.post('/voicedatalist',controller.getVoiceData);
router.post('/deletevoicedata',controller.deleteVoiceDataIndex);
router.post('/exitgroup',controller.exitGroup);

module.exports = router;
