package fr.isen.moussahmboumbe.isensmartcompanion.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import fr.isen.moussahmboumbe.isensmartcompanion.adapter.EventAdapter
import fr.isen.moussahmboumbe.isensmartcompanion.databinding.EventFragmentBinding
import fr.isen.moussahmboumbe.isensmartcompanion.model.Event

class EventsFragment : Fragment() {

    private var _binding: EventFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var eventAdapter: EventAdapter
    private val eventList = mutableListOf<Event>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = EventFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Charger les événements depuis Firebase
        loadEventsFromDatabase()

        // Configuration du RecyclerView
        eventAdapter = EventAdapter(eventList) { event -> /* Gérer le clic ici */ }
        binding.recyclerViewEvents.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewEvents.adapter = eventAdapter
    }

    /**
     *  Charger les événements depuis Firebase en excluant ceux avec category="cours"
     */
    private fun loadEventsFromDatabase() {
        val database = FirebaseDatabase.getInstance("https://isensmartsompanion-default-rtdb.firebaseio.com/")
            .getReference("events")

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                eventList.clear()
                if (snapshot.exists()) {
                    for (eventSnapshot in snapshot.children) {
                        val event = eventSnapshot.getValue(Event::class.java)
                        if (event != null && event.category != "cours") { //  Exclure "cours"
                            eventList.add(event)
                        }
                    }
                    eventAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Gérer l'erreur
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
