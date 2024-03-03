package com.example.smart24x7;

import android.graphics.Color;
import android.os.Bundle;

import com.hololo.tutorial.library.Step;
import com.hololo.tutorial.library.TutorialActivity;

public class HowToSwipe extends TutorialActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addFragment(new Step.Builder()
        .setTitle("About Smart 24x7")
        .setContent("In order to ask for help, you need to add your Family and Friends Mobile Number.").setSummary("Step 1")
        .setBackgroundColor(Color.parseColor("#CC0059"))
        .setDrawable(R.drawable.s1).build());

        addFragment(new Step.Builder()
                .setTitle("How to use in trouble?")
                .setContent("Just Shake the Device untill a Trigger is generated to send the Location details via SMS.")
                .setSummary("Step 2")
                .setBackgroundColor(Color.parseColor("#CC0059"))
                .setDrawable(R.drawable.s2).build());

        addFragment(new Step.Builder()
                .setTitle(" What happens after Shaking ?")
                .setContent("SOS will be triggered resulting in sending a Message and Location to the registered Mobile Numbers.")
                .setSummary("Step 3")
                .setSummary("This is summary")
                .setBackgroundColor(Color.parseColor("#CC0059"))
                .setDrawable(R.drawable.s3).build());
    }

    @Override
    public void currentFragmentPosition(int position) {

    }
}
