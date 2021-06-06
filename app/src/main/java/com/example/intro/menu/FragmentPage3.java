package com.example.intro.menu;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.intro.firebase.BookMarkListAdapter;
import com.example.intro.R;

import com.example.intro.firebase.DetailList;
import com.example.intro.firebase.TotalRecipe;
import com.example.intro.firebase.BookMarkListAdapter;
import com.example.intro.firebase.TotalRecipe;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

// 즐겨찾기 기능
public class FragmentPage3 extends Fragment {
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<TotalRecipe> arrayList=new ArrayList<>();
    int i;
    Context ct;
    Button refreshbtn, resetbtn;
    public interface Callback{
        void success(TotalRecipe data);
        void fail(String errorMessage);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        database= FirebaseDatabase.getInstance();//파이어베이스 데이터베이스 연동
        ViewGroup v = (ViewGroup)inflater.inflate(R.layout.activity_recent,container,false);
        ct=container.getContext();
        recyclerView=(RecyclerView)v.findViewById(R.id.RecipeListView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        refreshbtn=(Button)v.findViewById(R.id.btnrefresh);
        resetbtn=(Button)v.findViewById(R.id.btnreset);

        final String[][] key = new String[1][100];
        database=FirebaseDatabase.getInstance();
        databaseReference=database.getReference("data");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot Snapshot) {
                arrayList.clear();
                i=0;

                for(DataSnapshot snapshot : Snapshot.getChildren())
                {
                    if(snapshot.child("Recent").getValue()!=null && snapshot.child("Recent").getValue().equals("1")) {
                        TotalRecipe totalRecipe=snapshot.getValue(TotalRecipe.class);
                        arrayList.add(totalRecipe);
                        key[0][i] =snapshot.getKey();
                        i++;

                    }

                }
                adapter = new BookMarkListAdapter(arrayList,getActivity());
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MainActivity", String.valueOf(error.toException()));
            }
        });
        refreshbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //배열 출력
                refresh();
            }
        });
        resetbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                for(int n=0; n<i;n++) {

                    databaseReference = database.getReference("data/" + key[0][n]);
                    databaseReference.child("Recent").setValue("0");
                }
                refresh();
            }
        });
        setHasOptionsMenu(true);
        return v;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    public void refresh() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.detach(this).attach(this).commit();
    }

}