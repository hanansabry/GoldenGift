package com.android.goldengift.backend.storage;

import android.net.Uri;

public interface ImagesStorage {

    interface UploadImageCallback {
        void onSuccessfullyImageUploaded(String imgUri);

        void onImageUploadedFailed(String errmsg);
    }

    void uploadImage(Uri filePath, String advertiserId, String modelId, UploadImageCallback callback);
}
