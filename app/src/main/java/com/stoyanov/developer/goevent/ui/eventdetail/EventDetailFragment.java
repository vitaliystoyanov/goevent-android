package com.stoyanov.developer.goevent.ui.eventdetail;

import android.animation.Animator;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.stoyanov.developer.goevent.R;
import com.stoyanov.developer.goevent.di.component.DaggerFragmentComponent;
import com.stoyanov.developer.goevent.manager.NavigationManager;
import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.model.domain.Location;
import com.stoyanov.developer.goevent.ui.container.ContainerActivity;
import com.stoyanov.developer.goevent.utill.DateUtil;
import com.stoyanov.developer.goevent.utill.transition.DetailTransition;

import org.parceler.Parcels;

import javax.inject.Inject;

import at.blogc.android.views.ExpandableTextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class EventDetailFragment extends Fragment
        implements EventDetailView, OnMapReadyCallback {
    public static final String EXTRA_PARCELABLE_EVENT = "EXTRA_PARCELABLE_EVENT";
    public static final String EXTRA_TRANSITION_NAME = "EXTRA_TRANSITION_NAME";
    public static final int ZOOM_LEVEL = 16;
    @Inject
    NavigationManager navigationManager;
    @Inject
    EventDetailPresenter presenter;

    @BindView(R.id.detail_event_routes_map)
    MapView mapView;
    @BindView(R.id.detail_event_coordinator)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.expandable_textview)
    ExpandableTextView expandableTextView;
    @BindView(R.id.detail_event_info_duration_time)
    TextView durationTime;
    @BindView(R.id.detail_event_info_location_county_city)
    TextView countryAndCity;
    @BindView(R.id.detail_event_info_location_street)
    TextView street;
    @BindView(R.id.detail_event_image)
    ImageView image;
    @BindView(R.id.detail_event_name)
    TextView eventName;
    @BindView(R.id.event_info_category)
    TextView category;
    @BindView(R.id.detail_event_toolbar)
    Toolbar toolbar;
    @BindView(R.id.detail_event_card_map)
    CardView cardViewMap;
    @BindView(R.id.content)
    LinearLayout content;
    @BindView(R.id.nested_scroll)
    NestedScrollView nestedScroll;
    @BindView(R.id.bottom_sheet)
    RelativeLayout bottomSheet;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.img_btn_expand_collapse)
    Button imgBtnExpandCollapse;
    @BindView(R.id.event_map_button_open_map)
    Button eventMapButtonOpenMap;
    @BindView(R.id.btn_tickets)
    Button btnTickets;
    @BindView(R.id.btn_share)
    Button btnShare;

    private GoogleMap map;
    private LatLng location;
    private Unbinder unbinder;
    private IconGenerator generator;

    public static Fragment newInstance(Event event, String transitionName) {
        Bundle bundle = new Bundle();
        Fragment fragment = new EventDetailFragment();
        bundle.putParcelable(EXTRA_PARCELABLE_EVENT, Parcels.wrap(event));
        bundle.putString(EXTRA_TRANSITION_NAME, transitionName);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static Fragment newInstance(Event event) {
        Bundle bundle = new Bundle();
        Fragment fragment = new EventDetailFragment();
        bundle.putParcelable(EXTRA_PARCELABLE_EVENT, Parcels.wrap(event));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postponeEnterTransition();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSharedElementEnterTransition(new DetailTransition());
            setExitTransition(new DetailTransition());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_detail_event, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String transitionName = getArguments().getString(EXTRA_TRANSITION_NAME);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && transitionName != null) {
            image.setTransitionName(transitionName);
        }
        fab.hide();
        BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DaggerFragmentComponent.builder()
                .activityComponent(((ContainerActivity) getActivity()).getActivityComponent())
                .build()
                .inject(this);
        setupToolbar();
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        generator = new IconGenerator(getContext());
        generator.setBackground(ResourcesCompat.getDrawable(getResources(),
                R.drawable.ic_marker_red_32px, null));
        expandableTextView.addOnExpandListener(new ExpandableTextView.OnExpandListener() {
            @Override
            public void onExpand(@NonNull ExpandableTextView view) {
                imgBtnExpandCollapse.setText("Collapse");
            }

            @Override
            public void onCollapse(@NonNull ExpandableTextView view) {
                imgBtnExpandCollapse.setText("Read more");
            }
        });
    }

    @OnClick(R.id.fab)
    public void onClickStarFab() {
        presenter.onStarClick();
    }

    @OnClick(R.id.event_map_button_open_map)
    public void onClickOpenMap() {
        presenter.onOpenMapClick(location);
    }

    @OnClick(R.id.img_btn_expand_collapse)
    public void onClickExpandOrCollapse(View v) {
        expandableTextView.toggle();
    }

    private void setupToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((ContainerActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((ContainerActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(view -> navigationManager.back(EventDetailFragment.this));
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.attach(this);
        presenter.onStart(Parcels.unwrap(getArguments().getParcelable(EXTRA_PARCELABLE_EVENT)));
        startCircularAnimation(content);
        fab.show();
        bottomSheet.postDelayed(() -> BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED),
                400);
    }

    private void startCircularAnimation(View vg) {
        int cx = vg.getWidth() / 2;
        int cy = vg.getHeight() / 2;
        float finalRadius = (float) Math.hypot(cx, cy);
        Animator anim = ViewAnimationUtils.createCircularReveal(vg, cx, cy, 0, finalRadius);
        anim.setInterpolator(new AccelerateInterpolator());
        anim.setDuration(300);
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                vg.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                vg.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        anim.start();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detail_event_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
/*        if (itemId == R.id.toolbar_action_add_to_calendar) {
            presenter.onCalendarAddClick();
        }*/
        return true;
    }

    @Override
    public void showImage(String url) {
        if (url != null) {
            Picasso.with(getContext())
                    .load(url)
                    .fit()
                    .centerCrop()
                    .into(image, new Callback() {
                        @Override
                        public void onSuccess() {
                            startPostponedEnterTransition();
                        }

                        @Override
                        public void onError() {
                            startPostponedEnterTransition();
                        }
                    });
        } else {
            Picasso.with(getContext())
                    .load(R.drawable.ic_logo)
                    .fit()
                    .centerCrop()
                    .into(image);
        }
    }

    @Override
    public void showDescription(String desc, String name) {
        expandableTextView.setText(desc);
        eventName.setText(name);
        toolbar.setTitle("");
    }

    @Override
    public void showWhen(String dateFrom, String dateTo) {
        durationTime.setText(DateUtil.toDuration(DateUtil.toDate(dateFrom), DateUtil.toDate(dateTo)));
    }

    @Override
    public void showLocation(Location location) {
        if (location == null) {
            street.setText(R.string.field_no_location);
            countryAndCity.setVisibility(View.GONE);
            cardViewMap.setVisibility(View.GONE);
            return;
        }
        countryAndCity.setText(getResources()
                .getString(R.string.location_country_city_format,
                        location.getCity(), location.getCountry())
        );
        if (location.getStreet() != null && !location.getStreet().isEmpty()) {
            street.setText(location.getStreet());
        } else {
            street.setVisibility(View.GONE);
        }
        this.location = new LatLng(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void showRoutes() {
    }

    @Override
    public void showCategory(String category) {
        if (category == null || category.isEmpty()) {
            this.category.setText(R.string.field_no_category);
            return;
        }
        this.category.setText(category);
    }

    @Override
    public void showIsFavorite(boolean b) {
        fab.setImageResource(b ? R.drawable.ic_done_white_24dp : R.drawable.ic_star_white_24px);
    }

    @Override
    public void addToCalendar() {
    }

    @Override
    public void openGoogleMapApp(LatLng latLng) {
        NavigationManager.openGoogleMapApp(getContext(), latLng);
    }

    @Override
    public void showProgress(boolean b) {
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.getUiSettings().setAllGesturesEnabled(false);
        map.getUiSettings().setMapToolbarEnabled(false);
        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.google_maps_style);
        map.setMapStyle(style);

        if (location != null) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, ZOOM_LEVEL));
            MarkerOptions marker =
                    new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(generator.makeIcon()));
            marker.position(location);
            map.addMarker(marker);
        }
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.detach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapView != null) mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
