package com.android.goldengift.store.products;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.goldengift.BuildConfig;
import com.android.goldengift.Injection;
import com.android.goldengift.R;
import com.android.goldengift.backend.categories.CategoriesRepository;
import com.android.goldengift.backend.products.ProductsRepository;
import com.android.goldengift.backend.unitprice.UnitPriceRepository;
import com.android.goldengift.model.Category;
import com.android.goldengift.model.Product;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

public class ProductsActivity extends AppCompatActivity implements ProductsRepository.AddProductsCallback {

    private static final String TAG = Product.class.getName();

    private static final int PICK_MULTIPLE_IMAGE = 2;

    private static final int CAMERA_REQUEST_CODE = 1;

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    private ProductsPresenter mPresenter;
    private Spinner categorySpinner, unitPriceSpinner;
    private EditText productNameEditText, descEditText, unitPriceEditText;
    private Button addImagesButton;
    private TextView imagesUploadedTextView;
    private ArrayList<String> mSelectedImageArrayUri;
    private String mSelectedCategory;
    private String mSelectedUnitPrice;
    private ProgressBar categoriesSpinnerProgressBar, unitPriceSpinnerProgressBar,imagesUploadingProgressBar;
    private ProgressDialog dialog;
    private PickCaptureDialog pickCaptureDialog;
    private String[] permissions;
    private String cameraFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPresenter = new ProductsPresenter(this, Injection.provideAddProductUseCaseHandler());

