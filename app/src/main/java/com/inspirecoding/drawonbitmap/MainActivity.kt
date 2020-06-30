package com.inspirecoding.drawonbitmap

import java.io.FileNotFoundException
import android.net.Uri
import android.os.Bundle
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.Style
import android.graphics.Rect
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import android.app.Activity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity()
{
    val RQS_IMAGE1 = 1
    var source1: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadimage1.setOnClickListener {
            var intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, RQS_IMAGE1)
        }
        processing.setOnClickListener {
            source1?.let {
                val processedBitmap = ProcessingBitmap()
                if (processedBitmap != null)
                {
                    result.setImageBitmap(processedBitmap)
                    Toast.makeText(
                        applicationContext,
                        "Done",
                        Toast.LENGTH_LONG
                    ).show()
                }
                else
                {
                    Toast.makeText(
                        applicationContext,
                        "Something wrong in processing!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun ProcessingBitmap(): Bitmap?
    {
        var bitmap: Bitmap? = null

        var newBitmap: Bitmap? = null

        try
        {
            bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(source1!!))

            var config: Bitmap.Config? = bitmap!!.config
            if (config == null) {
                config = Bitmap.Config.ARGB_8888
            }

            newBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, config!!)
            val newCanvas = Canvas(newBitmap)

            newCanvas.drawBitmap(bitmap, 0f, 0f, null)

            val captionString = caption.getText().toString()
            if (captionString != null) {

                val paintText = Paint(Paint.ANTI_ALIAS_FLAG)
                paintText.setColor(ContextCompat.getColor(this, R.color.light_blue))
                paintText.setTextSize(200f)
                paintText.setStyle(Style.FILL)
                paintText.setShadowLayer(10f, 10f, 10f, Color.BLACK)

                val rectText = Rect()
                paintText.getTextBounds(captionString, 0, captionString!!.length, rectText)

                newCanvas.drawText(
                    captionString,
                    0f, rectText.height().toFloat(), paintText
                )

                Toast.makeText(
                    applicationContext,
                    "drawText: " + captionString!!,
                    Toast.LENGTH_LONG
                ).show()

            } else {
                Toast.makeText(
                    applicationContext,
                    "caption empty!",
                    Toast.LENGTH_LONG
                ).show()
            }

        } catch (e: FileNotFoundException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }

        return newBitmap
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK)
        {
            when (requestCode)
            {
                RQS_IMAGE1 -> {
                    data?.getData()?.let {
                        result.setImageURI(it)
                        source1 = it
                    }
                }
            }
        }
    }
}
