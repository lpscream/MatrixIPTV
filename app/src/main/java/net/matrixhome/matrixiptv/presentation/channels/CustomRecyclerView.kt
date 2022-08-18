package net.matrixhome.matrixiptv.presentation.channels

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView

class CustomRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): RecyclerView(context, attrs, defStyleAttr) {

    override fun fling(velocityX: Int, velocityY: Int): Boolean {
//        var velY = velocityY
//        velY *= 0.999.toInt()
        return super.fling(velocityX, velocityY)
    }
}