package com.stoyanov.developer.goevent.ui.common;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.stoyanov.developer.goevent.R;
import com.stoyanov.developer.goevent.mvp.model.domain.Event;

public class EventMarkerClusterRenderer extends DefaultClusterRenderer<Event> {
    private final IconGenerator mIconGenerator;

    public EventMarkerClusterRenderer(Context context, GoogleMap map, ClusterManager<Event> clusterManager) {
        super(context, map, clusterManager);
        mIconGenerator = new IconGenerator(context);
        mIconGenerator.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_marker_red_32px, null));
    }

    @Override
    protected void onClusterItemRendered(Event clusterItem, Marker marker) {
        super.onClusterItemRendered(clusterItem, marker);
    }

    @Override
    protected void onClusterRendered(Cluster<Event> cluster, Marker marker) {
        super.onClusterRendered(cluster, marker);
    }

    @Override
    protected void onBeforeClusterRendered(Cluster<Event> cluster, MarkerOptions markerOptions) {
        super.onBeforeClusterRendered(cluster, markerOptions);
    }

    @Override
    protected void onBeforeClusterItemRendered(Event item, MarkerOptions markerOptions) {
        markerOptions.icon(BitmapDescriptorFactory
                .fromBitmap(mIconGenerator.makeIcon()));
    }
}
