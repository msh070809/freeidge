package com.example.intro.firebase;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.intro.R;
import com.example.intro.menu.FragmentPage1;
import com.example.intro.menu.FragmentPage2;
import com.example.intro.menu.FragmentPage3;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class MainDbActivity extends AppCompatActivity {
    private BottomNavigationView mBottomNV;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        mBottomNV = findViewById(R.id.nav_view);
        mBottomNV.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() { //NavigationItemSelecte
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                BottomNavigate(menuItem.getItemId());
                return true;
            }
        });
        mBottomNV.setSelectedItemId(R.id.navigation_1);
        getWebsite();
    }
    private void BottomNavigate(int id) {  //BottomNavigation 페이지 변경
        String tag = String.valueOf(id);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment currentFragment = fragmentManager.getPrimaryNavigationFragment();
        if (currentFragment != null) {
            fragmentTransaction.hide(currentFragment);
        }

        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment == null) {
            if (id == R.id.navigation_1) {
                fragment = new FragmentPage1();

            } else if (id == R.id.navigation_2){

                fragment = new FragmentPage2();
            }else {
                fragment = new FragmentPage3();
            }

            fragmentTransaction.add(R.id.content_layout, fragment, tag);
        } else {
            fragmentTransaction.show(fragment);
        }

        fragmentTransaction.setPrimaryNavigationFragment(fragment);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commitNow();
    }
    private void getWebsite(){
        new Thread(new Runnable() {
            int j=0;
            @Override
            public void run() {
                database= FirebaseDatabase.getInstance();
                databaseReference=database.getReference();//db 연결하는 부 지정
                //string을 덧붙이면서 저장할 stringBuilder 객체 선언
                final StringBuilder builder_title = new StringBuilder();
                final StringBuilder builder_mainImage = new StringBuilder();
                final StringBuilder builder_ingre = new StringBuilder();
                final StringBuilder builder_steps = new StringBuilder();
                final StringBuilder builder_stepsImage = new StringBuilder();

                int t=0;//요리법의 개수를 세기 위한 변수
                int l=1;//요리법 개수 세기

                //i=크롤링할 주소 변수에 대한 변수
                for(int i=286 ; l<=100 ; i++) {
                    try {
                        //DB레시피 분류 번호 3자리화 (001~999)
                        String index;
                        if(l >= 100)
                            index = ""+l;
                        else if(l>=10)
                            index = "0"+l;
                        else
                            index = "00"+l;

                        ArrayList<Context> arraylist =new ArrayList<>();
                        databaseReference=database.getReference();//db 연결하는 부 지정

                        Recipe recipe= new Recipe();//받아온 데이터(전체적인 요리 제목,사진,재료) 저장할 객체인 recipe 생성

                        Document webPage = Jsoup.connect("https://www.10000recipe.com/recipe/6952" + i).get(); //크롤링할 주소

                        //제목 크롤링
                        String webTitle = webPage.title(); //웹페이지 제목을 불러옴
                        builder_title.append(webTitle); //제목 저장
                        recipe.setTitle(builder_title.toString());//제목을 db에 올리기 위해서 객체에 넣기

                        //메인이미지 크롤링
                        Elements mainImgClass = webPage.select("#main_thumbs"); //메인이미지가 삽입된 클래스 id
                        String mainImg = mainImgClass.attr("src"); //해당 클래스에서 src(이미지주소)부분 저장
                        builder_mainImage.append(mainImg); //src 문자열 저장
                        recipe.setImage(builder_mainImage.toString());//이미지를 db에 올리기 위해서 객체에 넣기

                        //재료 크롤링
                        Elements ingreClass = webPage.select("div.ready_ingre3"); //재료부분 div 클래스
                        Elements ingreUl = ingreClass.select("ul"); //재료부분 ul태그 분리
                        String ingre = ingreUl.text(); //ul태그 내에 텍스트를 저장
                        builder_ingre.append(ingre); //재료텍스트 문자열 저장
                        databaseReference.child("data").child("recipe" + index).setValue(recipe);
                        databaseReference.child("data").child("recipe" + index).child("ingredient").setValue(builder_ingre.toString());

                        databaseReference = database.getReference("data/recipe" + index + "/Context");//상세한 데이터(요리법, 요리법별 사진을 저장하기위한 db 위치 불러오기)
                        //요리법, 이미지 크롤링
                        for (j = 1; j < 30; j++) {   // j = 요리순서index
                            Context context = new Context();
                            Elements stepsClass = webPage.select("#stepdescr" + j); //요리법를 받아올 클래스 id
                            String steps = stepsClass.text();   //클래스 내의 텍스트 저장
                            if (steps.equals("")) { //마지막 요리법에 도달한경우
                                if(j==1)//처음부터 비어있다면
                                {
                                    l--; //레시피 번호를 뒤로 가고 break (이후 i가 증가하면서 기존의 l 내용들에 덮어씀)
                                    break;
                                }
                                else { //마지막에 도달한거라면
                                    j--; //index를 이전으로 설정 후 break
                                    break;
                                }
                            }
                            builder_steps.append(j+". ").append(steps).append("\n"); //요리법 문자열 저장
                            context.setRecipe(builder_steps.toString());
                            Elements stepsImgClass = webPage.select("#stepimg" + j); //요리사진을 받아올 클래스id
                            Elements stepsTag = stepsImgClass.select("img"); //클래스 내의 img태그 분리
                            String stepsImg = stepsTag.attr("src"); //img태그에서 src(이미지주소)를 불러옴


                            if (stepsImg.equals(""))                             //만약 요리사진이 없다면?
                            {
                                builder_stepsImage.append("no_image");//src가 저장될 곳에 no_image 문자열 저장
                            } else                                                //만약 요리사진이 있다면?
                            {
                                builder_stepsImage.append(stepsImg);        //src를 추가
                                context.setImage(builder_stepsImage.toString());
                            }
                            if (!context.getRecipe().equals("")) //객체에 요리법이 비어있지 않다면
                            {
                                arraylist.add(context);  //디테일한 정보를 담는 객체를 리스트에 추가
                            }

                            builder_steps.setLength(0);
                            builder_stepsImage.setLength(0);
                        }

                        for (Context k : arraylist) { //
                            String j = Integer.toString(t);
                            databaseReference.child(j).setValue(k);
                            t++;
                        }
                    } catch (IOException e) {
                        builder_title.append("Error");
                    }
                    l++;
                    t=0;
                    builder_title.setLength(0);
                    builder_mainImage.setLength(0);
                    builder_ingre.setLength(0);
                }

            }
        }).start();
    }
}

