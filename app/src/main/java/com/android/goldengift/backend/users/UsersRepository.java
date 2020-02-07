package com.android.goldengift.backend.users;


import com.android.goldengift.model.Store;

public interface UsersRepository {

    interface UserInsertionCallback {
        void onUserInsertedSuccessfully();

        void onUserInsertedFailed(String errmsg);
    }

    void insertNewUser(Store user, UserInsertionCallback callback);
}
