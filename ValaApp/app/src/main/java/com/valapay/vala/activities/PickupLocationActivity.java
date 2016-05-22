package com.valapay.vala.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.valapay.vala.R;
import com.valapay.vala.model.Affiliate;
import com.valapay.vala.utils.LocationUtils;
import com.valapay.vala.utils.RoundImage;

import java.util.HashMap;
import java.util.Map;

public class PickupLocationActivity extends NavigationDrawerActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final float STUB_LAT = 32.064642f;
    private static final float STUB_LONG = 34.774431f;

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Button mButton;
    private boolean mHideMarkers;
    private TextView mAmountTextView;
    private ConfirmAffiliateTask confirmAffiliateTask = null;
    private Map<LatLng, Affiliate> mAffiliates;
    private Marker selectedMarker;
    private ImageView afImage;
    private ImageView afRating;
    private TextView afName;
    private ImageButton afPhone;
    private TextView afAddress;
    private TextView afHours;
    private TextView afDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        afImage = (ImageView) findViewById(R.id.imageViewAffiliate);
        afRating = (ImageView) findViewById(R.id.imageViewRating);
        afName = (TextView) findViewById(R.id.textViewAffiliateName);
        afPhone = (ImageButton) findViewById(R.id.imageButtonCall);
        afAddress = (TextView) findViewById(R.id.textViewAddress);
        afHours = (TextView) findViewById(R.id.textViewHours);
        afDistance = (TextView) findViewById(R.id.textViewdistance);;

        //Add map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        ImageView userImage = (ImageView) findViewById(R.id.imageViewAffiliate);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.babu);
        RoundImage roundedImage = new RoundImage(bm);
        userImage.setImageDrawable(roundedImage);

        mAmountTextView = (TextView) findViewById(R.id.textViewAmount);
        Spannable wordToSpan = new SpannableString(mAmountTextView.getText().toString());
        wordToSpan.setSpan(new StyleSpan(Typeface.BOLD), 1, wordToSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mAmountTextView.setText(wordToSpan);

        mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReservationActivity.startCollectActivity(mAffiliates.get(selectedMarker.getPosition()).getName(), "$100", PickupLocationActivity.this);
            }
        });

        //Add affiliates
        mAffiliates = new HashMap<>();
        addAffiliates();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Add items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_home) {
            startActivity(new Intent(this, HomeActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_pickup_location;
    }

    private void addAffiliates(){
        LatLng location1 = new LatLng(32.065831, 34.774922);
        LatLng location2 = new LatLng(32.065560, 34.778913);
        LatLng location3 = new LatLng(32.065076, 34.773394);
        Affiliate affiliate1 = new Affiliate("Barack", location1,
                "9:00-19:00", BitmapFactory.decodeResource(getResources(), R.drawable.rating_3), "111111",
                BitmapFactory.decodeResource(getResources(), R.drawable.babu), "36 Montefiore, Tel Aviv");
        Affiliate affiliate2 = new Affiliate("Bill", location2,
                "8:00-18:00", BitmapFactory.decodeResource(getResources(), R.drawable.rating_5), "111111",
                BitmapFactory.decodeResource(getResources(), R.drawable.babu), "19 Maze, Tel Aviv");
        Affiliate affiliate3 = new Affiliate("George", location3,
                "9:00-20:00", BitmapFactory.decodeResource(getResources(), R.drawable.rating_4), "111111",
                BitmapFactory.decodeResource(getResources(), R.drawable.babu), "15 Yavne, Tel Aviv");
        mAffiliates.put(location1, affiliate1);
        mAffiliates.put(location2, affiliate2);
        mAffiliates.put(location3, affiliate3);
    }

    private void showMarkers(){

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(STUB_LAT, STUB_LONG), 16));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
//                new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), 16));
        LatLng userPosition = new LatLng(STUB_LAT, STUB_LONG);
//        LatLng userPosition = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.userposition))
                .position(userPosition));
        MarkerOptions selected = null;
        for(LatLng af : mAffiliates.keySet()){

            if(selected == null){
                selected = new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.affiliate_on))
                        .position(af);
                selectedMarker = mMap.addMarker(selected);
            }else{
                mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.affiliate_off))
                        .position(af));
            }
        }
        if(selected != null){
            updateUIWithAffiliateDetails(mAffiliates.get(selected.getPosition()));
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.equals(selectedMarker)) {
                    return false;
                } else {
                    if (selectedMarker != null) {
                        selectedMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.affiliate_off));
                    }
                    selectedMarker = marker;
                    selectedMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.affiliate_on));
                }
                Affiliate selected = mAffiliates.get(marker.getPosition());
                updateUIWithAffiliateDetails(selected);
                return false;
            }
        });
    }

    private void updateUIWithAffiliateDetails(final Affiliate af){
        afImage.setImageDrawable(new BitmapDrawable(getResources(), af.getImage()));
        afRating.setImageDrawable(new BitmapDrawable(getResources(), af.getRating()));
        afName.setText(af.getName());
        afAddress.setText(af.getAddress());
        afHours.setText(af.getOpeningHours());
        LatLng afLoc = af.getLocation();
        afDistance.setText(String.valueOf(LocationUtils.distance(afLoc.latitude, afLoc.longitude, STUB_LAT, STUB_LONG)) + "m");
        afPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + af.getPhone()));
                PickupLocationActivity.this.startActivity(callIntent);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(mLastLocation != null){
            showMarkers();
        }
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if(mMap != null && mLastLocation != null && !mHideMarkers){
            showMarkers();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == ReservationActivity.REQUEST_CODE){
            if(resultCode == RESULT_OK){
                //TODO add marker, add textView
                mMap.clear();
                mHideMarkers = true;
                mAmountTextView.setVisibility(View.VISIBLE);
                mButton.setText(getText(R.string.pickup_reserve));
                mButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(confirmAffiliateTask == null){
                            //TODO show progress
                            confirmAffiliateTask = new ConfirmAffiliateTask();
                            confirmAffiliateTask.execute((Void) null);
                        }
                    }
                });
            }
        }
    }

    private class ConfirmAffiliateTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: get QR code
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            confirmAffiliateTask = null;
            //TODO hide progress
            if (success) {
                startActivity(new Intent(PickupLocationActivity.this, PickupConfirmationActivity.class));
            } else {
                //TODO show server errors
            }
        }

        @Override
        protected void onCancelled() {
            confirmAffiliateTask = null;
            //TODO hide progress
        }
    }

}
