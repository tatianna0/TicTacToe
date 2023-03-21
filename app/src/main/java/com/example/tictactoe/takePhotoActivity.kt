package com.example.tictactoe

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast

class takePhotoActivity : AppCompatActivity() {


    private val TAKE_PHOTO_ID = 0
    lateinit var imageView1: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_take_photo)

        val button1 = findViewById<Button>(R.id.button1)
        imageView1 = findViewById<ImageView>(R.id.imageView1)

        button1.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(
                intent,
                TAKE_PHOTO_ID
            )
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TAKE_PHOTO_ID) {
            if (data != null && data.hasExtra("data")) {
                val bitmap = data.getParcelableExtra<Bitmap>("data")
                imageView1.setImageBitmap(bitmap)
                Toast.makeText(
                    this, getString(R.string.goTO),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    fun goToMainActivity(v: View) {


        finish()
    }



}