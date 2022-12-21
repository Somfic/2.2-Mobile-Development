package com.example.mobile_development_2_2.gui.fragments.home

import android.util.Log
import com.example.mobile_development_2_2.R

class HelpItem(title: String, imgId: Int, description: String) {
    val title = title
    val imgId = imgId
    val description = description


    companion object{
        fun getItems(): List<HelpItem> {
            var item1 = HelpItem("Route selecteren", R.drawable.ic_launcher_background, "Klik op de middelste knop onderaan het scherm en selecteer een route door op kaart te drukken.")
            var item2 = HelpItem("Route lopen", R.drawable.ic_launcher_background, "Druk op de start route knop als je binnen op het kaart scherm ben.")
            var item3 = HelpItem("Route pauzeren", R.drawable.ic_launcher_background, "Als je een route heb gestart, druk op het kruisje links boven het scherm.")
            var item4 = HelpItem("POI bekijken", R.drawable.ic_launcher_background, "Als je een route aan het lopen krijg je binnen 20 meter van een trekpluisten een notificatie om de trekpluister te bekijken.")
            var item5 = HelpItem("POI op kaart bekijken", R.drawable.ic_launcher_background, "Als je de kaart aan het bekijken ben kan je op een puntje klikken om de trekplijster te bekijken.")
            var item6 = HelpItem("POI vooraf bekijken", R.drawable.ic_launcher_background, "Als je een lijst met trekplijsters wilt zien, kunt u naar de POI pagina recht onder het scherm. Dit laat de trekplijsters zien van je geselecteerde route.")
            var item7 = HelpItem("Taal verranderen", R.drawable.ic_launcher_background, "Om de taal te verranderen kan u naar de instellingen rechts boven. Daar kan u klikken op een taal om het te verranderen.")
            var item8 = HelpItem("Kleurenblinde modus", R.drawable.ic_launcher_background, "Als u moeite heeft met kleuren kunt u naar instellingen recht boven. Daar kunt u op het knopje naast kleurenblinde modus klikken om de app zwart-wit te maken.")

            return listOf(item1, item2, item3, item4, item5, item6, item7, item8)
        }

        var selectedItem = HelpItem("should be null", R.drawable.ic_launcher_background, "how")

        fun selectItem(item: HelpItem){
            Log.d("a", "Item selected")
            selectedItem = item
        }

        @JvmName("getSelectedItem1")
        fun getSelectedItem(): HelpItem{
            return selectedItem
        }
    }
}
