package com.bmh.trackchild.UI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bmh.trackchild.Activities.ChildLocationActivity;
import com.bmh.trackchild.Activities.MainActivity;
import com.bmh.trackchild.R;
import com.bmh.trackchild.Tools.SharedPrefs;
import com.bmh.trackchild.Tools.StaticValues;

import de.hdodenhof.circleimageview.CircleImageView;

public class Drawer implements NavigationView.OnNavigationItemSelectedListener {

    private AppCompatActivity mActivity;
    private Toolbar toolbar;
    SharedPrefs sharedPrefs;


    public Drawer(AppCompatActivity activity) {
        this.mActivity = activity;
        sharedPrefs = new SharedPrefs(mActivity);
        setActionBar();
        setDrawer();
    }

    private void setActionBar() {
        toolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
        mActivity.setSupportActionBar(toolbar);
    }

    private void setDrawer() {
        DrawerLayout drawer = (DrawerLayout) mActivity.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                mActivity, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) mActivity.findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        String userType = sharedPrefs.getPreferences(R.string.Key_UserType, "");

        if (userType.equals(StaticValues.USER_IS_PARENT)) {
            menu.setGroupCheckable(R.id.groupParent, true, true);
            menu.setGroupVisible(R.id.groupParent, true);
            menu.setGroupCheckable(R.id.groupChild, false, false);
            menu.setGroupVisible(R.id.groupChild, false);
        } else {
            menu.setGroupCheckable(R.id.groupParent, false, false);
            menu.setGroupVisible(R.id.groupParent, false);
            menu.setGroupCheckable(R.id.groupChild, true, true);
            menu.setGroupVisible(R.id.groupChild, true);
        }
        View hView = navigationView.getHeaderView(0);
        TextView tvNavUserName = (TextView) hView.findViewById(R.id.tvNavUserName);
        CircleImageView ivNavUserProfile = (CircleImageView) hView.findViewById(R.id.ivNavUserProfile);


        if (userType.equals(StaticValues.USER_IS_PARENT)) {
            setHeaderAndItem(ivNavUserProfile, tvNavUserName,
                    sharedPrefs.getPreferences(R.string.Key_ParentImage, ""),
                    sharedPrefs.getPreferences(R.string.Key_ParentName, ""));
        } else {
            setHeaderAndItem(ivNavUserProfile, tvNavUserName,
                    sharedPrefs.getPreferences(R.string.Key_ChildImage, ""),
                    sharedPrefs.getPreferences(R.string.Key_ChildName, ""));
        }
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int menuItemId = menuItem.getItemId();

        if (menuItemId == R.id.nav_logout || menuItemId == R.id.nav_parentLogout) {
            // Handle the logout  action
            sharedPrefs.clearPreferences();
            mActivity.startActivity(new Intent(mActivity, MainActivity.class));
            mActivity.finish();
        }


        DrawerLayout drawer = (DrawerLayout) mActivity.findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    @SuppressLint("NewApi")
    private void setHeaderAndItem(ImageView ivNavUserProfile, TextView tvNavUserName,
                                  String imagePath, String userName) {
        if (imagePath.equals("")) {
            ivNavUserProfile.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.user));
        } else {
            ivNavUserProfile.setImageURI(Uri.parse(imagePath));
        }
        tvNavUserName.setText(userName);
    }


}
