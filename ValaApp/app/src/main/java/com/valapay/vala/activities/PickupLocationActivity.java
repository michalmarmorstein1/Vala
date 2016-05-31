package com.valapay.vala.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.valapay.vala.R;
import com.valapay.vala.Vala;
import com.valapay.vala.model.Affiliate;
import com.valapay.vala.model.User;
import com.valapay.vala.utils.LocationUtils;
import com.valapay.vala.utils.RoundImage;

import java.util.Iterator;

public class PickupLocationActivity extends NavigationDrawerActivity implements OnMapReadyCallback {

    private static final float STUB_LAT = 32.064642f;
    private static final float STUB_LONG = 34.774431f;

    private static final String LOCATION_KEY = "LOCATION_KEY";

    private GoogleMap mMap;
    private Location mLastLocation;
    private Button mButton;
    private boolean mHideMarkers;
    private TextView mAmountTextView;
    private Marker selectedMarker;
    private ImageView afImage;
    private ImageView afRating;
    private TextView afName;
    private ImageButton afPhone;
    private TextView afAddress;
    private TextView afHours;
    private TextView afDistance;
    private LatLng mUserPosition;
    private User user;

    public static void startActivity(Context context, Location location){

        Intent intent = new Intent(context, PickupLocationActivity.class);
        intent.putExtra(LOCATION_KEY, location);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = Vala.getUser();
        mLastLocation = getIntent().getParcelableExtra(LOCATION_KEY);

        afImage = (ImageView) findViewById(R.id.imageViewAffiliate);
        afRating = (ImageView) findViewById(R.id.imageViewRating);
        afName = (TextView) findViewById(R.id.textViewAffiliateName);
        afPhone = (ImageButton) findViewById(R.id.imageButtonCall);
        afAddress = (TextView) findViewById(R.id.textViewAddress);
        afHours = (TextView) findViewById(R.id.textViewHours);
        afDistance = (TextView) findViewById(R.id.textViewdistance);;

        // Add map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Update UI
        ImageView userImage = (ImageView) findViewById(R.id.imageViewAffiliate);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.babu);
        RoundImage roundedImage = new RoundImage(bm);
        userImage.setImageDrawable(roundedImage);

        mAmountTextView = (TextView) findViewById(R.id.textViewAmount);
        Spannable wordToSpan = new SpannableString(getString(R.string.pickup_amount, user.getBalanceString())); //TODO replace with amount from server response
        wordToSpan.setSpan(new StyleSpan(Typeface.BOLD), user.getCurrency().length(), wordToSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mAmountTextView.setText(wordToSpan);

        mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Affiliate af = user.getAffiliates().get(selectedMarker.getPosition());
                ReservationActivity.startCollectActivity(af.getId(), af.getName(),
                //TODO send amount according to server response
                        user.getBalanceString(), PickupLocationActivity.this);
            }
        });

        addAffiliates(); //TODO remove to use server affiliates
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
                "9:00-19:00", BitmapFactory.decodeResource(getResources(), R.drawable.rating_3),
                "111111", BitmapFactory.decodeResource(getResources(), R.drawable.babu), "36 Montefiore, Tel Aviv", "aaa");
        Affiliate affiliate2 = new Affiliate("Bill", location2,
                "8:00-18:00", BitmapFactory.decodeResource(getResources(), R.drawable.rating_5), "111111",
                BitmapFactory.decodeResource(getResources(), R.drawable.babu), "19 Maze, Tel Aviv", "aaa");
        Affiliate affiliate3 = new Affiliate("George", location3,
                "9:00-20:00", BitmapFactory.decodeResource(getResources(), R.drawable.rating_4), "111111",
                BitmapFactory.decodeResource(getResources(), R.drawable.babu), "15 Yavne, Tel Aviv", "aaa");
        user.getAffiliates().clear();
        user.getAffiliates().put(location1, affiliate1);
        user.getAffiliates().put(location2, affiliate2);
        user.getAffiliates().put(location3, affiliate3);
    }

    private void showMarkers(){

        // Show user location
        mUserPosition = new LatLng(STUB_LAT, STUB_LONG); //TODO replace with next line to get real user location
//        mUserPosition = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mUserPosition, 16));
//        Iterator<LatLng> it = user.getAffiliates().keySet().iterator();
//        LatLng loc = it.next();
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 16)); //TODO uncomment to focus camera on affiliate
        mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.userposition))
                .position(mUserPosition));

        // Show affiliate markers
        MarkerOptions selected = null;
        for(LatLng af : user.getAffiliates().keySet()){
            if(selected == null){ // TODO select closest affiliate instead of the first one on the list
                selected = new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.affiliate_on))
                        .position(af);
                selectedMarker = mMap.addMarker(selected);
            }else{ //Add other affiliate markers to the map
                mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.affiliate_off))
                        .position(af));
            }
        }
        if (selected != null) {
            updateUIWithAffiliateDetails(user.getAffiliates().get(selected.getPosition()));
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {

                if(mHideMarkers){ //User already made a reservation - do nothing
                    return false;
                }
                Affiliate selected = user.getAffiliates().get(marker.getPosition());
                if (marker.equals(selectedMarker)) { //User pressed a marker twice - do nothing
                    return false;
                } else if(selected != null){ //User pressed an affiliate marker - update UI
                    if (selectedMarker != null) {
                        selectedMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.affiliate_off));
                    }
                    selectedMarker = marker;
                    selectedMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.affiliate_on));
                    updateUIWithAffiliateDetails(selected);
                }
                return false;
            }
        });
    }

    private void updateUIWithAffiliateDetails(final Affiliate af){

        RoundImage roundedImage = new RoundImage(af.getImage());
        afImage.setImageDrawable(roundedImage);
        afRating.setImageDrawable(new BitmapDrawable(getResources(), af.getRating()));
        afName.setText(af.getName());
        afAddress.setText(af.getAddress());
        afHours.setText(af.getOpeningHours());
        LatLng afLoc = af.getLocation();
        afDistance.setText(String.valueOf(LocationUtils.distance(afLoc.latitude, afLoc.longitude,
                mUserPosition.latitude, mUserPosition.longitude)) + "m");
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == ReservationActivity.REQUEST_CODE){
            if(resultCode == RESULT_OK){
                updateUIOnReservation();
                //Start Google Maps navigation
//                String navigationPath = "google.navigation:q=" + selectedMarker.getPosition().latitude +
//                        "," + selectedMarker.getPosition().longitude + "&mode=w";
//                Uri gmmIntentUri = Uri.parse(navigationPath);
//                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//                mapIntent.setPackage("com.google.android.apps.maps");
//                startActivity(mapIntent);
            }
        }
    }

    private void updateUIOnReservation(){
        mMap.clear();
        mHideMarkers = true;
        mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.userposition))
                .position(mUserPosition));
        mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.affiliate_on))
                .position(selectedMarker.getPosition()));
        mAmountTextView.setVisibility(View.VISIBLE);
        mButton.setText(getText(R.string.pickup_reserve));
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PickupLocationActivity.this, PickupConfirmationActivity.class));
            }
        });
    }
}
