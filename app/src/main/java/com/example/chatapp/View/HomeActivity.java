package com.example.chatapp.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.chatapp.R;
import com.example.chatapp.Menu.CallsFragment;
import com.example.chatapp.Menu.ChatFragment;
import com.example.chatapp.Menu.StatusFragment;
import com.example.chatapp.View.Setting.SettingActivity;
import com.example.chatapp.View.auth.RegistrationActivity;
import com.example.chatapp.View.contact.ContactActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends AppCompatActivity {

    FirebaseAuth auth;

    FirebaseDatabase database;

    // ImageView imgLogout, imgSetting;
    Toolbar toolbar;
    ViewPager viewPager;
    TabLayout tabLayout;

    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);


        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        floatingActionButton = findViewById(R.id.fab_action);


        // imgLogout = findViewById(R.id.img_logOUt);
        //  imgSetting = findViewById(R.id.img_settings);
        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tab_layout);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(HomeActivity.this, RegistrationActivity.class));

        }


        setUpWithViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeFabIcon(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

/*
        DatabaseReference reference = database.getReference().child("user");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    userArrayList.add(user);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));

        }
/*
        imgSetting.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, SettingActivity.class));

        });
*/


    }


    private void setUpWithViewPager(ViewPager viewPager) {

        HomeActivity.SectionPagerAdapter adapter = new SectionPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new ChatFragment(), "Chats");
        adapter.addFragment(new StatusFragment(), "Status");
        adapter.addFragment(new CallsFragment(), "Calls");

        viewPager.setAdapter(adapter);

    }

    private static class SectionPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public SectionPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);

        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.menu_main, menu);


        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.search_ic:
                Toast.makeText(HomeActivity.this, "Action Search", Toast.LENGTH_SHORT).show();
                break;
            case R.id.new_group:
                Toast.makeText(HomeActivity.this, "New group", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_broadcast:
                Toast.makeText(HomeActivity.this, "Action Broadcast", Toast.LENGTH_SHORT).show();
                break;
            case R.id.whatsApp_Web:
                Toast.makeText(HomeActivity.this, "WhatsApp Web", Toast.LENGTH_SHORT).show();
                break;
            case R.id.Start_Message:
                Toast.makeText(HomeActivity.this, "Start Message", Toast.LENGTH_SHORT).show();
                break;
            case R.id.setting:
                startActivity(new Intent(HomeActivity.this, SettingActivity.class));
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    public void changeFabIcon(final int index) {
        floatingActionButton.hide();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (index) {
                    case 0:
                        floatingActionButton.setImageDrawable(getDrawable(R.drawable.ic_chat));
                        floatingActionButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(HomeActivity.this, ContactActivity.class));

                            }
                        });

                        break;
                    case 1:
                        floatingActionButton.setImageDrawable(getDrawable(R.drawable.baseline_edit));
                        break;
                    case 2:
                        floatingActionButton.setImageDrawable(getDrawable(R.drawable.ic_audiotrack_dark));
                        break;
                }
                floatingActionButton.show();
            }
        }, 500);


    }
}












        /*
        imgLogout.setOnClickListener(v -> {
            TextView yesBtn, noBtn;

            Dialog dialog = new Dialog(HomeActivity.this, R.style.Dialog);
            dialog.setContentView(R.layout.dialog_layout);
            yesBtn = findViewById(R.id.yesBtn);
            noBtn = findViewById(R.id.noBtn);
            dialog.show();

            /*
            yesBtn.setOnClickListener(v1 -> {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeActivity.this, RegistrationActivity.class));
            });




        });
*/


