package com.example.cordova.camera;

import android.content.Intent;
import android.provider.MediaStore;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

public class CordovaCameraSimple extends CordovaPlugin {

    private static final int CAMERA_REQUEST = 1;
    private CallbackContext callbackContext;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("openCamera")) {
            this.callbackContext = callbackContext;
            openCamera();
            return true;
        }
        return false;
    }

    private void openCamera() {
        cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (cameraIntent.resolveActivity(cordova.getActivity().getPackageManager()) != null) {
                    cordova.startActivityForResult(CordovaCameraSimple.this, cameraIntent, CAMERA_REQUEST);
                } else {
                    callbackContext.error("Camera not available");
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == cordova.getActivity().RESULT_OK) {
            callbackContext.success("Photo captured successfully");
        } else {
            callbackContext.error("Failed to capture photo");
        }
    }
}
