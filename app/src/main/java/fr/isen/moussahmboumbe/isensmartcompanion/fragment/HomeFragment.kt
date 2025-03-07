package fr.isen.moussahmboumbe.isensmartcompanion.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import fr.isen.moussahmboumbe.isensmartcompanion.R
import fr.isen.moussahmboumbe.isensmartcompanion.adapter.ChatAdapter
import fr.isen.moussahmboumbe.isensmartcompanion.ai.GeminiClient
import fr.isen.moussahmboumbe.isensmartcompanion.database.HistoriqueDatabase
import fr.isen.moussahmboumbe.isensmartcompanion.databinding.FragmentAccueilBinding
import fr.isen.moussahmboumbe.isensmartcompanion.model.ChatMessage
import fr.isen.moussahmboumbe.isensmartcompanion.model.HistoriqueItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {

    private var _binding: FragmentAccueilBinding? = null
    private val binding get() = _binding!!
    private lateinit var chatAdapter: ChatAdapter
    private val messages = mutableListOf<ChatMessage>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccueilBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        GeminiClient.initialize(requireContext())

        chatAdapter = ChatAdapter(messages)
        binding.RecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.RecyclerView.adapter = chatAdapter

        binding.imageSend.setOnClickListener {
            val message = binding.editText.text.toString().trim()
            if (message.isNotEmpty()) {
                showCustomToast("Question soumise")
                sendMessageToGemini(message)
                binding.editText.text.clear()
            }
        }
    }

    private fun sendMessageToGemini(question: String) {
        messages.add(ChatMessage(question, isUser = true))
        chatAdapter.notifyItemInserted(messages.size - 1)
        binding.RecyclerView.smoothScrollToPosition(messages.size - 1)

        CoroutineScope(Dispatchers.Main).launch {
            val response = GeminiClient.getResponse(question)
            messages.add(ChatMessage(response, isUser = false))
            chatAdapter.notifyItemInserted(messages.size - 1)
            binding.RecyclerView.smoothScrollToPosition(messages.size - 1)

            saveToHistory(question, response)
        }
    }

    private fun saveToHistory(question: String, response: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val historiqueDao = HistoriqueDatabase.getDatabase(requireContext()).historiqueDao()
            historiqueDao.insertHistorique(HistoriqueItem(question = question, reponse = response))
        }
    }

    private fun showCustomToast(message: String) {
        val inflater = layoutInflater
        val layout: View = inflater.inflate(R.layout.custom_toast, null)

        val toastText: TextView = layout.findViewById(R.id.toastMessage)
        val toastIcon: ImageView = layout.findViewById(R.id.toastIcon)

        toastText.text = message
        toastIcon.setImageResource(R.drawable.ic_valide) // Utilise ton icône personnalisée

        val toast = Toast(requireContext())
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout
        toast.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
