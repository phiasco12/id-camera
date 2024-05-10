package com.example.cordova.camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.FrameLayout;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;

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
        cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (cameraIntent.resolveActivity(cordova.getActivity().getPackageManager()) != null) {
                    FrameLayout rootView = new FrameLayout(cordova.getActivity());
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
                    rootView.setLayoutParams(params);

                    // Add FrameOverlayView to root view
                    FrameOverlayView frameOverlay = new FrameOverlayView(cordova.getActivity());
                    rootView.addView(frameOverlay);

                    // Set root view as content view
                    cordova.getActivity().setContentView(rootView);

                    // Start camera intent
                    cordova.startActivityForResult(CordovaCameraWithFrame.this, cameraIntent, CAMERA_REQUEST);
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

    // Define FrameOverlayView class here
    private static class FrameOverlayView extends View {

        public FrameOverlayView(android.content.Context context) {
            super(context);
        }

        @Override
        protected void onDraw(android.graphics.Canvas canvas) {
            super.onDraw(canvas);

            // Draw a basic frame overlay (red rectangle) on the canvas
            android.graphics.Paint paint = new android.graphics.Paint();
            paint.setColor(android.graphics.Color.RED);
            paint.setStyle(android.graphics.Paint.Style.STROKE);
            paint.setStrokeWidth(5);
            int left = 100;
            int top = 100;
            int right = 500;
            int bottom = 500;
            canvas.drawRect(left, top, right, bottom, paint);
        }
    }
}
