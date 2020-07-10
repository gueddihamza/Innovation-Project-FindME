package com.project.findme.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.findme.HelperClasses.HomeAdapter.FeaturedAdapter;
import com.project.findme.HelperClasses.HomeAdapter.FeaturedHelperClass;
import com.project.findme.HelperClasses.HomeAdapter.FriendHelperClass;
import com.project.findme.HelperClasses.HomeAdapter.FriendsAdapter;
import com.project.findme.HelperClasses.HomeAdapter.FriendsHelperClass;
import com.project.findme.HelperClasses.HomeAdapter.GeolocationAdapter;
import com.project.findme.HelperClasses.HomeAdapter.GeolocationHelperClass;
import com.project.findme.HelperClasses.UserInfo;
import com.project.findme.R;
import com.project.findme.common.Authentication.StartScreen;

import java.util.ArrayList;
import java.util.List;

public class UserDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, FeaturedAdapter.onFeaturedListener {
    static final float END_SCALE = 0.7f;
    RecyclerView featuredRecycler;
    RecyclerView friendsRecycler;
    RecyclerView geolocationRecycler;
    ImageView menu;
    LinearLayout contentView;
    ArrayList<FeaturedHelperClass> featuredLocations = new ArrayList<>();
    FirebaseDatabase database;
    List<String> friendsUID = new ArrayList<>();
    ArrayList<FriendsHelperClass> friendsList;


    RecyclerView.Adapter adapter;
    private GradientDrawable gradient1, gradient2, gradient3, gradient4;


    //
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    public void callGroupScreen(View view) {
        startActivity(new Intent(getApplicationContext(), GroupsMain.class));
    }

    public void callGeolocationScreen(View view) {
        startActivity(new Intent(getApplicationContext(), UserLocalization.class));
    }

    public void callFriendsScreen(View view) {
        startActivity(new Intent(getApplicationContext(), FriendsMain.class));
    }

    public void callProfileScreen(View view) {
        startActivity(new Intent(getApplicationContext(), UserProfile.class));
    }

    public void callClassGroup(View view) {
        startActivity(new Intent(getApplicationContext(), ClassGroups.class));
    }

    public void callClubGroup(View view) {
        startActivity(new Intent(getApplicationContext(), ClubGroups.class));
    }

    public void callCustomGroup(View view) {
        startActivity(new Intent(getApplicationContext(), CustomGroups.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_dashboard);


        featuredRecycler = findViewById(R.id.featured_recycler);
        friendsRecycler = findViewById(R.id.friends_recycler);
        geolocationRecycler = findViewById(R.id.geolocation_recycler);

        //Methods
        featuredRecycler();
        friendsRecycler();
        geolocationRecycler();

        //Menu
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        menu = findViewById(R.id.menu_icon);
        contentView = findViewById(R.id.content);

        //NavigationDrawer
        navigationDrawer();


    }

    private void navigationDrawer() {
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
        animateNavigationDrawer();
    }

    private void animateNavigationDrawer() {
        drawerLayout.setScrimColor(getResources().getColor(R.color.colorPrimary));
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //Scale the view
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                //Translate the view , accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.nav_all_groups:
                startActivity(new Intent(getApplicationContext(), GroupsMain.class));
                break;


            case R.id.nav_search:
                startActivity(new Intent(getApplicationContext(), UsersSearch.class));
                break;

            case R.id.nav_profile:
                callProfileScreen(new View(this));
                break;

            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, StartScreen.class));
                finish();
                break;


        }
        return true;
    }


    private void friendsRecycler() {

        friendsRecycler.setHasFixedSize(true);
        friendsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        friendsList = new ArrayList<>();
        FillFriendsList();

    }

    private void FillFriendsList() {
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            startActivity(new Intent(getApplicationContext(), StartScreen.class));
            finish();
        } else {
            database.getReference("users").child(user.getUid()).child("friends")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        if(ds.getValue(FriendHelperClass.class).getStatus().equals("True")){
                            friendsUID.add(ds.getKey());
                        }
                    }
                    getInfos();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

    private void getInfos() {
        if(friendsUID==null || friendsUID.size()==0){
            return;
        }
        else{
            for(String uid : friendsUID){
                FirebaseDatabase.getInstance().getReference("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UserInfo userInfo=dataSnapshot.getValue(UserInfo.class);
                        friendsList.add(new FriendsHelperClass(userInfo.getFullName(),userInfo.getUsername()));
                        NotifyAdapter();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




            }


        }
    }

    private void NotifyAdapter() {
        adapter = new FriendsAdapter(friendsList);
        friendsRecycler.setAdapter(adapter);
    }

    private void geolocationRecycler() {
        gradient1 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xff7adccf, 0xff7adccf});
        gradient2 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xffd4cbe5, 0xffd4cbe5});
        gradient3 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xfff7c59f, 0xFFf7c59f});
        gradient4 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xffb8d7f5, 0xffb8d7f5});

        ArrayList<GeolocationHelperClass> geolocationHelperClasses = new ArrayList<>();
        geolocationHelperClasses.add(new GeolocationHelperClass(R.drawable.location_icon, "Open Map", gradient1));
        geolocationHelperClasses.add(new GeolocationHelperClass(R.drawable.location_icon, "Localize Your Friends", gradient2));
        geolocationHelperClasses.add(new GeolocationHelperClass(R.drawable.location_icon, "Locate Someone", gradient3));


        geolocationRecycler.setHasFixedSize(true);
        adapter = new GeolocationAdapter(geolocationHelperClasses);
        geolocationRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        geolocationRecycler.setAdapter(adapter);
    }

    private void featuredRecycler() {

        featuredRecycler.setHasFixedSize(true);
        featuredRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        featuredLocations.add(new FeaturedHelperClass(R.drawable.university_icon, this.getString(R.string.class_groups), this.getString(R.string.more_details)));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.club_group_icon, this.getString(R.string.club_groups), this.getString(R.string.more_details)));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.custom_group_icon, this.getString(R.string.custom_groups), this.getString(R.string.more_details)));

        adapter = new FeaturedAdapter(featuredLocations, this);
        featuredRecycler.setAdapter(adapter);

    }


    @Override
    public void onFeaturedClick(int position) {
        if (position == 0) {
            callClassGroup(new View(this));
        } else if (position == 1) {
            callClubGroup(new View(this));
        } else if (position == 2) {
            callCustomGroup(new View(this));
        }

    }
}
