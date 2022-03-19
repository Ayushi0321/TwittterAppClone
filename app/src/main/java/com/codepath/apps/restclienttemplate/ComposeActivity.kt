package com.codepath.apps.restclienttemplate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class ComposeActivity : AppCompatActivity() {

    lateinit var etCompose: EditText
    lateinit var btnTweet: Button
    lateinit var client: TwitterClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)

        etCompose = findViewById(R.id.etTweet)
        btnTweet = findViewById(R.id.btnTweet)

        client = TwitterApplication.getRestClient(this)

        btnTweet.setOnClickListener {
            val tweetcontent = etCompose.text.toString()
            if (tweetcontent.isEmpty()) {
                Toast.makeText(this, "Empty Tweets not allowed!!", Toast.LENGTH_SHORT).show()
            } else if (tweetcontent.length > 280) {
                    Toast.makeText(this, "Tweet too long!! Limit 280 characters", Toast.LENGTH_SHORT).show()
            } else {
                client.publishTweet(tweetcontent, object: JsonHttpResponseHandler() {
                    override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                        val tweet = Tweet.fromJson(json.jsonObject)
                        val intent = Intent()
                        intent.putExtra("tweet", tweet)
                        setResult(RESULT_OK, intent)
                        finish()
                    }
                    override fun onFailure(
                        statusCode: Int, headers: Headers?, response: String?, throwable: Throwable?
                    ) {
                        Log.e("ComposeActivity", "Failed to post tweet", throwable)
                    }
                })
            }

        }
    }
}