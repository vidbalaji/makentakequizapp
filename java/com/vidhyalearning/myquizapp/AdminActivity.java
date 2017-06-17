package com.vidhyalearning.myquizapp;

import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class AdminActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userName = getIntent().getStringExtra("UserName");
        setContentView(R.layout.activity_admin);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }
    public String getUserName(){
        return userName;
    }
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();

        if(position==1) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, ChangePasswordFragment.newInstance(userName))
                    .commit();
        }else if(position==0)
        {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, ListCategoryFragment.newInstance(userName))
                    .commit();
        }
        else if(position==2)
        {
            if(userName.equals("admin")) {
                fragmentManager.beginTransaction()
                        .replace(R.id.container, AddCategory.newInstance(userName))
                        .commit();
            }
            else
            {
                Toast.makeText(this,"Only admin has access to add category" ,Toast.LENGTH_SHORT).show();
            }

        }
        else if(position==3){
            if(userName.equals("admin")) {
                fragmentManager.beginTransaction()
                        .replace(R.id.container, deleteElement.newInstance("CATEGORY"))
                        .commit();
            }
            else
            {
                Toast.makeText(this,"Only admin has access to delete category" ,Toast.LENGTH_SHORT).show();
            }
        }
        else if(position==4){
            if(userName.equals("admin")) {

                fragmentManager.beginTransaction()
                        .replace(R.id.container, deleteElement.newInstance("USER"))
                        .commit();
            }
            else
            {
                Toast.makeText(this,"Only admin has access to delete user" ,Toast.LENGTH_SHORT).show();
            }
        }
        else if(position==5){
            if(userName.equals("admin")) {
                fragmentManager.beginTransaction()
                        .replace(R.id.container, XMLText.newInstance())
                        .commit();
            }
            else
            {
                Toast.makeText(this,"Only admin has access to this operation" ,Toast.LENGTH_SHORT).show();
            }

        }
        else if(position==6){
            if(userName.equals("admin")) {
                fragmentManager.beginTransaction()
                        .replace(R.id.container, downloadToXml.newInstance())
                        .commit();
            }
            else
            {
                Toast.makeText(this,"Only admin has access to this operation" ,Toast.LENGTH_SHORT).show();
            }

        }
        else if(position==7){
            fragmentManager.beginTransaction()
                    .replace(R.id.container, helpscreen.newInstance())
                    .commit();
        }
        else if(position==8){
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK,returnIntent);
            finish();
        }

    }


    public void onSectionAttached(int number) {
        switch (number) {
            case 2:
                mTitle = getString(R.string.title_section1);
                break;
            case 1:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
                break;
            case 5:
                mTitle = getString(R.string.title_section5);
                break;
            case 6:
                mTitle = getString(R.string.title_section6);
                break;
            case 7:
                mTitle = getString(R.string.title_section7);
                break;
            case 8:
                mTitle = "Sign Out";
                break;

        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    public void showNavDrawer(View view) {

        mNavigationDrawerFragment.getDrawer().openDrawer(GravityCompat.START);


    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_admin, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((AdminActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
