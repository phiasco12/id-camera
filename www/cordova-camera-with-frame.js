var CordovaCameraWithFrame = {
    openCamera: function(successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, 'CordovaCameraWithFrame', 'openCamera', []);
    }
};

module.exports = CordovaCameraWithFrame;

