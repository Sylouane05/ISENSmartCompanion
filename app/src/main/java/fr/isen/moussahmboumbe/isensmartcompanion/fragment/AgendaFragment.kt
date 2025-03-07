package fr.isen.moussahmboumbe.isensmartcompanion.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import fr.isen.moussahmboumbe.isensmartcompanion.adapter.AgendaAdapter
import fr.isen.moussahmboumbe.isensmartcompanion.databinding.AgendaFragmentBinding
import fr.isen.moussahmboumbe.isensmartcompanion.model.Event
import fr.isen.moussahmboumbe.isensmartcompanion.R

class AgendaFragment : Fragment() {

    private var _binding: AgendaFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var agendaAdapter: AgendaAdapter
    private val eventList = mutableListOf<Event>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = AgendaFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialisation du RecyclerView
        agendaAdapter = AgendaAdapter(eventList) { event ->
            showPopup(view, event)
        }
        binding.recyclerViewAgenda.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewAgenda.adapter = agendaAdapter

        // Charger les événements depuis Firebase
        loadAgendaFromDatabase()
    }

    /**
     *  Charger les événements avec notification=true ou catégorie="cours"
     */
    private fun loadAgendaFromDatabase() {
        val database = FirebaseDatabase.getInstance("https://isensmartsompanion-default-rtdb.firebaseio.com/")
            .getReference("events")

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                eventList.clear()
                if (snapshot.exists()) {
                    for (eventSnapshot in snapshot.children) {
                        val event = eventSnapshot.getValue(Event::class.java)
                        if (event != null && (event.notification || event.category == "Cours")) {
                            eventList.add(event)
                        }
                    }
                    agendaAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Gérer l'erreur
            }
        })
    }

    /**
     *  Afficher un popup avec les détails de l'événement
     */
    @SuppressLint("InflateParams")
    private fun showPopup(view: View, event: Event) {
        val popupView = LayoutInflater.from(view.context).inflate(R.layout.popup_agenda, null)
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        popupView.findViewById<TextView>(R.id.categoryPops).text = event.category
        popupView.findViewById<TextView>(R.id.titlePops).text = event.title
        popupView.findViewById<TextView>(R.id.datePops).text = event.date
        popupView.findViewById<TextView>(R.id.locationPops).text = event.location
        popupView.findViewById<TextView>(R.id.descriptionPops).text = event.description

        // Fermer le popup en cliquant sur le bouton X
        popupView.findViewById<View>(R.id.closePops).setOnClickListener {
            popupWindow.dismiss()
        }

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
