package com.example.shoppingapp;

import android.os.Bundle;
import android.transition.Explode;

import android.transition.Slide;
import android.transition.Transition;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.shoppingapp.fragments.CheckoutScreen;
import com.example.shoppingapp.fragments.DescriptionScreen;
import com.example.shoppingapp.fragments.ItemsScreen;
import com.example.shoppingapp.util.Animation;
import com.example.shoppingapp.util.Navigation;
import com.example.shoppingapp.util.OpenFragment;
import com.google.android.material.transition.platform.MaterialElevationScale;
import com.google.android.material.transition.platform.MaterialFade;
import com.google.android.material.transition.platform.MaterialFadeThrough;

import timber.log.Timber;

public class MainActivity2 extends AppCompatActivity implements Navigation, OpenFragment {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Transition transition=new MaterialFade();
        getWindow().setExitTransition(transition);
        setContentView(R.layout.activity_main2);

        String fragName = getIntent().getStringExtra(getString(R.string.FRAGMENT_NAME_TAG));
        Timber.d(fragName);

        if (DescriptionScreen.class.getSimpleName().equals(fragName)) {
            Fragment fragment=new DescriptionScreen();
            Bundle bundle=new Bundle();
            int color=getIntent().getIntExtra(getString(R.string.COLOR_TAG),getColor(R.color.light_blue));
            bundle.putInt(getString(
                    R.string.COLOR_TAG),color);
//            bundle.putParcelable(getString(R.string.OBJECT_TAG), getIntent().getParcelableExtra(getString(R.string.OBJECT_TAG)));
            Timber.d(String.valueOf(color));
            fragment.setArguments(bundle);
            openFragment(fragment);
        }
        else if (CheckoutScreen.class.getSimpleName().equals(fragName)) {
            Fragment fragment=new CheckoutScreen();
//            Bundle bundle=new Bundle();
//            int color=getIntent().getIntExtra(getString(R.string.COLOR_TAG),getColor(R.color.light_blue));
//            bundle.putInt(getString(
//                    R.string.COLOR_TAG),color);
//            bundle.putParcelable(getString(R.string.OBJECT_TAG), getIntent().getParcelableExtra(getString(R.string.OBJECT_TAG)));
            Timber.d(String.valueOf(fragName));
            openFragment(fragment);
        }
        else {
            openFragment(new ItemsScreen());
        }


    }

    @Override
    public void goBack() {
        super.supportFinishAfterTransition();
        super.onBackPressed();
    }

    @Override
    public void openFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity2_frame, fragment)
//                .addToBackStack(getString(R.string.backstack_tag))
                .commit();
    }

}

