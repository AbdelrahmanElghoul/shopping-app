package com.example.shoppingapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.transition.Fade;
import android.transition.Transition;
import android.view.View;

import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;

public abstract class Animation {

    public static void SharedElementTransition(View view1, String transitionName, Context context, Intent intent){

        ActivityOptionsCompat compat=ActivityOptionsCompat
                .makeSceneTransitionAnimation((Activity) context,
                        view1,transitionName);
        context.startActivity(intent,compat.toBundle());

    }

    @SafeVarargs
    public static void MultipleSharedElementTransition(Context context, Intent intent,
                                                       Pair<View, String>... sharedElements){
        ActivityOptionsCompat compat=ActivityOptionsCompat
                .makeSceneTransitionAnimation(
                        (Activity) context,
                        sharedElements);
        context.startActivity(intent,compat.toBundle());
    }
    public static void FadeTransition(Activity context,boolean enterTransition,boolean existTransition){

        Transition animation=new Fade();
        if(existTransition) context.getWindow().setExitTransition(animation);
        if(enterTransition) context.getWindow().setEnterTransition(animation);



    }

}
