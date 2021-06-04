package com.example.intro.firebase;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.intro.R;
import com.example.intro.firebase.RecipeListAdapter;
import com.example.intro.firebase.TotalRecipe;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class RecipeList extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<TotalRecipe> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipelist);

        //상태바 제거
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //액션바 사라지게 하기
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Intent intent = getIntent();
        String searchtag = intent.getStringExtra("taglist");
        StringTokenizer st = new StringTokenizer(searchtag);
        ArrayList<String> ingred= new ArrayList<>();
        while(st.hasMoreTokens()==true)
        {
            ingred.add(st.nextToken());
        }


        database= FirebaseDatabase.getInstance();//파이어베이스 데이터베이스 연동
        recyclerView=findViewById(R.id.RecipeListView);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();

        database= FirebaseDatabase.getInstance();
        databaseReference=database.getReference("data");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot Snapshot) {
                arrayList.clear(); int i =0;
                for(DataSnapshot snapshot : Snapshot.getChildren())
                {
                    TotalRecipe totalRecipe = snapshot.getValue(TotalRecipe.class);

                    for(String k : ingred){

                        if (totalRecipe.getIngredient().contains(k) ){
                                i++;
                        }
                    }
                    if(i==ingred.size()) {
                        arrayList.add(totalRecipe);
                    }
                    i=0;
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MainActivity", String.valueOf(error.toException()));

            }
        });
        adapter = new RecipeListAdapter(arrayList,this);
        recyclerView.setAdapter(adapter);
    }
}