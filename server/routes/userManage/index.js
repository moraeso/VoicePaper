const express = require('express');
const router = express.Router();

const controller = require('./controller');

router.post('/info', controller.getUserInfo);
router.post('/change', controller.modifyUserPassword);

module.exports = router;
