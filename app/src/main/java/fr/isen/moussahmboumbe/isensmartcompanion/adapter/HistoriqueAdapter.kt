package fr.isen.moussahmboumbe.isensmartcompanion.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.isen.moussahmboumbe.isensmartcompanion.R
import fr.isen.moussahmboumbe.isensmartcompanion.model.HistoriqueItem

class HistoriqueAdapter(
    private val historiqueList: MutableList<HistoriqueItem>,
    private val onDeleteClick: (HistoriqueItem) -> Unit
) : RecyclerView.Adapter<HistoriqueAdapter.HistoriqueViewHolder>() {

    class HistoriqueViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val questionText: TextView = view.findViewById(R.id.questionText)
        val reponseText: TextView = view.findViewById(R.id.reponseText)
        val deleteIcon: ImageView = view.findViewById(R.id.deleteIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoriqueViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.historique, parent, false)
        return HistoriqueViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoriqueViewHolder, position: Int) {
        val historique = historiqueList[position]
        holder.questionText.text = historique.question
        holder.reponseText.text = historique.reponse

        // ✅ Supprimer un historique individuel lorsqu'on clique sur l'icône
        holder.deleteIcon.setOnClickListener {
            onDeleteClick(historique)
        }
    }

    override fun getItemCount(): Int = historiqueList.size
}
