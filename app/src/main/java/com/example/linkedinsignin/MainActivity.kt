package com.example.linkedinsignin

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.linkedin.platform.APIHelper
import com.linkedin.platform.LISessionManager
import com.linkedin.platform.errors.LIApiError
import com.linkedin.platform.listeners.ApiListener
import com.linkedin.platform.listeners.ApiResponse
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*

import org.json.JSONException
import org.json.JSONObject

// https://developer.linkedin.com/docs/android-sdk
// https://developer.linkedin.com/docs/android-sdk-auth
// https://developer.linkedin.com/downloads#androidsdk
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!LISessionManager.getInstance(applicationContext).session.isValid) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        setContentView(R.layout.activity_main)
        loadLinkedInProfileData()
    }


    private fun loadLinkedInProfileData() {
        val url = "https://api.linkedin.com/v1/people/~:(id,first-name,last-name,headline,formatted-name,email-address,picture-url)"

        val apiHelper = APIHelper.getInstance(applicationContext)
        apiHelper.getRequest(this, url, object : ApiListener {
            override fun onApiSuccess(apiResponse: ApiResponse) {
                val jsonObject = apiResponse.responseDataAsJson
                displayProfileData(jsonObject)
            }

            override fun onApiError(liApiError: LIApiError) {
                liApiError.printStackTrace()
                Toast.makeText(applicationContext, "Error: " + liApiError.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayProfileData(jsonObject: JSONObject?) {
        try {
            tv_name.text = jsonObject!!.get("formattedName").toString()
            tv_headline.text = jsonObject.getString("headline")
            Picasso.with(applicationContext).load(jsonObject.getString("pictureUrl")).into(iv_profile_image)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }
}


