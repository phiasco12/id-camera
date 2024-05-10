var CordovaCameraWithFrame = function() {};

CordovaCameraWithFrame.prototype.openCamera = function(successCallback, errorCallback) {
    cordova.exec(successCallback, errorCallback, 'CordovaCameraWithFrame', 'openCamera', []);
};

module.exports = new CordovaCameraWithFrame();
