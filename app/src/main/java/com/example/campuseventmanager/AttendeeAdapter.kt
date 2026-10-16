package com.example.campuseventmanager

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import com.example.campuseventmanager.models.Attendee
import java.util.Locale

// Wrap the 'attendees' parameter with `.toMutableList()` to ensure the superclass gets a modifiable list.
class AttendeeAdapter(context: Context, attendees: List<Attendee>) : ArrayAdapter<Attendee>(context, 0, attendees.toMutableList()) {

    // kopya ng original, unfiltered list for filtering logic.
    private var originalAttendees: List<Attendee> = ArrayList(attendees)

    fun updateAttendees(newAttendees: List<Attendee>) {
        clear()
        originalAttendees = ArrayList(newAttendees)
        addAll(newAttendees)
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_attendee, parent, false)

        val attendee = getItem(position)

        val sectionTextView = view.findViewById<TextView>(R.id.attendeeSection)
        val studentNumberTextView = view.findViewById<TextView>(R.id.attendeeStudentNumber)
        val nameTextView = view.findViewById<TextView>(R.id.attendeeName)
        val statusTextView = view.findViewById<TextView>(R.id.attendeeStatus)

        // Set text, with null checks for safety
        sectionTextView.text = attendee?.section ?: "N/A"
        studentNumberTextView.text = attendee?.studentNumber ?: "N/A"
        nameTextView.text = attendee?.username ?: "No Name"
        statusTextView.text = attendee?.status ?: "No Status"

        return view
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                val query = constraint?.toString()?.trim()?.lowercase(Locale.getDefault())

                val filteredList = if (query.isNullOrEmpty()) {
                    originalAttendees
                } else {
                    // Otherwise, filter the original list
                    originalAttendees.filter { attendee ->
                        (attendee.username?.lowercase(Locale.getDefault())?.contains(query) == true) ||
                                (attendee.section?.lowercase(Locale.getDefault())?.contains(query) == true) ||
                                (attendee.studentNumber?.lowercase(Locale.getDefault())?.contains(query) == true)
                    }
                }

                results.values = filteredList
                results.count = filteredList.size
                return results
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                clear()
                // Add the new filtered results
                if (results?.values != null) {
                    val filteredData = results.values as List<Attendee>
                    addAll(filteredData)
                }
                notifyDataSetChanged()
            }
        }
    }
}
