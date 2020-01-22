package com.example.assignment;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;

    private Context context;

    LatLng location;

    double latitude, longitude, newLatitude, newLongitude;

    List<Address> addressList;

    Geocoder geocoder;

    private static final int REQUEST_IMAGE_CAPTURE = 111;

    private static final int REQUEST_GALLARY = 211;

    private Uri mUri;

    private File direct;

    private String imageName = "1";

    private int compressionLevel,complevel;

    String BYTE_ARRAY = "BYTE_ARRAY";

    String COMPRESSION_LEVEL="COMPRESSION_LEVEL";

    private Bitmap camBitmap;

    String addressData ,lat,lang;

    DBHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        context = MapsActivity.this;
        myDB = new DBHelper(MapsActivity.this);
        RequestMultiplePermission();



        direct = new File(Environment.getExternalStorageDirectory() + "/assignment_images");
        if (!direct.exists()) {
            direct.mkdirs();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(MapsActivity.this);
            }
        },2000);



    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setOnMarkerDragListener(MapsActivity.this);
        mMap.setOnMarkerClickListener(MapsActivity.this);

        // Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/

        /*if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }*/

        if (isNetworkAvailable())
        {
            try {
                addressList = new ArrayList<>();
                googleMap.setMyLocationEnabled(true);

                // For dropping a marker at a point on the Map
                geocoder = new Geocoder(context);

                GpsTracker gpsTracker = new GpsTracker(context);
                Intent intent = getIntent();
                if(intent.hasExtra("latitude") && intent.hasExtra("longitude")){
                    latitude = Double.parseDouble(intent.getExtras().getString("latitude"));
                    longitude = Double.parseDouble(intent.getExtras().getString("longitude"));
                }else if (gpsTracker.isGPSEnabled || gpsTracker.isNetworkEnabled)
                {
                    latitude = gpsTracker.getLatitude();
                    longitude = gpsTracker.getLongitude();
                }
                location = new LatLng(latitude, longitude);


                addressList = geocoder.getFromLocation(latitude, longitude, 1);

                if(addressList.size()!=0){
                    Address address = addressList.get(0);

                    CameraPosition cameraPosition = new CameraPosition.Builder().target(location).zoom(12)
                            .build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    googleMap.addMarker(
                            new MarkerOptions().position(location).title(address.getAddressLine(0)));



                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener()
                            {
                                @Override
                                public void onCameraMove()
                                {

                                    try
                                    {


                                        mMap.clear();

                                        LatLng updateLocation = mMap.getCameraPosition().target;

                                        newLatitude = updateLocation.latitude;
                                        newLongitude = updateLocation.longitude;

											/*List<Address> newAddressList = geocoder.getFromLocation(newLatitude,newLongitude,1);

											if(newAddressList != null && !newAddressList.isEmpty()) {

											    Address address = newAddressList.get(0);

											    googleMap.addMarker(new MarkerOptions().position(updateLocation).title(address.getAddressLine(0)));
											}
											else*/
                                        {
                                            mMap.addMarker(new MarkerOptions().position(updateLocation)
                                                    .title("" + newLatitude + " , " + newLongitude));
                                        }

                                    }
                                    catch (Exception e)
                                    {
                                        e.printStackTrace();
                                    }
                                }

                            });
                        }
                    }, 2000);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_SHORT).show();

            //LeadActivity.md_customProgressDialog.dismiss();
        }
    }

    private boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    private void RequestMultiplePermission()
    {

        // Creating String Array with Permissions.
        ActivityCompat.requestPermissions(this,
                new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS, Manifest.permission.READ_CONTACTS,
                        Manifest.permission.REQUEST_INSTALL_PACKAGES,Manifest.permission.INTERNET


                }, 7);

    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        LatLng markerPosition = marker.getPosition();
        try {
            addresses = geocoder.getFromLocation(markerPosition.latitude, markerPosition.longitude, 1);
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            Log.d("MapsActivity",address);
            System.out.println("address===="+address);
            showConfirmLocationDialogue(address, markerPosition);
            //Toast.makeText(this , "Confirm location :" + address.toString() , Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return true;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        LatLng markerPosition = marker.getPosition();
        try {
            addresses = geocoder.getFromLocation(markerPosition.latitude, markerPosition.longitude, 1);
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            showConfirmLocationDialogue(address, markerPosition);
            //Toast.makeText(this , "Confirm location :" + address.toString() , Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void showConfirmLocationDialogue(String address, final LatLng markerPosition) {
        // Create custom dialog object
        addressData = address;
        lat = String.valueOf(markerPosition.latitude);
        lang = String.valueOf(markerPosition.longitude);

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Include dialog.xml file
        dialog.setContentView(R.layout.custom_location_dialogue);

        // Set dialog title
        //dialog.setTitle("Custom Dialog");

        // set values for custom dialog components - text, image and button
        TextView tv_address = (TextView) dialog.findViewById(R.id.Id_tv_address);
        TextView btn_confirm = (TextView) dialog.findViewById(R.id.Id_btn_confirm);
        TextView btn_cancel = (TextView) dialog.findViewById(R.id.Id_btn_cancel);

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initUploadSelection();
                dialog.cancel();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();

            }
        });
        tv_address.setText(address);
        dialog.show();


    }

    public void initUploadSelection() {
        final Dialog dialog = new Dialog(context, android.R.style
                .Theme_Translucent_NoTitleBar_Fullscreen);
        LayoutInflater li = LayoutInflater.from(context);
        final View myView = li.inflate(R.layout.custom_upload_document_selection, null);
        dialog.setContentView(myView);
        ImageView selectCamera = (ImageView) myView.findViewById(R.id.select_from_camera);
        ImageView selectGallery = (ImageView) myView.findViewById(R.id.select_from_gallery);

        selectCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureIdPhoto();
                dialog.dismiss();
            }
        });

        selectGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openGallery(REQUEST_GALLARY);
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void captureIdPhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void openGallery(int requestCode) {
        Intent intent = new Intent();
        // Show only images, no videos or anything else
        intent.setType("image/*");
        //String[] mimeTypes = {"image/jpeg", "image/png"};
        String[] mimeTypes = {"image/jpeg"};
        //intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setAction(Intent.ACTION_PICK);
        File f = new File(direct.getPath() + "/" + imageName + ".png");
        //takePictureClick = true;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));

            mUri = Uri.fromFile(f);
        } else {
            mUri = FileProvider.getUriForFile(context, ".GenericFileProvider", f);
            //Uri with file provider
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);

        }
        // Always show the chooser (if there are multiple options available)
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), requestCode);
    }

    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String encodedImage = "";
        byte[] b = null;
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if(data != null){
                complevel = data.getIntExtra(COMPRESSION_LEVEL, 75);
                camBitmap = (Bitmap)data.getExtras().get("data");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                camBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                 b = baos.toByteArray();
                //encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
            }



        }

        if (requestCode == REQUEST_GALLARY && resultCode == RESULT_OK){
            byte[] imgdata = data.getByteArrayExtra(BYTE_ARRAY);
            //Uri uri = data.getData();
            complevel = data.getIntExtra(COMPRESSION_LEVEL, 75);
            Uri selectedImage = data.getData();
            if (selectedImage != null){
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = context.getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                File file =  new File(picturePath);
                if (selectedImage.toString().startsWith("content://com.google.android.apps.photos.content"))
                {
                    try {
                        InputStream is = context.getContentResolver().openInputStream(selectedImage);
                        camBitmap = BitmapFactory.decodeStream(is);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    camBitmap = BitmapFactory.decodeFile(picturePath);
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                camBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                b = baos.toByteArray();
                //encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
            }

        }
        Log.d("MapsActivity",encodedImage);

        if(b !=null){
            insertData(addressData,lat,lang,b);
        }

    }

    private void insertData(String addressData,String lat,String lang,byte[] imgByte){
        if(myDB.insertData(addressData,lat,lang,imgByte)){
            Log.d("MapsActivity","insert successfully");
            Intent intent = new Intent(MapsActivity.this,ListDisplayActivity.class);
            startActivity(intent);
        }
    }
}
