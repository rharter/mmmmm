package com.ryanharter.mmmmm

import android.graphics.*
import android.graphics.drawable.Drawable

class DonutDrawable : Drawable() {

  var icing: IcingDrawable? = null
    set(value) {
      field = value
      value?.let {
        val newBounds = Rect(bounds)
        newBounds.inset(40, 40)
        it.bounds = newBounds
      }
      invalidateSelf()
    }

  private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    style = Paint.Style.FILL
    color = 0xFFc6853b.toInt()
  }
  private val clipPath = Path()

  override fun onBoundsChange(bounds: Rect?) {
    super.onBoundsChange(bounds)

    // Update the clip path to be a circle that is 1/3 the diameter of the donut
    bounds?.let {
      clipPath.addCircle(it.exactCenterX(), it.exactCenterY(), it.width() / 6F, Path.Direction.CW)
      icing?.let {
        val newBounds = Rect(bounds)
        newBounds.inset(10, 10)
        it.bounds = newBounds
      }
    }
  }

  override fun draw(canvas: Canvas?) {
    canvas?.let { canvas ->
      canvas.drawCircle(bounds.exactCenterX(), bounds.exactCenterY(), bounds.width() / 2f, paint)

      // draw the icing
      icing?.draw(canvas)

      // cut out the middle
      canvas.clipPath(clipPath)
      canvas.drawColor(Color.WHITE)
    }
  }

  override fun setAlpha(alpha: Int) {
    throw IllegalStateException("Who wants an invisible donut?")
  }

  override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

  override fun setColorFilter(colorFilter: ColorFilter?) {
    TODO("not implemented")
  }
}