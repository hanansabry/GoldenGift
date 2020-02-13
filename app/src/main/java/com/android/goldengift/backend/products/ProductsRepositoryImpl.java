package com.android.goldengift.backend.products;

import android.net.Uri;

import com.android.goldengift.backend.storage.ImagesStorage;
import com.android.goldengift.model.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;

public class ProductsRepositoryImpl implements ProductsRepository {

    private static final String CATEGORIES = "categories";

    private static final String PRODUCTS = "products";

    private static final String STORES = "stores";

    private final ImagesStorage mImagesStorage;
    private final FirebaseDatabase mDatabase;
//    private String storeId;

    public ProductsRepositoryImpl(ImagesStorage mImagesStorage) {
        this.mImagesStorage = mImagesStorage;
        mDatabase = FirebaseDatabase.getInstance();
    }

    @Override
    public void retrieveProductsByCategoryId(String categoryId, final ProductsRetrievingCallback callback) {
        DatabaseReference storeRef = mDatabase.getReference(CATEGORIES).child(categoryId);

        //get ids of categories' products
        storeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("products").exists()) {
                    ArrayList<String> productsIds = new ArrayList<>();
                    DataSnapshot productsSnapshot = dataSnapshot.child("products");
                    for (DataSnapshot productSnapshot : productsSnapshot.getChildren()) {
                        productsIds.add(productSnapshot.getKey());
                    }

                    //get products items
                    getProductsItems(productsIds, callback);
                } else {
                    callback.onRetrievingProductsSuccessfully(new ArrayList<Product>());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onRetrievingProductsFailed(databaseError.getMessage());
            }
        });
    }

    private void getProductsItems(final ArrayList<String> productsIds, final ProductsRetrievingCallback callback) {
        DatabaseReference productsRef = mDatabase.getReference(PRODUCTS);
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Product> productItems = new ArrayList<>();
                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    if (productsIds.contains(productSnapshot.getKey())) {
                        Product product = productSnapshot.getValue(Product.class);
                        product.setId(productSnapshot.getKey());
                        productItems.add(product);
                    }
                }
                callback.onRetrievingProductsSuccessfully(productItems);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onRetrievingProductsFailed(databaseError.getMessage());
            }
        });
    }

    @Override
    public void addNewProduct (final Product product, final AddProductsCallback callback){
        final DatabaseReference productsRef = mDatabase.getReference(PRODUCTS);
        final String productId = productsRef.push().getKey();

        String storeId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        product.setStoreId(storeId);
        //upload model images to firebase storage
        final ArrayList<String> firebaseStorageImages = new ArrayList<>();
        for (String uri : product.getImagesUrls()) {
            mImagesStorage.uploadImage(Uri.parse(uri), product.getCategoryId(), productId, new ImagesStorage.UploadImageCallback() {
                @Override
                public void onSuccessfullyImageUploaded(String imgUri) {
                    firebaseStorageImages.add(imgUri);
                    if (firebaseStorageImages.size() == product.getImagesUrls().size()) {
                        product.setImagesUrls(firebaseStorageImages);
                        productsRef.child(productId).setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    callback.onAddingNewProductsSuccessfully();
                                    addProductToStoreAndCategory(productId, product.getCategoryId());
                                } else {
                                    callback.onAddingNewProductFailed(task.getException().getMessage());
                                }
                            }
                        });
                    }
                }

                @Override
                public void onImageUploadedFailed(String errmsg) {
                    callback.onAddingNewProductFailed(errmsg);
                }
            });
        }
    }

    private void addProductToStoreAndCategory(String productId, String categoryId) {
        String storeId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference storesDbRef = mDatabase.getReference(STORES);
        DatabaseReference categoriesDbRef = mDatabase.getReference(CATEGORIES);
        HashMap<String, Object> productIdValue = new HashMap<>();
        productIdValue.put(productId, true);
        storesDbRef.child(storeId).child(PRODUCTS).updateChildren(productIdValue);
        categoriesDbRef.child(categoryId).child(PRODUCTS).updateChildren(productIdValue);
    }
}
