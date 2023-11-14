package in.macrocodes.creatives;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import in.macrocodes.creatives.Adapter.HomePageFragmentAdapter;
import in.macrocodes.creatives.DatabaseHelper.DatabaseHelper;
import in.macrocodes.creatives.Models.CartSize;
import in.macrocodes.creatives.Models.TrendingProducts;
import in.macrocodes.creatives.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    HomePageFragmentAdapter mSectionsPagerAdapter;
    ActivityMainBinding binding;
    private MenuItem prevMenuItem;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);


        mSectionsPagerAdapter = new HomePageFragmentAdapter(getSupportFragmentManager());
        binding.viewpager.setAdapter(mSectionsPagerAdapter);
        binding.viewpager.setOffscreenPageLimit(0);
        binding.cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,CartActivity.class);
                startActivity(intent);
            }
        });
        databaseHelper = new DatabaseHelper(this);
        Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {

                ArrayList<TrendingProducts> arrayList = new ArrayList<>();
                arrayList = databaseHelper.getAllData();
                if (arrayList.size() == 0){
                    binding.cartCount.setVisibility(View.GONE);
                }else{
                    binding.cartCount.setText(String.valueOf(arrayList.size()));
                    binding.cartCount.setVisibility(View.VISIBLE);
                }
            }
        },1000);



        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.splashLayout.setVisibility(View.GONE);
            }
        },1500);
        //binding.viewpager.setPageTransformer(true,new ZoomOutTransformation());
        handleBottomNav();
    }

    private void handleBottomNav(){
        binding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch(menuItem.getItemId()) {
                    case R.id.home:
                        binding.viewpager.setCurrentItem(0);
                        binding.title.setText("Home");
                        break;


                    case R.id.search:

                        binding.viewpager.setCurrentItem(1);
                        binding.title.setText("Search");
                        break;

                    case R.id.profile:
                        binding.viewpager.setCurrentItem(2);
                        binding.title.setText("My Profile");
                        break;

                }
                return false;
            }
        });
        binding.viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null)
                    prevMenuItem.setChecked(false);
                else
                    binding.bottomNavigation.getMenu().getItem(0).setChecked(false);

                binding.bottomNavigation.getMenu().getItem(position).setChecked(true);
                prevMenuItem = binding.bottomNavigation.getMenu().getItem(position);

                switch(prevMenuItem.getItemId()) {
                    case R.id.home:
                        binding.viewpager.setCurrentItem(0);
                        binding.title.setText("Home");
                        break;


                    case R.id.search:
                        binding.viewpager.setCurrentItem(1);
                        binding.title.setText("Search");
                        break;


                    case R.id.profile:
                        binding.viewpager.setCurrentItem(2);
                        binding.title.setText("My Profile");
                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();


    }
}