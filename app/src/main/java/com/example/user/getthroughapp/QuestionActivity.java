package com.example.user.getthroughapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

public class QuestionActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private QuestionManager questionManager;
    private int[] layouts;
    private TextView[] dots;
    private LinearLayout dotsLayout;
    private Button btnNext;
    private RadioButton rbtnFirst,rbtnSecond,rbtnThird,rbtnFourth;
    private int btnCheckCtr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        questionManager = new QuestionManager(this);
        getSupportActionBar().hide();
        if(!questionManager.checked()){
            questionManager.setFirst(false);
            startActivity(new Intent(QuestionActivity.this,HomeActivity.class));
            finish();
        }

        if(Build.VERSION.SDK_INT>21){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        }

        setContentView(R.layout.activity_question);

        viewPager = findViewById(R.id.viewpager);
        dotsLayout=findViewById(R.id.layoutDots);
        btnNext=findViewById(R.id.btnNext);


        layouts = new int[]{R.layout.activity_question_1,R.layout.activity_question_2,R.layout.activity_question_3,
                R.layout.activity_question_4,R.layout.activity_question_5,R.layout.activity_question_6,R.layout.activity_question_7
                ,R.layout.activity_question_8,R.layout.activity_question_9,R.layout.activity_question_10};


        changeStatusBarColor();
        viewPagerAdapter = new ViewPagerAdapter();
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(viewListener);

        rbtnFirst = findViewById(R.id.rbtnFirst);
        rbtnSecond = findViewById(R.id.rbtnSecond);
        rbtnThird = findViewById(R.id.rbtnThird);
        rbtnFourth = findViewById(R.id.rbtnFourh);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = getItem(+1);
                if (current<layouts.length){
                    viewPager.setCurrentItem(current);
                }else{
                    startActivity(new Intent(QuestionActivity.this,HomeActivity.class));
                    finish();
                }
            }
        });
    }

    public void answerClick(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.rbtnFirst:
                if (checked)
                    btnCheckCtr++;
                break;
            case R.id.rbtnSecond:
                if (checked)
                    btnCheckCtr++;
                break;
            case R.id.rbtnThird:
                if (checked)
                    btnCheckCtr++;
                break;
            case R.id.rbtnFourh:
                if (checked)
                    btnCheckCtr++;
                break;
        }
    }

    private void addBottomDots(int position){
        dots = new TextView[layouts.length];
        int[] colorActive = getResources().getIntArray(R.array.dotActive);
        int[] colorInactive = getResources().getIntArray(R.array.dotInactive);
        dotsLayout.removeAllViews();
        for(int i=0;i<dots.length;i++){
            dots[i]=new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorInactive[position]);
            dotsLayout.addView(dots[i]);
        }
        if(dots.length>0){
            dots[position].setTextColor(colorActive[position]);
        }
     }

     private int getItem(int i){
        return viewPager.getCurrentItem() + 1;
     }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
            if (position==layouts.length-1 ){
                btnNext.setText("FINISH");
            }
            else{
                btnNext.setText("NEXT");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void changeStatusBarColor(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);

        }
    }

    public class ViewPagerAdapter extends PagerAdapter{

        private LayoutInflater layoutInflater;

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = layoutInflater.inflate(layouts[position],container,false);
            container.addView(v);
            return v;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view==object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            View v = (View)object;
            container.removeView(v);
        }
    }
}
