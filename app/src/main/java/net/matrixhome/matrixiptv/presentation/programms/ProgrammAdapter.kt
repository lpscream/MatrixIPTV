package net.matrixhome.matrixiptv.presentation.programms

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import net.matrixhome.matrixiptv.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ProgrammAdapter : RecyclerView.Adapter<ProgrammAdapter.ProgramItemViewHolder>() {

    private val programms = ArrayList<ProgrammUI>()


    fun update(new: List<ProgrammUI>) {
        programms.clear()
        programms.addAll(new)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int) = when (programms[position]) {
        is ProgrammUI.Base -> 0
        is ProgrammUI.Fail -> 1
        is ProgrammUI.Progress -> 2
    }

    override fun onBindViewHolder(holder: ProgramItemViewHolder, position: Int) =
        holder.bind(programms[position])

    override fun getItemCount() = programms.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        if (programms.isEmpty()) {
            ProgramItemViewHolder.Fail(R.layout.fail_fullscreen.makeView(parent))
        } else {
            when (viewType) {
                0 -> ProgramItemViewHolder.Base(R.layout.item_programm.makeView(parent))
                1 -> ProgramItemViewHolder.Fail(R.layout.fail_fullscreen.makeView(parent))
                else -> ProgramItemViewHolder.GuidesSkeleton(
                    R.layout.item_skeleton_program.makeView(
                        parent
                    )
                )
            }
        }

    abstract class ProgramItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        open fun bind(programm: ProgrammUI) {}

        fun convertLongToTime(time: Long): String {
            val newtime = time * 1000
            val date = Date(newtime)
            val format = SimpleDateFormat("HH:mm")
            return format.format(date)
        }


        class GuidesSkeleton(view: View) : ProgramItemViewHolder(view)



        class Base(view: View) : ProgramItemViewHolder(view) {
            private val start_time = itemView.findViewById<TextView>(R.id.start_time)
            private val stop_time = itemView.findViewById<TextView>(R.id.end_time)
            private val programm_title = itemView.findViewById<TextView>(R.id.current_programm)
            private val date = itemView.findViewById<TextView>(R.id.date)
            private val programm_layout = itemView.findViewById<ConstraintLayout>(R.id.programm_layout)
            private val date_layout = itemView.findViewById<ConstraintLayout>(R.id.date_layout)
            override fun bind(programm: ProgrammUI) {
                programm.map(object : ProgrammUI.StringMapper {
                    override fun map(id: String, title: String, start: Long, stop: Long) {
                        if (id == "date") {
                            start_time.text = ""
                            stop_time.text = ""
                            programm_title.text = ""
                            date.text  = title
                            programm_layout.visibility = View.GONE
                            date_layout.visibility = View.VISIBLE
                            date_layout.background = itemView.resources.getDrawable(R.drawable.rounded_corners_date)
                        } else {
                            start_time.text = convertLongToTime(start)
                            stop_time.text = convertLongToTime(stop)
                            programm_title.text = title
                            date.text  = ""
                            programm_layout.visibility = View.VISIBLE
                            date_layout.visibility = View.GONE
                            if (System.currentTimeMillis() >= start * 1000 && System.currentTimeMillis() <= stop * 1000){
                                programm_layout.background = itemView.resources.getDrawable(R.drawable.rounded_corners_curr_programm)
                            }else{
                                programm_layout.background = itemView.resources.getDrawable(R.color.transparent)
                            }
                        }
                    }
                })
            }
        }


        class Fail(view: View) : ProgramItemViewHolder(view) {
            private val failMessage = itemView.findViewById<TextView>(R.id.fail_message)
            private val tryAgainButton = itemView.findViewById<Button>(R.id.try_again_button)
            override fun bind(programm: ProgrammUI) {
                programm.map(object : ProgrammUI.StringMapper {
                    override fun map(id: String, title: String, start: Long, stop: Long) {
                        failMessage.text = title
                        tryAgainButton.visibility = View.GONE
                    }
                })
            }
        }
    }
}


private fun Int.makeView(parent: ViewGroup) =
    LayoutInflater.from(parent.context).inflate(this, parent, false)
