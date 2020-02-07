package com.android.goldengift.backend.categories;

import com.android.goldengift.model.Category;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;

public class CategoriesRepositoryImpl implements CategoriesRepository{

    private static final String CATEGORIES = "categories";

    private final DatabaseReference mDatabase;

    public CategoriesRepositoryImpl() {
        mDatabase = FirebaseDatabase.getInstance().getReference(CATEGORIES);
    }

    @Override
    public void retrieveAllCategories(final CategoriesRetrievingCallback callback) {
        mDatabase.addValueEventListener(new ValueEventListener() {
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
    public void insertNewCategory(Category category, final CategoriesInsertionCallback callback) {
        mDatabase.push().setValue(category).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    callback.onInsertNewCategorySuccessfully();
                } else {
                    callback.onInsertNewCategoryFailed(task.getException().getMessage());
                }
            }
        });
    }
}
