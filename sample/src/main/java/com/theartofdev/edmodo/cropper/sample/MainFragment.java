// "Therefore those skilled at the unorthodox
// are infinite as heaven and earth,
// inexhaustible as the great rivers.
// When they come to an end,
// they begin again,
// like the days and months;
// they die and are reborn,
// like the four seasons."
//
// - Sun Tsu,
// "The Art of War"

package com.theartofdev.edmodo.cropper.sample;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.croppersample.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

/**
 * The fragment that will show the Image Cropping UI by requested preset.
 */
public final class MainFragment extends Fragment
        implements CropImageView.OnSetImageUriCompleteListener, CropImageView.OnGetCroppedImageCompleteListener {

    private CropImageView mCropImageView;

    /**
     * Set the image to show for cropping.
     */
    public void setImageUri(Uri imageUri) {
        mCropImageView.setImageUriAsync(imageUri);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_rect, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCropImageView = (CropImageView) view.findViewById(R.id.cropImageView);
        mCropImageView.setOnSetImageUriCompleteListener(this);
        mCropImageView.setOnGetCroppedImageCompleteListener(this);

        if (savedInstanceState == null) {
            mCropImageView.setImageResource(R.drawable.cat);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.main_action_crop) {
            mCropImageView.getCroppedImageAsync();
            return true;
        } else if (item.getItemId() == R.id.main_action_rotate) {
            mCropImageView.rotateImage(90);
            return true;
        } else if (item.getItemId() == R.id.main_action_camera) {
            if (CropImage.isExplicitCameraPermissionRequired(getActivity())) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, CropImage.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE);
            } else {
                CropImage.startPickImageActivity(getActivity());
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).setCurrentFragment(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mCropImageView != null) {
            mCropImageView.setOnSetImageUriCompleteListener(null);
            mCropImageView.setOnGetCroppedImageCompleteListener(null);
        }
    }

    @Override
    public void onSetImageUriComplete(CropImageView view, Uri uri, Exception error) {
        if (error == null) {
            Toast.makeText(getActivity(), "Image load successful", Toast.LENGTH_SHORT).show();
        } else {
            Log.e("AIC", "Failed to load image by URI", error);
            Toast.makeText(getActivity(), "Image load failed: " + error.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onGetCroppedImageComplete(CropImageView view, Bitmap bitmap, Exception error) {
        handleCropResult(null, bitmap, error);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            handleCropResult(result.getUri(), null, result.getError());
        }
    }

    private void handleCropResult(Uri uri, Bitmap bitmap, Exception error) {
        if (error == null) {
            Intent intent = new Intent(getActivity(), CropResultActivity.class);
            if (uri != null) {
                intent.putExtra("URI", uri);
            } else {
                CropResultActivity.mImage = mCropImageView.getCropShape() == CropImageView.CropShape.OVAL
                        ? CropImage.toOvalBitmap(bitmap)
                        : bitmap;
            }
            startActivity(intent);
        } else {
            Log.e("AIC", "Failed to crop image", error);
            Toast.makeText(getActivity(), "Image crop failed: " + error.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
