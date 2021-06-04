package com.example.intro.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.intro.R;
import com.example.intro.firebase.RecipeList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// 검색 기능
public class FragmentPage1 extends Fragment {
    private List<String> list;

    //태그내용 저장하는 리스트 생성. 크기는 5
    ArrayList<String> taglist = new ArrayList<String>(5);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //setHasOptionsMenu(true);

        //프래그먼트에서 findBiewById 사용하려면 먼저 선언해 줘야함
        View v = inflater.inflate(R.layout.fragment_page_1, container, false);

        list = new ArrayList<String>(); //자동완성 검색 내용을 담을 리스트 생성
        settingList();

        final AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView)v.findViewById((R.id.autoCompleteTextView33));
        //자동완성 검색창 어댑터
        autoCompleteTextView.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, list));

        //검색창 엔터키 이벤트
        autoCompleteTextView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keycode, KeyEvent keyEvent) {
                //키보드 상에서 엔터키 입력받음
                if(keyEvent.getAction() == KeyEvent.ACTION_DOWN && keycode == KeyEvent.KEYCODE_ENTER){
                    //엔터키 이벤트 동작확인 토스트 메시지
                    // Toast.makeText(getActivity().getApplicationContext(), "태그 생성", Toast.LENGTH_SHORT).show();

                    //버튼추가 함수에 넘겨줄 문자열. 검색창 내용을 복사
                    String a1 = autoCompleteTextView.getText().toString();

                    addButton(a1);  //버튼추가 함수. 문자열 a1을 인자로 넘겨줌
                    autoCompleteTextView.setText("");
                    return true;    //엔터키로 줄바꿈 방지

                }
                return false;
            }
        });
        return v;   //반드시 v를 리턴
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    //버튼추가 함수
    public void addButton(String a1){
        //태그내용 추가
        taglist.add(a1);

        //버튼 동적 생성
        GridLayout manager = (GridLayout)getView().findViewById(R.id.glayout33);
        Button btn = new Button(getContext());
        btn.setText("#" + a1);
        //생성되는 버튼의 크기 설정
        btn.setLayoutParams(new LinearLayout.LayoutParams(200,100));
        //버튼 안의 텍스트 크기 설정
        btn.setTextSize(10);
        manager.addView(btn);

        //버튼클릭 이벤트. 삭제용
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //버튼 삭제. 안보이고 공간을 차지하지 않음
                btn.setVisibility(View.GONE);
                btn.setEnabled(false);

                //태그내용 삭제
                taglist.remove(a1);
            }
        });

        //태그완성 버튼 클릭 이벤트
        Button tbutton = (Button)getView().findViewById(R.id.tbutton);

        tbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //배열 출력
                String list = Arrays.toString(taglist.toArray()).replace("[", "").replace("]", "")
                        .replace(",", "").replace("\n", "");
                System.out.println(list);
                Intent intent = new Intent(getActivity(), RecipeList.class);
                intent.putExtra("taglist", list);
                startActivity(intent);
                //인텐트는 에러발생

                //Fragment fragment = new Fragment();
                //Bundle bundle = new Bundle();
                //bundle.putStringArrayList("taglist", taglist);
                //fragment.setArguments(bundle);
                //배열 데이터 전달. 확인필요
            }
        });

    }

    //자동완성으로 보이게 할 단어들 목록
    private void settingList() {
        list.add("소고기");
        list.add("닭고기");
        list.add("돼지고기");
        list.add("양파");
        list.add("마늘");
        list.add("버섯");
        list.add("피망");
        list.add("파프리카");
        list.add("새우");
        list.add("감자");
        list.add("고구마");
        list.add("간장");
        list.add("된장");
        list.add("고추장");
        list.add("두부");
        list.add("애호박");
        list.add("콩나물");
        list.add("무");
        list.add("계란");
        list.add("생강");
        list.add("밀가루");
        list.add("당근");
        list.add("고사리");
        list.add("콩");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");

    }
}