var exec = require('cordova/exec');

var CordovaCameraWithFrame = function() {};

CordovaCameraWithFrame.prototype.openCamera = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, 'CordovaCameraWithFrame', 'openCamera', []);
};

module.exports = new CordovaCameraWithFrame();
