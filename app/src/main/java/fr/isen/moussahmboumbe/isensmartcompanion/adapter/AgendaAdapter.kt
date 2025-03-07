package fr.isen.moussahmboumbe.isensmartcompanion.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.isen.moussahmboumbe.isensmartcompanion.R
import fr.isen.moussahmboumbe.isensmartcompanion.model.Event

class AgendaAdapter(
    private val events: List<Event>,
    private val onClick: (Event) -> Unit
) : RecyclerView.Adapter<AgendaAdapter.AgendaViewHolder>() {

    class AgendaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val category: TextView = view.findViewById(R.id.categorieAgenda)
        val title: TextView = view.findViewById(R.id.titreAgenda)
        val location: TextView = view.findViewById(R.id.lieuAgenda)
        val date: TextView = view.findViewById(R.id.dateAgenda)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgendaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.agenda, parent, false)
        return AgendaViewHolder(view)
    }

    override fun onBindViewHolder(holder: AgendaViewHolder, position: Int) {
        val event = events[position]
        holder.category.text = event.category
        holder.title.text = event.title
        holder.location.text = event.location
        holder.date.text = event.date

        // Afficher le popup lorsque l'utilisateur clique sur un événement
        holder.itemView.setOnClickListener {
            onClick(event)
        }
    }

    override fun getItemCount(): Int = events.size
}
