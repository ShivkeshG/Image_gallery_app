package com.example.galleryapp

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.galleryapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var allImages: ArrayList<Image>

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

       if (ContextCompat.checkSelfPermission(
               this, Manifest.permission.READ_EXTERNAL_STORAGE
           )!= PackageManager.PERMISSION_GRANTED) {
           ActivityCompat.requestPermissions(
               this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 101
           )
       }

        allImages = arrayListOf()

        if (allImages.isEmpty()) {
            allImages = getAllImages()

            binding.rvImage.adapter = ImageAdapter(this, allImages)
            binding.rvImage.layoutManager = GridLayoutManager(this, 3)
            binding.rvImage.setHasFixedSize(true)
        }



    }

    private fun getAllImages(): ArrayList<Image> {
        val images = ArrayList<Image>()

        val allImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.ImageColumns.DATA,
            MediaStore.Images.Media.DISPLAY_NAME)

        val cursor = this@MainActivity.contentResolver.query(
            allImageUri,
            projection,
            null, null, null)

        try {
            cursor!!.moveToNext()
            do {
                val image = Image()
                image.imagePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA))
                image.imageName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
                images.add(image)
            }while (cursor.moveToNext())
            cursor.close()

        }catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

        return images
    }


}