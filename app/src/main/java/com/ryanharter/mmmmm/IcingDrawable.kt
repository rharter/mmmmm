package com.ryanharter.mmmmm

import android.graphics.*
import android.graphics.drawable.Drawable

class IcingDrawable(color: Int = 0xFF53250F.toInt()) : Drawable() {

  private val path = Path()
  private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    style = Paint.Style.FILL
    pathEffect = ComposePathEffect(CornerPathEffect(40f), DiscretePathEffect(60f, 25f))
  }

  var color: Int
    get() = paint.color
    set(value) {
      paint.color = value
    }

  init {
    this.color = color
  }

  override fun onBoundsChange(bounds: Rect?) {
    super.onBoundsChange(bounds)

    // resize the path
    path.reset()
    bounds?.let { bounds ->
      path.addCircle(bounds.exactCenterX(), bounds.exactCenterY(), bounds.width() / 2f - 25f / 2, Path.Direction.CW)
    }
  }

  override fun draw(canvas: Canvas?) {
    canvas?.let { canvas ->
      val scaleX = bounds.width().toFloat() / canvas.width
      val scaleY = bounds.height().toFloat() / canvas.height

      canvas.save()
      canvas.translate((canvas.width - bounds.width()) / 2F, (canvas.height - bounds.height()) / 2F)
      canvas.scale(scaleX, scaleY)
      canvas.drawPath(path, paint)
      canvas.restore()
    }
  }

  override fun setAlpha(alpha: Int) {
    TODO("not implemented")
  }

  override fun getOpacity(): Int = PixelFormat.OPAQUE

  override fun setColorFilter(colorFilter: ColorFilter?) {
    TODO("not implemented")
  }
}