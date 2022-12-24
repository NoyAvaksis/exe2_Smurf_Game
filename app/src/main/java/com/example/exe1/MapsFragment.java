package com.example.exe1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    //defult jerusalem
    private double lat = 31.771959;
    private double lag = 35.217018;
    private String tag_name = "default place";

    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        SupportMapFragment mapFragment = ((SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map));

        mapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        ArrayList<UserDetails> userDetails = DataManager.getDataManager().getTopRecords().getTop_records();
        if (userDetails.size() == 0)
            return;
        mMap = googleMap;
        mMap.clear();
        userDetails.forEach(user -> {
            LatLng sydney = new LatLng(user.getLat(), user.getLag());
            mMap.addMarker(new MarkerOptions().position(sydney).title(user.getName()));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            mMap.setMinZoomPreference(7);
        });
    }
}
