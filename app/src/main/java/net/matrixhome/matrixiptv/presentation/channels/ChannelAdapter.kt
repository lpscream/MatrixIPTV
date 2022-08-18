package net.matrixhome.matrixiptv.presentation.channels

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import net.matrixhome.matrixiptv.R
import net.matrixhome.matrixiptv.data.LOGO_URL

private val TAG = "ChannelAdapter_log"

class ChannelAdapter(private val retry:Retry, private val clickListener: ClickListener):
    RecyclerView.Adapter<ChannelAdapter.ChannelItemViewHolder>() {

    private val channels = ArrayList<ChannelUI>()


    fun update(new :List<ChannelUI>){
        channels.clear()
        channels.addAll(new)
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        if (channels.isEmpty()){
            ChannelItemViewHolder.Fail(R.layout.fail_fullscreen.makeView(parent), retry)
        }else{
            when(viewType){
                0 -> ChannelItemViewHolder.Base(R.layout.item_channel.makeView(parent), clickListener)
                1 -> ChannelItemViewHolder.Fail(R.layout.fail_fullscreen.makeView(parent), retry)
                else -> ChannelItemViewHolder.FullScreenProgress(R.layout.item_skeleton_channel.makeView(parent))
        }
    }

    override fun onBindViewHolder(holderItem: ChannelItemViewHolder, position: Int) =
        holderItem.bind(channels[position])

    override fun getItemViewType(position: Int) = when(channels[position]){
        is ChannelUI.Base -> 0
        is ChannelUI.Fail -> 1
        is ChannelUI.Progress -> 2
    }

    override fun getItemCount() = channels.size



    interface Retry{
        fun tryAgain()
    }
    interface ClickListener{
        fun onChannelClickListener(id: String, itemPosition: Int, number: Int)
        //fun onChannelLongClickListener()
        fun onFocusChannelListener(id: String)
    }


    abstract class ChannelItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
        open fun bind(channel: ChannelUI){}

        class FullScreenProgress(view: View): ChannelItemViewHolder(view)

//        class FullScreenProgress(view: View): ChannelItemViewHolder(view){
//            private val skeleton = itemView.findViewById<SkeletonPlaceholderView>(R.id.skeletonPlaceholderView)
//            private val shimmer = itemView.findViewById<ShimmerFrameLayout>(R.id.shimmer)
//            private val shimmerBuilder = Shimmer.AlphaHighlightBuilder()
//            override fun bind(channel: ChannelUI) {
//                skeleton.rootBackgroundColor = itemView.resources.getColor(R.color.transparent)
//                skeleton.skinView(R.layout.item_channel,
//                    RectBone.Builder(R.id.channel_icon)
//                        .cornerRadius(8f)
//                        .customHeight(68)
//                        .verticalSpacing(8)
//                        .build(),
//                    RectBone.Builder(R.id.channel_number)
//                        .cornerRadius(10f)
//                        .customWidth(100)
//                        .customHeight(40)
//                        .build(),
//                    RectBone.Builder(R.id.channel_name)
//                        .cornerRadius(10f)
//                        .customHeight(40)
//                        .build())
//                shimmer.setShimmer(shimmerBuilder
//                    .setDuration(2000L)
//                    .setShape(Shimmer.Shape.RADIAL)
//                    .build())
//            }
//        }

        class Base(view: View, private val clickListener: ClickListener): ChannelItemViewHolder(view){
            private val superLayout = itemView.findViewById<ConstraintLayout>(R.id.superLayout)
            private val channels_items_layout = itemView.findViewById<ConstraintLayout>(R.id.channels_items_layout)
            private val nameSign = itemView.findViewById<TextView>(R.id.channel_name)
            private val icon = itemView.findViewById<ImageView>(R.id.channel_icon)
            private val number_channel = itemView.findViewById<TextView>(R.id.channel_number)
            private val selector = itemView.findViewById<ConstraintLayout>(R.id.selector_channel)
            private val picasso = Picasso.get()
            override fun bind(channel: ChannelUI) {
                channel.map(object: ChannelUI.StringMapper{
                    override fun map(id: Int, name: String, url: String, number: Int) {
                        nameSign.text = name
                        picasso
                            .load("$LOGO_URL${id}.png")
                            .placeholder(android.R.drawable.ic_menu_report_image)
                            .error(android.R.drawable.ic_menu_report_image)
                            .networkPolicy(NetworkPolicy.NO_STORE)
                            .into(icon)
                        number_channel.text = number.toString()
                        superLayout.setOnClickListener{
                            clickListener.onChannelClickListener(id.toString(), absoluteAdapterPosition, number)
                        }
                        superLayout.setOnFocusChangeListener { view, b ->
                            clickListener.onFocusChannelListener(id.toString())
                            if (b){
                                selector.visibility = View.VISIBLE
                                channels_items_layout.animate().apply {
                                    translationX(200f)
                                    scaleX(1.3f)
                                    scaleY(1.3f)
                                    duration = 200
                                }.start()
                                channels_items_layout.elevation = 20f
                            }else{
                                selector.visibility = View.GONE
                                channels_items_layout.animate().apply {
                                    translationX(0f)
                                    scaleX(1.0f)
                                    scaleY(1.0f)
                                    duration = 200
                                }.start()
                                channels_items_layout.elevation = 0f
                            }
                        }
                    }
                })
            }
        }


        class Fail(view: View, private val retry: Retry): ChannelItemViewHolder(view){
            private val failMessage = itemView.findViewById<TextView>(R.id.fail_message)
            private val tryAgainButton = itemView.findViewById<Button>(R.id.try_again_button)
            override fun bind(channel: ChannelUI) {
                channel.map(object: ChannelUI.StringMapper{
                    override fun map(id: Int, text: String, url: String, number: Int) {
                        failMessage.text = text
                    }
                })
                tryAgainButton.setOnClickListener {
                    retry.tryAgain()
                }
            }
        }
    }
}

private fun Int.makeView(parent: ViewGroup) =
    LayoutInflater.from(parent.context).inflate(this, parent, false)