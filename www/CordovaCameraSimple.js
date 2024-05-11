var CordovaCameraSimple = {
    openCamera: function(successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, 'CordovaCameraSimple', 'openCamera', []);
    }
};

module.exports = CordovaCameraSimple;
