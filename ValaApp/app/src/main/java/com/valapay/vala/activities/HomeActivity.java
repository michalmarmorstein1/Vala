package com.valapay.vala.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.valapay.vala.R;
import com.valapay.vala.Vala;
import com.valapay.vala.common.CashCollectRequestMessage;
import com.valapay.vala.model.Affiliate;
import com.valapay.vala.model.User;
import com.valapay.vala.network.NetworkServices;
import com.valapay.vala.utils.RoundImage;

import java.io.IOException;

import retrofit2.Response;

public class HomeActivity extends NavigationDrawerActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_LOCATION = 1;

    private GetAffiliatesTask getAffiliatesTask = null;
    private View mProgressView;
    private View mImage;
    private View mLocationTextView;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Button mCollectButton;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        user = Vala.getUser();
        mProgressView = findViewById(R.id.home_progress);
        mImage = findViewById(R.id.userImage);
        mLocationTextView = findViewById(R.id.textViewLocation);
        TextView tv = (TextView) findViewById(R.id.textViewName);
        String str = getString(R.string.home_name, user.getFirstName());
        Spannable wordToSpan = new SpannableString(str);
        wordToSpan.setSpan(new StyleSpan(Typeface.BOLD), str.indexOf(user.getFirstName()), wordToSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(wordToSpan);

        tv = (TextView) findViewById(R.id.textViewAmount);
        wordToSpan = new SpannableString(user.getBalanceString());
        wordToSpan.setSpan(new StyleSpan(Typeface.BOLD), 1, wordToSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(wordToSpan);

        mCollectButton = (Button) findViewById(R.id.collect_btn);
        mCollectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mLastLocation == null){
                    Toast.makeText(HomeActivity.this, getString(R.string.home_location_disabled_msg), Toast.LENGTH_LONG).show();
                }else{
                    if (getAffiliatesTask == null) {
                        showProgress(true);
                        getAffiliatesTask = new GetAffiliatesTask();
                        getAffiliatesTask.execute((Void) null);
                    }
                }
            }
        });

        Button sendButton = (Button) findViewById(R.id.send_btn);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, SendActivity.class));
            }
        });

        ImageView userImage = (ImageView) findViewById(R.id.userImage);
        RoundImage roundedImage = new RoundImage(user.getImageBitmap());
        userImage.setImageDrawable(roundedImage);
    }

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mImage.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_home;
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        final LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("VALA", "HomeActivity:onLocationChanged()");
                mLastLocation = location;
                checkLocation();
                if (ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                locationManager.removeUpdates(this);
            }


            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.d("VALA", "HomeActivity:onStatusChanged()");
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.d("VALA", "HomeActivity:onProviderEnabled()");
            }


            @Override
            public void onProviderDisabled(String provider) {
                Log.d("VALA", "HomeActivity:onProviderDisabled()");
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
//        locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, locationListener, null);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    private void saveLocation(){
        Log.d("VALA", "HomeActivity:saveLocation()");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_LOCATION);
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        checkLocation();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("VALA", "HomeActivity:onConnected()");
        saveLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private void checkLocation(){
        Log.d("VALA", "HomeActivity:checkLocation() - " + mLastLocation);
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(mLastLocation != null){
            mLocationTextView.setVisibility(View.INVISIBLE);
        }else{
            if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                mLocationTextView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d("VALA", "HomeActivity:onRequestPermissionsResult()");
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    }
                }
                checkLocation();
                return;
            }
        }
    }

    public class GetAffiliatesTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            Response<CashCollectRequestMessage> collectResponse = null;
            CashCollectRequestMessage requestBody = new CashCollectRequestMessage();
            requestBody.setUserId(user.getUserId());
            requestBody.setAmount(user.getBalanceString());
            requestBody.setCurrency(user.getCurrency());
            requestBody.setLocationLat(mLastLocation.getLatitude());
            requestBody.setLocationLong(mLastLocation.getLongitude());
            try {
                collectResponse = NetworkServices.getTestService().collect(requestBody).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(collectResponse.isSuccessful()){
                CashCollectRequestMessage data = collectResponse.body();
                for(com.valapay.vala.common.Affiliate af : data.getAffiliates()){
                    LatLng afLoc = new LatLng(af.getLocationLat(), af.getLocationLong());
                    Affiliate affiliate = new Affiliate(af.getName(), afLoc, af.getHoursOpen() +
                            "-" + af.getHoursClose(), mapRatingToImage(af.getRating()),
                            af.getPhoneNumber(), BitmapFactory.decodeResource(getResources(), R.drawable.babu),
                            af.getAddress(), af.getUserId());
                    user.getAffiliates().put(afLoc, affiliate);
                }
                return true;
            }else{
                Log.d("VALA", "HomeActivity:GetAffiliatesTask.doInBackground() - collect failed");
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            getAffiliatesTask = null;
            showProgress(false);
            if (success) {
                PickupLocationActivity.startActivity(HomeActivity.this, mLastLocation);
            } else {
                Toast.makeText(HomeActivity.this, R.string.general_error_message, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            getAffiliatesTask = null;
            showProgress(false);

        }
    }
    private Bitmap mapRatingToImage(String rating){
        double d = Double.valueOf(rating);
        int i = (int) d;
        switch(i){
            case 5:
                return BitmapFactory.decodeResource(getResources(), R.drawable.rating_5);
            case 4:
                return BitmapFactory.decodeResource(getResources(), R.drawable.rating_4);
            case 3:
                return BitmapFactory.decodeResource(getResources(), R.drawable.rating_3);
            case 2:
                return BitmapFactory.decodeResource(getResources(), R.drawable.rating_2);
            default:
                return BitmapFactory.decodeResource(getResources(), R.drawable.rating_1);
        }
    }

}
