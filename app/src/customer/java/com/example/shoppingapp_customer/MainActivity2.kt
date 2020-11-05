package com.example.shoppingapp_customer

import android.os.Bundle
import android.transition.Transition
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.shoppingapp.R
import com.example.shoppingapp.util.OpenFragment
import com.example.shoppingapp_customer.fragments.CheckoutScreen
import com.example.shoppingapp_customer.fragments.DescriptionScreen
import com.example.shoppingapp_customer.fragments.ItemsScreen
import com.example.shoppingapp_customer.util.Navigation
import com.google.android.material.transition.platform.MaterialFade
import timber.log.Timber

class MainActivity2 : AppCompatActivity(), Navigation, OpenFragment {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val transition: Transition = MaterialFade()
        window.exitTransition = transition
        setContentView(R.layout.activity_main2)
        val fragName = intent.getStringExtra(getString(R.string.FRAGMENT_NAME_TAG))
        Timber.d(fragName)
        when {
            DescriptionScreen::class.java.simpleName == fragName -> {
                val fragment: Fragment = DescriptionScreen()
                val bundle = Bundle()
                val color = intent.getIntExtra(getString(R.string.COLOR_TAG), getColor(R.color.app_icon_color))
                bundle.putInt(getString(
                        R.string.COLOR_TAG), color)
                //            bundle.putParcelable(getString(R.string.OBJECT_TAG), getIntent().getParcelableExtra(getString(R.string.OBJECT_TAG)));
                Timber.d(color.toString())
                fragment.arguments = bundle
                openFragment(this, fragment, R.id.activity2_frame)
            }
            CheckoutScreen::class.java.simpleName == fragName -> {
                val fragment: Fragment = CheckoutScreen()
                //            Bundle bundle=new Bundle();
//            int color=getIntent().getIntExtra(getString(R.string.COLOR_TAG),getColor(R.color.light_blue));
//            bundle.putInt(getString(
//                    R.string.COLOR_TAG),color);
//            bundle.putParcelable(getString(R.string.OBJECT_TAG), getIntent().getParcelableExtra(getString(R.string.OBJECT_TAG)));
                Timber.d(fragName.toString())
                openFragment(this, fragment, R.id.activity2_frame)
            }
            else -> {
                openFragment(this, ItemsScreen(), R.id.activity2_frame)
            }
        }
    }

    override fun goBack() {
        super.supportFinishAfterTransition()
        super.onBackPressed()
    }
}