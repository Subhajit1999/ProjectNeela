package com.subhajitkar.commercial.project_neela.adapters;

import android.app.Activity;
import android.app.usage.StorageStats;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.subhajitkar.commercial.project_neela.R;
import com.subhajitkar.commercial.project_neela.activities.SetupActivity;
import com.subhajitkar.commercial.project_neela.objects.User;
import com.subhajitkar.commercial.project_neela.utils.StaticFields;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SliderAdapter extends PagerAdapter implements AdapterView.OnItemSelectedListener{
    private static final String TAG = "SliderAdapter";

    private Context context;
    private Activity mActivity;
    private LayoutInflater layoutInflater;
    private TextView title, subTitle;
    private ChipGroup chipGroup;
    private List<String> genders;
    private List<String> selectedQuotesChips, selectedNewsChips;
    private int[] layouts = {R.layout.walkthrough_slide1, R.layout.walkthrough_slide2,
            R.layout.walkthrough_slide2, R.layout.walkthrough_slide4};

    public SliderAdapter(Context context, Activity activity){
        this.context = context;
        mActivity = activity;
    }

    @Override
    public int getCount() {
         return layouts.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        Log.d(TAG, "isViewFromObject: "+ (view == (LinearLayout) object));
        return view == (LinearLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Log.d(TAG, "instantiateItem: layout instantiating of position: "+position);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(layouts[position], container, false);

        switch(position){  //methods for different slides
            case 0:
                handle_slide1(view);
                break;
            case 1:
                handle_slide2(view);
                break;
            case 2:
                handle_slide3(view);
                break;
            case 3:
                handle_slide4(view);
                break;
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        Log.d(TAG, "destroyItem: destroying item position: "+position);
        container.removeView((LinearLayout) object);
    }

    private void handle_slide1(View v){
        Log.d(TAG, "handle_slide1: Handling slider 1 fields");
        //widgets init
        User user = StaticFields.userCredentials;
        TextView title = v.findViewById(R.id.tv_slide1_title);
        TextView subTitle = v.findViewById(R.id.tv_slide1_subtitle);
        TextView toc = v.findViewById(R.id.tv_text_toc);
        EditText nameInput = v.findViewById(R.id.et_slide1_name);
        EditText ageInput = v.findViewById(R.id.et_slide1_age);
        Spinner gender = v.findViewById(R.id.slide1_spinner);
        toc.setText(R.string.slide1_agreement_text);
        //spinner
        genders = Arrays.asList(context.getResources().getStringArray(R.array.genders));
        ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, genders);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        gender.setAdapter(adapter);
        gender.setOnItemSelectedListener(this);

        //set if any previous data exists
        if (!user.getUserGender().isEmpty()){
            gender.setSelection(genders.indexOf(user.getUserGender()));
        }else {
            gender.setSelection(0);
        }
        if (!user.getUserName().isEmpty()){
            nameInput.setText(user.getUserName());
        }
        if (user.getUserAge()>0){
            ageInput.setText(String.valueOf(user.getUserAge()));
        }

        //editTexts callbacks
        nameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                StaticFields.userCredentials.setUserName(editable.toString());
            }
        });

        ageInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().isEmpty() && TextUtils.isDigitsOnly(editable.toString())){
                StaticFields.userCredentials.setUserAge(Integer.parseInt(editable.toString()));
                }else{
                    Toast.makeText(context,"I think ages are only digits. So please make it correct.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void handle_slide2(View v){
        Log.d(TAG, "handle_slide2: Handling slider 2 fields");
        initCommonViews(v);
        selectedQuotesChips = new ArrayList<>();
        title.setText(R.string.slide2_cat_quotes);

        //chipGroup setup
        chipGroup.setSingleSelection(false);
        User user = StaticFields.userCredentials;
        final String[] arr = context.getResources().getStringArray(R.array.categories_quotes);

        if (user.getQuotesPref().size()>0){   //check if already values are there
            selectedQuotesChips = user.getQuotesPref();
        }
        for (String item: arr){ //adding chips to the group
            chipGroup.addView(StaticFields.addChip(context, item));
        }
        for (int i=0; i<chipGroup.getChildCount();i++){  //adding checkedListener to each group and checking them if already exists
            Chip chip = (Chip) chipGroup.getChildAt(i);
            final Integer I = i;
            if (selectedQuotesChips.contains(chip.getText())){
                Log.d(TAG, "handle_slide2: chip text:"+chip.getText());
                chip.setChecked(true);
            }
            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    Log.d(TAG, "onCheckedChanged: gets called. boolean: "+b);
                    if (b){
                        selectedQuotesChips.add(arr[I]);
                    }else{
                        selectedQuotesChips.remove(arr[I]);
                    }
                }
            });
        }
    }

    private void handle_slide3(View v){
        initCommonViews(v);
        selectedNewsChips = new ArrayList<>();

        title.setText(R.string.slide3_cat_news);
        //chipGroup
        chipGroup.setSingleSelection(false);
        final String[] arr = context.getResources().getStringArray(R.array.categories_news);
        User user = StaticFields.userCredentials;

        if (user.getNewsPref().size()>0){   //check if already values are there
            selectedNewsChips = user.getNewsPref();
        }
        for (String item: arr){  //adding chips to the group
            chipGroup.addView(StaticFields.addChip(context, item));
        }
        for (int i=0; i<chipGroup.getChildCount();i++){  //adding listeners to the chips
            Chip chip = (Chip) chipGroup.getChildAt(i);
            final Integer I = i;
            if (selectedNewsChips.contains(chip.getText())){
                Log.d(TAG, "handle_slide2: chip text:"+chip.getText());
                chip.setChecked(true);
            }
            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    Log.d(TAG, "onCheckedChanged: gets called. boolean: "+b);
                    if (b){
                        selectedNewsChips.add(arr[I]);
                    }else{
                        selectedNewsChips.remove(arr[I]);
                    }
                }
            });
        }
    }

    private void handle_slide4(View v){
        Log.d(TAG, "handle_slide4: handling slide 4");
        title = v.findViewById(R.id.tv_slide4_title);
        subTitle = v.findViewById(R.id.tv_slide4_subtitle);
    }

    private void initCommonViews(View view){
        Log.d(TAG, "initCommonViews: initializing common views for slide 2 and 3");
        title = view.findViewById(R.id.tv_slide2_title);
        subTitle = view.findViewById(R.id.tv_slide2_subtitle);
        chipGroup = view.findViewById(R.id.slide2_chipgroup);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Log.d(TAG, "onItemSelected: item listener callback for the spinner");
        //spinner item selectListener
        TextView tv = (TextView) view;
        if (i==0){
            tv.setTextColor(Color.GRAY);
            StaticFields.userCredentials.setUserGender("");
        }else{
            tv.setTextColor(Color.BLACK);
            StaticFields.userCredentials.setUserGender(genders.get(i));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public int getCheckedChips(int identifier) {
        Log.d(TAG, "getCheckedChips: saving and getting the number of checked chips");
        if (identifier == 0) {
            StaticFields.userCredentials.setQuotesPref(selectedQuotesChips);
            return StaticFields.userCredentials.getQuotesPref().size();
        } else {
            StaticFields.userCredentials.setNewsPref(selectedNewsChips);
            return StaticFields.userCredentials.getNewsPref().size();
        }
    }
}
