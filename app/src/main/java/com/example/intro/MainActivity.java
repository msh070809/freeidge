package com.example.intro;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.intro.R;
import com.example.intro.menu.FragmentPage1;
import com.example.intro.menu.FragmentPage2;
import com.example.intro.menu.FragmentPage3;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FragmentPage1 fragmentp1 = new FragmentPage1();
    private FragmentPage2 fragmentp2 = new FragmentPage2();
    private FragmentPage3 fragmentp3 = new FragmentPage3();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //상태바 제거
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //액션바 사라지게 하기
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setContentView(R.layout.activity_main); //메인 화면 레이아웃 연결 -> 프래그먼트 아님

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_layout, fragmentp1).commitAllowingStateLoss(); // 가장 먼저 등장할 프래그먼트 화면 레이아웃 fragmentSearch

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view); //메인화면에서, bottmnavigationview 연결
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());
    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{ //bottomnavigaion에서 각 아이템을 클릭했을 때

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch(menuItem.getItemId())
            {
                case R.id.navigation_1:
                    transaction.replace(R.id.content_layout, fragmentp1).commitAllowingStateLoss();

                    break;
                case R.id.navigation_2:
                    transaction.replace(R.id.content_layout, fragmentp2).commitAllowingStateLoss();
                    break;
                case R.id.navigation_3:
                    transaction.replace(R.id.content_layout, fragmentp3).commitAllowingStateLoss();
                    break;
            }
            return true;
        }
    }
}
