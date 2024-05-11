import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.util.Base64;

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
            String encodedImage = encodeImage(photo); 
            callbackContext.success(encodedImage);
        } else {
            callbackContext.error("Failed to capture photo");
        }
    }

    private String encodeImage(Bitmap photo) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] byteArrayImage = baos.toByteArray();
        return Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
    }
}
