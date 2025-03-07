package fr.isen.moussahmboumbe.isensmartcompanion.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import fr.isen.moussahmboumbe.isensmartcompanion.adapter.HistoriqueAdapter
import fr.isen.moussahmboumbe.isensmartcompanion.database.HistoriqueDatabase
import fr.isen.moussahmboumbe.isensmartcompanion.databinding.HistoriqueFragmentBinding
import fr.isen.moussahmboumbe.isensmartcompanion.model.HistoriqueItem
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HistoriqueFragment : Fragment() {

    private var _binding: HistoriqueFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var historiqueAdapter: HistoriqueAdapter
    private var historiqueList = mutableListOf<HistoriqueItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = HistoriqueFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        historiqueAdapter = HistoriqueAdapter(historiqueList) { historique ->
            supprimerHistorique(historique)
        }

        binding.recyclerViewHistorique.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewHistorique.adapter = historiqueAdapter

        binding.clearAllIcon.setOnClickListener {
            supprimerToutHistorique()
        }

        loadHistorique()
    }

    private fun loadHistorique() {
        lifecycleScope.launch {
            val historiqueDao = HistoriqueDatabase.getDatabase(requireContext()).historiqueDao()
            historiqueDao.getAllHistorique().collect { list ->
                historiqueList.clear()
                historiqueList.addAll(list)
                historiqueAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun supprimerHistorique(historique: HistoriqueItem) {
        lifecycleScope.launch {
            val historiqueDao = HistoriqueDatabase.getDatabase(requireContext()).historiqueDao()
            historiqueDao.deleteHistorique(historique)
            loadHistorique() // 🔥 Rafraîchir après suppression
        }
    }

    private fun supprimerToutHistorique() {
        lifecycleScope.launch {
            val historiqueDao = HistoriqueDatabase.getDatabase(requireContext()).historiqueDao()
            historiqueDao.clearHistorique()
            loadHistorique() // 🔥 Rafraîchir après suppression
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
