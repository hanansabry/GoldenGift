package com.android.goldengift.backend.categories;

import com.android.goldengift.model.Category;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;

public class CategoriesRepositoryImpl implements CategoriesRepository{

    private static final String CATEGORIES = "categories";
    private static final String STORES = "stores";

    private final FirebaseDatabase mDatabase;

    public CategoriesRepositoryImpl() {
        mDatabase = FirebaseDatabase.getInstance();
    }

    @Override
    public void retrieveCategoriesByCurrentStoreId(final CategoriesRetrievingCallback callback) {
//        String currentStoreId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase.getReference(CATEGORIES)
//                .child(currentStoreId)
//                .child(CATEGORIES)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<Category> categories = new ArrayList<>();
                        for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                            Category category = new Category();
                            String categoryName = categorySnapshot.getKey();
                            category.setName(categoryName);
                            categories.add(category);
                        }
                        callback.onCategoriesRetrievedSuccessfully(categories);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        callback.onCategoriesRetrievedFailed(databaseError.getMessage());
                    }
                });
    }

    @Override
    public void retrieveAllCategories(final CategoriesRetrievingCallback callback) {
        mDatabase.getReference(CATEGORIES).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Category> categories = new ArrayList<>();
                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    Category category = categorySnapshot.getValue(Category.class);
                    category.setId(categorySnapshot.getKey());
                    categories.add(category);
                }
                callback.onCategoriesRetrievedSuccessfully(categories);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onCategoriesRetrievedFailed(databaseError.getMessage());
            }
        });
    }

    @Override
    public void insertNewCategory(final Category category, final CategoriesInsertionCallback callback) {
        //add category to categories node

        mDatabase.getReference(CATEGORIES).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild(category.getName())) {
                    mDatabase.getReference(CATEGORIES).child(category.getName()).setValue(category).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                //add category to store node
                                String storeId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                HashMap<String, Object> categoryValue = new HashMap<>();
                                categoryValue.put(category.getName(), true);
                                mDatabase.getReference(STORES)
                                        .child(storeId)
                                        .child(CATEGORIES)
                                        .updateChildren(categoryValue);
                                callback.onInsertNewCategorySuccessfully();
                            } else {
                                callback.onInsertNewCategoryFailed(task.getException().getMessage());
                            }
                        }
                    });
                } else {
                    //add category to store node
                    String storeId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    HashMap<String, Object> categoryValue = new HashMap<>();
                    categoryValue.put(category.getName(), true);
                    mDatabase.getReference(STORES)
                            .child(storeId)
                            .child(CATEGORIES)
                            .updateChildren(categoryValue);
                    callback.onInsertNewCategorySuccessfully();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onInsertNewCategoryFailed(databaseError.getMessage());
            }
        });
    }
}
