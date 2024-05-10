package com.example.cordova.camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CordovaCameraWithFrame extends CordovaPlugin {

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
    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    if (cameraIntent.resolveActivity(cordova.getActivity().getPackageManager()) != null) {
        cordova.startActivityForResult(this, cameraIntent, CAMERA_REQUEST);
        
        // Add logic to draw frame overlay on camera preview
        FrameOverlayView frameOverlay = new FrameOverlayView(cordova.getActivity());
        cordova.getActivity().addContentView(frameOverlay, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    } else {
        callbackContext.error("Camera not available");
    }
}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == cordova.getActivity().RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Bitmap croppedPhoto = cropToFrame(photo); // Implement method to crop photo to frame
            String encodedImage = encodeImage(croppedPhoto); // Implement method to encode bitmap to base64
            callbackContext.success(encodedImage);
        } else {
            callbackContext.error("Failed to capture photo");
        }
    }

    private Bitmap cropToFrame(Bitmap photo) {
        // Implement logic to crop photo to frame
        // This is a placeholder, you'll need to calculate the coordinates and dimensions of the frame
        int frameWidth = photo.getWidth() / 2;
        int frameHeight = photo.getHeight() / 2;
        return Bitmap.createBitmap(photo, frameWidth / 2, frameHeight / 2, frameWidth, frameHeight);
    }

    private String encodeImage(Bitmap photo) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] byteArrayImage = baos.toByteArray();
        return Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
    }
}
