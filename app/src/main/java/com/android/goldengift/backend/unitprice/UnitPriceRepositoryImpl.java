package com.android.goldengift.backend.unitprice;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;

public class UnitPriceRepositoryImpl implements UnitPriceRepository {

    private static final String UNIT_PRICES = "unit_price";

    private final DatabaseReference mDatabase;

    public UnitPriceRepositoryImpl() {
        mDatabase = FirebaseDatabase.getInstance().getReference(UNIT_PRICES);
    }

    @Override
    public void retrieveUnitPrices(final UnitPriceRetrievingCallback callback) {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> unitPrices = (ArrayList<String>) dataSnapshot.getValue();
                callback.onRetrievingUnitPriceSuccessfully(unitPrices);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onRetrievingUnitPriceFailed(databaseError.getMessage());
            }
        });
    }
}
