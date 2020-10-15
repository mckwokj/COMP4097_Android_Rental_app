package com.example.rentalapp.ui.load

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import com.example.rentalapp.R
import com.example.rentalapp.ui.login.LoginFragment

class LoadingDialog(myActivity: Activity){
    private var activity: Activity = myActivity
    private var dialog: Dialog? = null

    fun startLoadingDialog () {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        Log.d("ckp1", "ckp1")
        val inflater: LayoutInflater = activity.layoutInflater
        Log.d("ckp2", "ckp2")
        builder.setView((inflater.inflate(R.layout.loading_page, null)))
        Log.d("ckp3", "ckp3")
        builder.setCancelable(false)
        Log.d("ckp4", "ckp4")
        dialog = builder.create()
        Log.d("ckp5", "ckp5")
        dialog?.show()
        Log.d("ckp6", "ckp6")
        Log.d("loading", "already show loading page")
    }

    fun dismissDialog () {
        dialog?.dismiss()
    }



}