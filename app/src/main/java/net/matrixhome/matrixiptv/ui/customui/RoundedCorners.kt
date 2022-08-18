package net.matrixhome.matrixiptv.ui.customui

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import net.matrixhome.matrixiptv.R

class RoundedCorners @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
): ConstraintLayout(context, attrs, defStyleAttr) {

  private val maskPath: MaskPath

  init {

    val cornerRadius = context.resources.getDimension(R.dimen.corner_radius)
    maskPath = MaskPathRoundedCorners(cornerRadius, cornerRadius)
    maskPath.setRadius(cornerRadius, cornerRadius, cornerRadius, cornerRadius)
    maskPath.rebuildPath(measuredWidth, measuredHeight)

  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    maskPath.rebuildPath(measuredWidth, measuredHeight)
  }

  override fun dispatchDraw(canvas: Canvas) {
    canvas.clipPath(maskPath.getPath())

    super.dispatchDraw(canvas)

  }
}
