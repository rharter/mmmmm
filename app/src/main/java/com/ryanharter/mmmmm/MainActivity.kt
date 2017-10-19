package com.ryanharter.mmmmm

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.ImageView

class MainActivity : AppCompatActivity() {

  private val toolbar: Toolbar by lazy { findViewById<Toolbar>(R.id.toolbar) }
  private val platter: ImageView by lazy { findViewById<ImageView>(R.id.platter) }

  private val donut = DonutDrawable()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    setSupportActionBar(toolbar)

    donut.icing = IcingDrawable()
    platter.setImageDrawable(donut)
  }

}
