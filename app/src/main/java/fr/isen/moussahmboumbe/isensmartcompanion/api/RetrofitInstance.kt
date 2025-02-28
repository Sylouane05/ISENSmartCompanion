package fr.isen.moussahmboumbe.isensmartcompanion.api

import fr.isen.moussahmboumbe.isensmartcompanion.model.Event
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


interface EventsApiService {
    @GET("events.json")
    fun getEvents(): Call<List<Event>>
}


object RetrofitInstance {
    private const val BASE_URL = "https://isen-smart-companion-default-rtdb.europe-west1.firebasedatabase.app/"

    val api: EventsApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EventsApiService::class.java)
    }
}

