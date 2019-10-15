package com.ryanharter.mmmmm

import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.Keep
import java.util.*

class DonutDrawable(sprinkleCount: Int) : Drawable() {

  /*
   * Base
   */
  private val basePaint = Paint().apply {
    color = 0xFFc6853b.toInt()
  }
  var baseColor: Int
    get() = basePaint.color
    set(value) {
      basePaint.color = value
      invalidateSelf()
    }

  var scale = 0.8f
    set(value) {
      field = value
      invalidateSelf()
    }

  /**
   * Whether this is a whole donut, or a donut hole.
   */
  var isHole = false
    set(value) {
      field = value
      invalidateSelf()
    }
  private val holePath = Path()

  /*
   * Icing
   */
  private val icingPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    pathEffect = ComposePathEffect(CornerPathEffect(40f), DiscretePathEffect(60f, 25f))
    color = 0xFF53250F.toInt()
  }
  var icingColor: Int
    get() = icingPaint.color
    set(value) {
      icingPaint.color = value
      invalidateSelf()
    }

  /*
   * Sprinkles
   */
  private data class Sprinkle(val color: Int, val angle: Float, val distance: Float, var rotation: Float)
  private val sprinkleColors = intArrayOf(Color.RED, Color.WHITE, Color.YELLOW, Color.BLUE, Color.CYAN, Color.GREEN, Color.MAGENTA)
  private val sprinkles = generateSprinkles(sprinkleCount)
  private val sprinklePaint = Paint(Paint.ANTI_ALIAS_FLAG)
  @get:Keep @set:Keep var sprinkleRotation = 0f
    set(value) {
      field = value
      invalidateSelf()
    }

  private fun generateSprinkles(sprinkleCount: Int): List<Sprinkle> {
    val random = Random()
    return mutableListOf<Sprinkle>().also {
      for (i in 0 until sprinkleCount) {
        it += Sprinkle(
            color = sprinkleColors[i % sprinkleColors.size],
            angle = random.nextFloat() * 360f,
            distance = random.nextFloat(),
            rotation = random.nextFloat() * 360f
        )
      }
    }
  }

  override fun onBoundsChange(bounds: Rect?) {
    super.onBoundsChange(bounds)

    // Update the clip path to be a circle that is 1/3 the diameter of the donut
    bounds?.let {
      holePath.reset()
      holePath.addCircle(it.exactCenterX(), it.exactCenterY(), it.width() / 6F, Path.Direction.CW)
    }
  }

  override fun draw(canvas: Canvas) {
      val saveCount = canvas.save()
      canvas.scale(scale, scale,
          bounds.width() / 2f,
          bounds.height() / 2f)

      // clip the drawing to inside/outside the hole
      if (isHole) {
        // draw inside the hole
        canvas.clipPath(holePath)
      } else {
        // draw outside the hole
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
          canvas.clipOutPath(holePath)
        } else {
          @Suppress("DEPRECATION")
          canvas.clipPath(holePath, Region.Op.DIFFERENCE)
        }
      }

      // draw the base
      canvas.drawCircle(
          bounds.exactCenterX(), // cx
          bounds.exactCenterY(), // cy
          bounds.width() / 2f, // radius
          basePaint)

      // draw the icing
      canvas.drawCircle(
          bounds.exactCenterX(),
          bounds.exactCenterY(),
          bounds.width() / 2.1f,
          icingPaint)

      // draw the sprinkles
      val cx = bounds.exactCenterX()
      val cy = bounds.exactCenterY()
      sprinkles.forEach {
        val holeRadius = bounds.width() / 6f
        val padding = 20f

        // set the distance based on whether this is a donut hole or a whole donut
        val modDistance = if (isHole) {
          holeRadius * it.distance
        } else {
          val ringRadius = bounds.width() / 3f
          holeRadius + padding + (ringRadius - padding * 2) * it.distance
        }

        canvas.save()
        canvas.rotate(it.angle, cx, cy) // rotate the entire canvas around the center
        canvas.translate(0f, modDistance) // move the canvas to the sprinkle's position
        canvas.rotate(it.rotation + 360f * sprinkleRotation, cx, cy) // rotate the canvas around the sprinkle's location

        sprinklePaint.color = it.color
        canvas.drawRoundRect(cx - 7f, cy - 22f, cx + 7f, cy + 22f, 10f, 10f, sprinklePaint)

        canvas.restore()
      }

      canvas.restoreToCount(saveCount)
  }

  override fun setAlpha(alpha: Int) {
    throw IllegalStateException("Who wants an invisible donut?")
  }

  override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

  override fun setColorFilter(colorFilter: ColorFilter?) {
    TODO("not implemented")
  }
}
