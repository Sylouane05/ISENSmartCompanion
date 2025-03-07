package fr.isen.moussahmboumbe.isensmartcompanion.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.isen.moussahmboumbe.isensmartcompanion.R
import fr.isen.moussahmboumbe.isensmartcompanion.model.ChatMessage


class ChatAdapter(private val messages: MutableList<ChatMessage>) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val messageText: TextView = view.findViewById(R.id.messageText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val (message, isUser) = messages[position]
        holder.messageText.text = message

        val params = holder.messageText.layoutParams as ViewGroup.MarginLayoutParams

        if (isUser) {
            // Messages utilisateur (droite, violet)
            holder.messageText.setBackgroundResource(R.drawable.message_user)
            params.marginStart = 50
            params.marginEnd = 8
        } else {
            // Messages IA (gauche, vert)
            holder.messageText.setBackgroundResource(R.drawable.message_ia)
            params.marginStart = 8
            params.marginEnd = 50
        }
        holder.messageText.layoutParams = params
    }

    override fun getItemCount(): Int = messages.size
}
