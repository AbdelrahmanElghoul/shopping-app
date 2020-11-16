package com.example.shoppingapp_vendor.register

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.shoppingapp.R
import com.example.shoppingapp.util.OpenFragment
import com.example.shoppingapp.util.Permissions
import timber.log.Timber

class RegisterActivity : AppCompatActivity(),OpenFragment {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        openFragment(this, SignInFragment(),R.id.frame_register_ar)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    Permissions.REQUEST_STORAGE.get)
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if ((requestCode == Permissions.REQUEST_STORAGE.get)) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Timber.e("permission granted")
            }
            else {
                Toast.makeText(this,"app need that permission to save your data",Toast.LENGTH_LONG).show()
                finishAffinity()
            }
        }
    }
    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        if(count==1)
            finish()
        else
            super.onBackPressed()
    }

}