package at.htl.breaknotifierandroid.model

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import at.htl.breaknotifierandroid.R
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.math.min

class TimetableAdapter(
    val context: Context,
    var items: ArrayList<Lesson>
) : BaseAdapter() {

    companion object {
        val TAG = TimetableAdapter::class.java.simpleName
    }

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private val selectedItems: BooleanArray = BooleanArray(items.size)
    private val checkBoxes: MutableList<CheckBox> = mutableListOf()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.list_item_with_checkbox, parent, false)
        val checkBox = rowView.findViewById<CheckBox>(R.id.cb_selected)
        val textView = rowView.findViewById<TextView>(R.id.tv_lessonInfo)
        val item = this.items[position]

        checkBox.setOnCheckedChangeListener { _: CompoundButton, b: Boolean ->
            this.selectedItems[position] = b
        }
        this.checkBoxes.add(checkBox)
        textView.text = item.toString()
        return rowView
    }

    override fun getItem(p0: Int): Any {
        return items[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return this.items.size
    }

    fun joinSelectedLessons() {
        var firstJoinedLesson: Lesson? = null

        for (i in 0 until this.selectedItems.size) {
            if(selectedItems[i]) {
                if(firstJoinedLesson == null) {
                    firstJoinedLesson = this.items[i]
                } else {
                    firstJoinedLesson.skipBreakUnits += if(items[i].startTime == "1000") 3 else 1
                    val formatter = DateTimeFormatter.ofPattern("HHmm")
                    var endTime = LocalTime.parse(this.items[i].endTime, formatter)
                    this.items[i].endTime = "0000"
                    firstJoinedLesson.endTime = endTime.plusMinutes((-5 * firstJoinedLesson.skipBreakUnits).toLong()).format(formatter)
                }
            } else if (firstJoinedLesson != null) {
                firstJoinedLesson = null
            }
        }
    }

    fun changeItems(items: ArrayList<Lesson>) {
        for(i in 0 until min(items.size, this.items.size)) {
            this.items[i].copy(items[i])
        }
    }

    fun resetSelection() {
        for(i in 0 until this.selectedItems.size) {
            this.selectedItems[i] = false
            this.checkBoxes[i].isChecked = false
        }
    }
}