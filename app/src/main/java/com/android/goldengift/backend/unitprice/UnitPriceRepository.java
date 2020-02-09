package com.android.goldengift.backend.unitprice;

import java.util.ArrayList;

public interface UnitPriceRepository {

    interface UnitPriceRetrievingCallback {

        void onRetrievingUnitPriceSuccessfully(ArrayList<String> unitPrices);

        void onRetrievingUnitPriceFailed(String errmsg);
    }

    void retrieveUnitPrices(UnitPriceRetrievingCallback callback);
}
