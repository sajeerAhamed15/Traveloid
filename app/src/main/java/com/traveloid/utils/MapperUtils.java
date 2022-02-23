package com.traveloid.utils;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;
import com.traveloid.model.Hike;
import com.traveloid.model.HikeSerializable;
import com.traveloid.model.LatLangSerializable;

import java.util.ArrayList;
import java.util.List;

public class MapperUtils {
    public static HikeSerializable convertToSerializable(Hike hike) {
        HikeSerializable h = new HikeSerializable();
        h.setId(hike.getId());
        h.setTitle(hike.getTitle());
        h.setDistance(hike.getDistance());
        h.setImage(hike.getImage());
        h.setPopular(hike.getPopular());
        h.setFeatured(hike.getFeatured());
        h.setPath(convertToSerializableLatLang(hike.getPath()));
        return h;
    }

    private static List<LatLangSerializable> convertToSerializableLatLang(List<GeoPoint> path) {
        List<LatLangSerializable> latLngs = new ArrayList<>();
        for (GeoPoint geoPoint : path) {
            latLngs.add(new LatLangSerializable(geoPoint.getLatitude(), geoPoint.getLongitude()));
        }
        return latLngs;
    }

    public static List<LatLng> convertToLatLang(List<LatLangSerializable> path) {
        List<LatLng> latLngs = new ArrayList<>();
        for (LatLangSerializable geoPoint : path) {
            latLngs.add(new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude()));
        }
        return latLngs;
    }
}
