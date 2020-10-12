package com.example.rentalapp.ui.load

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.text.Layout
import android.view.LayoutInflater
import com.example.rentalapp.R

class LoadingDialog (myActivity: Activity){
    private var activity: Activity = myActivity
    private var dialog: Dialog? = null

    fun startLoadingDialog () {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        val inflater: LayoutInflater = activity.layoutInflater
        builder.setView((inflater.inflate(R.layout.loading_page, null)))
        builder.setCancelable(false)

        dialog = builder.create()
        dialog?.show()
    }

    fun dismissDialog () {
        dialog?.dismiss()
    }



}