        initializeViews();
        initializeCategorySpinner();
        initializeUnitPriceSpinner();
    }

    private void initializeViews() {
        categoriesSpinnerProgressBar = findViewById(R.id.category_spinner_progressbar);
        unitPriceSpinnerProgressBar = findViewById(R.id.unit_price_progressbar);
        productNameEditText = findViewById(R.id.product_name_edittext);
        descEditText = findViewById(R.id.desc_edittext);
        unitPriceEditText = findViewById(R.id.unit_price_edittext);

        imagesUploadedTextView = findViewById(R.id.images_uploaded_textview);
        imagesUploadingProgressBar = findViewById(R.id.images_uploading_progressbar);

        addImagesButton = findViewById(R.id.add_images_button);
        addImagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                pickImage();
                pickCaptureDialog = new PickCaptureDialog(ProductsActivity.this);
                pickCaptureDialog.show();
                imagesUploadingProgressBar.setVisibility(View.VISIBLE);
                imagesUploadedTextView.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void initializeCategorySpinner() {
        mPresenter.retrieveCategories(new CategoriesRepository.CategoriesRetrievingCallback() {


            @Override
            public void onCategoriesRetrievedSuccessfully(final ArrayList<Category> categories) {
                categorySpinner = findViewById(R.id.category_spinner);
                categoriesSpinnerProgressBar.setVisibility(View.INVISIBLE);
                categorySpinner.setVisibility(View.VISIBLE);

                final ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<String>(ProductsActivity.this, android.R.layout.simple_spinner_dropdown_item);
                for (Category category : categories) {
                    categoriesAdapter.add(category.getName());
                }

                categorySpinner.setAdapter(categoriesAdapter);
                categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        mSelectedCategory = categories.get(position).getName();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        Toast.makeText(ProductsActivity.this, "no thing selected", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCategoriesRetrievedFailed(String errmsg) {
                categoriesSpinnerProgressBar.setVisibility(View.INVISIBLE);
                categorySpinner.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initializeUnitPriceSpinner() {
        mPresenter.retrieveUnitPrice(new UnitPriceRepository.UnitPriceRetrievingCallback() {
            @Override
            public void onRetrievingUnitPriceSuccessfully(final ArrayList<String> unitPrices) {
                unitPriceSpinner = findViewById(R.id.unit_price_spinner);
                unitPriceSpinnerProgressBar.setVisibility(View.INVISIBLE);
                unitPriceSpinner.setVisibility(View.VISIBLE);

                final ArrayAdapter<String> unitPriceAdapter = new ArrayAdapter<>(ProductsActivity.this, android.R.layout.simple_spinner_dropdown_item);
                for (String unitPrice : unitPrices) {
                    unitPriceAdapter.add(unitPrice);
                }
                unitPriceSpinner.setAdapter(unitPriceAdapter);
                unitPriceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        mSelectedUnitPrice = unitPrices.get(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        Toast.makeText(ProductsActivity.this, "no thing selected", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onRetrievingUnitPriceFailed(String errmsg) {
                unitPriceSpinnerProgressBar.setVisibility(View.INVISIBLE);
                unitPriceSpinner.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_product_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else if (item.getItemId() == R.id.done) {
            showProgressDialog();
            mPresenter.addNewProduct(getProductData(), this);
        }
        return true;
    }

    private void showProgressDialog() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Adding new Product, Please wait..");
        dialog.setCancelable(false);
        dialog.show();
    }

    private Product getProductData() {
        Product product = new Product();
        product.setName(productNameEditText.getText().toString());
        product.setDesc(descEditText.getText().toString());
        try {
            product.setUnitPrice(Double.parseDouble(unitPriceEditText.getText().toString()));
        } catch (NumberFormatException ex) {
            product.setUnitPrice(0);
        }

        product.setCategoryId(mSelectedCategory);
        product.setUnit(mSelectedUnitPrice);
        product.setImagesUrls(mSelectedImageArrayUri);
        return product;
    }

    public void captureImage() {
        if (!checkPermissions()) {
            requestPermissions();
        } else {
            try {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", createImageFile()));
                startActivityForResult(intent, CAMERA_REQUEST_CODE);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void pickImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_MULTIPLE_IMAGE);
    }

    public void setSelectCategoryErrorMessage() {
        dialog.dismiss();
        Toast.makeText(this, "You must select category", Toast.LENGTH_SHORT).show();
    }

    public void setProductNameErrorMessage() {
        dialog.dismiss();
        productNameEditText.setError("Product Name can't be empty");
    }

    public void setUnitErrorMessage() {
        dialog.dismiss();
        Toast.makeText(this, "You must select unit price", Toast.LENGTH_SHORT).show();
    }

    public void setUnitPriceErrorMessage() {
        dialog.dismiss();
        unitPriceEditText.setError("Unit price can't be empty");
    }

    public void setProductImagesErrorMessage() {
        dialog.dismiss();
        Toast.makeText(this, "You must select images for the product to be added", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAddingNewProductsSuccessfully() {
        Toast.makeText(this, "The Product is added successfully", Toast.LENGTH_SHORT).show();
        dialog.dismiss();
        finish();
    }

    @Override
    public void onAddingNewProductFailed(String errmsg) {
        dialog.dismiss();
        Toast.makeText(this, errmsg, Toast.LENGTH_SHORT).show();
    }

    private void showSnackBar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(
                findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    public void showPermissionDeniedSnackbar() {
        showSnackBar(R.string.permission_denied_explanation, R.string.settings,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Build intent that displays the App settings screen.
                        Intent intent = new Intent();
                        intent.setAction(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null);
                        intent.setData(uri);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //This is the directory in which the file will be created. This is the default location of Camera photos
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for using again
        cameraFilePath = "file://" + image.getAbsolutePath();
        return image;
    }

    private boolean checkPermissions() {
        permissions = new String[2];
        permissions[0] = Manifest.permission.CAMERA;
        permissions[1] = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            showSnackBar(R.string.permission_rationale, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(ProductsActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });
        } else {
            ActivityCompat.requestPermissions(this,
                    permissions,
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PickCaptureDialog.REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "Permission granted.");
                captureImage();
            } else {
                showPermissionDeniedSnackbar();
                Log.i(TAG, "Permission denied explanation.");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            mSelectedImageArrayUri = new ArrayList<>();
            // When an Image is picked
            if (resultCode == RESULT_OK) {
                if (requestCode == PickCaptureDialog.PICK_MULTIPLE_IMAGE && null != data) {
                    // Get the Image from data
                    if (data.getData() != null) {
                        Uri mImageUri = data.getData();
                        mSelectedImageArrayUri.add(mImageUri.toString());
                    } else {
                        if (data.getClipData() != null) {
                            ClipData mClipData = data.getClipData();

                            for (int i = 0; i < mClipData.getItemCount(); i++) {

                                ClipData.Item item = mClipData.getItemAt(i);
                                Uri uri = item.getUri();
                                mSelectedImageArrayUri.add(uri.toString());
                            }
                        }
                    }
                } else if (requestCode == PickCaptureDialog.CAMERA_REQUEST_CODE) {
                    mSelectedImageArrayUri.add(cameraFilePath);
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }
        imagesUploadingProgressBar.setVisibility(View.INVISIBLE);
        imagesUploadedTextView.setVisibility(View.VISIBLE);
        imagesUploadedTextView.setText(String.format("Selected Images : %d", mSelectedImageArrayUri.size()));
        super.onActivityResult(requestCode, resultCode, data);
    }
}
