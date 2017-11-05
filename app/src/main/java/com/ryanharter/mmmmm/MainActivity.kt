package com.ryanharter.mmmmm

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator.INFINITE
import android.animation.ValueAnimator.RESTART
import android.os.Bundle
import android.support.annotation.ColorInt
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.Toast

class MainActivity : AppCompatActivity() {

  private data class Flavor(val name: String, @ColorInt val color: Int)
  private var currentBaseColor = 0
  private val baseColors = arrayOf(
      Flavor("Plain", 0xFFC6853b.toInt()),
      Flavor("Chocolate", 0xFF895E3E.toInt()),
      Flavor("Red Velvet", 0xFF8F0F1A.toInt())
  )
  private var currentIcingColor = 0
  private val icingColors = arrayOf(
      Flavor("Chocolate", 0xFF53250F.toInt()),
      Flavor("Vanilla", 0xFFEFEFEF.toInt()),
      Flavor("Strawberry", 0xFFDC5599.toInt())
  )

  private val content: View by lazy { findViewById<View>(android.R.id.content) }
  private val toolbar: Toolbar by lazy { findViewById<Toolbar>(R.id.toolbar) }
  private val platter: ImageView by lazy { findViewById<ImageView>(R.id.platter) }
  private val scale: SeekBar by lazy { findViewById<SeekBar>(R.id.scale) }
  private val baseColor: Button by lazy { findViewById<Button>(R.id.base_color) }
  private val icingColor: Button by lazy { findViewById<Button>(R.id.icing_color) }
  private val holeButton: Button by lazy { findViewById<Button>(R.id.hole) }
  private val animateButton: Button by lazy { findViewById<Button>(R.id.animate) }

  private val donut = DonutDrawable(80)
  private var animator: Animator? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    setSupportActionBar(toolbar)

    scale.progress = (scale.max * donut.scale).toInt()
    scale.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
      override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if (!fromUser) return
        donut.scale = progress.toFloat() / scale.max
      }
      override fun onStartTrackingTouch(seekBar: SeekBar?) {}
      override fun onStopTrackingTouch(seekBar: SeekBar?) {}
    })
    baseColor.setOnClickListener {
      currentBaseColor = (currentBaseColor + 1) % baseColors.size
      val flavor = baseColors[currentBaseColor]
      donut.baseColor = flavor.color
      Toast.makeText(this, flavor.name, Toast.LENGTH_SHORT).show()
    }
    icingColor.setOnClickListener {
      currentIcingColor = (currentIcingColor + 1) % icingColors.size
      val flavor = icingColors[currentIcingColor]
      donut.icingColor = flavor.color
      Toast.makeText(this, flavor.name, Toast.LENGTH_SHORT).show()
    }
    holeButton.setOnClickListener {
      val value = !donut.isHole
      donut.isHole = value
      holeButton.text = if (value) "Hole" else "Whole"
    }
    animateButton.setOnClickListener {
      val animator = animator
      if (animator != null) {
        animator.cancel()
        this.animator = null
      } else {
        val anim = ObjectAnimator.ofFloat(donut, "sprinkleRotation", 0f, 1f)
        anim.interpolator = LinearInterpolator()
        anim.duration = 3000
        anim.repeatMode = RESTART
        anim.repeatCount = INFINITE
        anim.start()
        this.animator = anim
      }
    }

    platter.setImageDrawable(donut)
  }
}
