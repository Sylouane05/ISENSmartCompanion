package fr.isen.moussahmboumbe.isensmartcompanion

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import fr.isen.moussahmboumbe.isensmartcompanion.fragment.EventsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import fr.isen.moussahmboumbe.isensmartcompanion.fragment.AgendaFragment
import fr.isen.moussahmboumbe.isensmartcompanion.fragment.HistoriqueFragment
import fr.isen.moussahmboumbe.isensmartcompanion.fragment.HomeFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.navigation_view)

        // Charger le fragment d'accueil par défaut
        loadFragment(HomeFragment())

        // Gérer la navigation entre les fragments
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_accueil -> loadFragment(HomeFragment())
                R.id.item_evenement -> loadFragment(EventsFragment())
                R.id.item_historique -> loadFragment(HistoriqueFragment())
                R.id.item_agenda -> loadFragment(AgendaFragment())





            }
            true
        }
    }

    // Fonction pour charger un fragment dans le `fragment_container`
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentLayout, fragment)
            .commit()
    }
}
