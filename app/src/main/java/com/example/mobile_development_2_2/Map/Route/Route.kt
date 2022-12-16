package com.example.mobile_development_2_2.map.route

import com.example.mobile_development_2_2.R
import com.example.mobile_development_2_2.map.route.POI
import org.osmdroid.util.GeoPoint

class Route(
    val name : String,
    val description : String,
    val POIs: List<POI>
    ) {

    fun addPOI(poi: POI){
        POIs.plus(poi)
    }

    companion object{
        fun testRoute(): Route {
            val avans = POI(
                name = "Avans",
                location = GeoPoint(51.5856, 4.7925),
                imgId = R.drawable.img_poi1,
                streetName = "street1",
                shortDescription = "description of Avans",
                longDescription = "We're no strangers to love\n" +
                        "You know the rules and so do I (do I)\n" +
                        "A full commitment's what I'm thinking of\n" +
                        "You wouldn't get this from any other guy\n" +
                        "I just wanna tell you how I'm feeling\n" +
                        "Gotta make you understand\n" +
                        "Never gonna give you up\n" +
                        "Never gonna let you down\n" +
                        "Never gonna run around and desert you\n" +
                        "Never gonna make you cry\n" +
                        "Never gonna say goodbye\n" +
                        "Never gonna tell a lie and hurt you\n" +
                        "We've known each other for so long\n" +
                        "Your heart's been aching, but you're too shy to say it (say it)\n" +
                        "Inside, we both know what's been going on (going on)\n" +
                        "We know the game and we're gonna play it\n" +
                        "And if you ask me how I'm feeling\n" +
                        "Don't tell me you're too blind to see\n" +
                        "Never gonna give you up\n" +
                        "Never gonna let you down\n" +
                        "Never gonna run around and desert you\n" +
                        "Never gonna make you cry\n" +
                        "Never gonna say goodbye\n" +
                        "Never gonna tell a lie and hurt you\n" +
                        "Never gonna give you up\n" +
                        "Never gonna let you down\n" +
                        "Never gonna run around and desert you\n" +
                        "Never gonna make you cry\n" +
                        "Never gonna say goodbye\n" +
                        "Never gonna tell a lie and hurt you\n" +
                        "We've known each other for so long\n" +
                        "Your heart's been aching, but you're too shy to say it (to say it)\n" +
                        "Inside, we both know what's been going on (going on)\n" +
                        "We know the game and we're gonna play it\n" +
                        "I just wanna tell you how I'm feeling\n" +
                        "Gotta make you understand\n" +
                        "Never gonna give you up\n" +
                        "Never gonna let you down\n" +
                        "Never gonna run around and desert you\n" +
                        "Never gonna make you cry\n" +
                        "Never gonna say goodbye\n" +
                        "Never gonna tell a lie and hurt you\n" +
                        "Never gonna give you up\n" +
                        "Never gonna let you down\n" +
                        "Never gonna run around and desert you\n" +
                        "Never gonna make you cry\n" +
                        "Never gonna say goodbye\n" +
                        "Never gonna tell a lie and hurt you\n" +
                        "Never gonna give you up\n" +
                        "Never gonna let you down\n" +
                        "Never gonna run around and desert you\n" +
                        "Never gonna make you cry\n" +
                        "Never gonna say goodbye\n" +
                        "Never gonna tell a lie and hurt you",
                imgMap = R.drawable.img_map1
            )

            // TODO: Move to POI repository
            val breda = POI(
                name = "Breda",
                location = GeoPoint(51.5719, 4.7683),
                imgId = R.drawable.img_poi2,
                streetName = "street2",
                shortDescription = "description of Breda",
                longDescription = "We're no strangers to love\n" +
                        "You know the rules and so do I (do I)\n" +
                        "A full commitment's what I'm thinking of\n" +
                        "You wouldn't get this from any other guy\n" +
                        "I just wanna tell you how I'm feeling\n" +
                        "Gotta make you understand\n" +
                        "Never gonna give you up\n" +
                        "Never gonna let you down\n" +
                        "Never gonna run around and desert you\n" +
                        "Never gonna make you cry\n" +
                        "Never gonna say goodbye\n" +
                        "Never gonna tell a lie and hurt you\n" +
                        "We've known each other for so long\n" +
                        "Your heart's been aching, but you're too shy to say it (say it)\n" +
                        "Inside, we both know what's been going on (going on)\n" +
                        "We know the game and we're gonna play it\n" +
                        "And if you ask me how I'm feeling\n" +
                        "Don't tell me you're too blind to see\n" +
                        "Never gonna give you up\n" +
                        "Never gonna let you down\n" +
                        "Never gonna run around and desert you\n" +
                        "Never gonna make you cry\n" +
                        "Never gonna say goodbye\n" +
                        "Never gonna tell a lie and hurt you\n" +
                        "Never gonna give you up\n" +
                        "Never gonna let you down\n" +
                        "Never gonna run around and desert you\n" +
                        "Never gonna make you cry\n" +
                        "Never gonna say goodbye\n" +
                        "Never gonna tell a lie and hurt you\n" +
                        "We've known each other for so long\n" +
                        "Your heart's been aching, but you're too shy to say it (to say it)\n" +
                        "Inside, we both know what's been going on (going on)\n" +
                        "We know the game and we're gonna play it\n" +
                        "I just wanna tell you how I'm feeling\n" +
                        "Gotta make you understand\n" +
                        "Never gonna give you up\n" +
                        "Never gonna let you down\n" +
                        "Never gonna run around and desert you\n" +
                        "Never gonna make you cry\n" +
                        "Never gonna say goodbye\n" +
                        "Never gonna tell a lie and hurt you\n" +
                        "Never gonna give you up\n" +
                        "Never gonna let you down\n" +
                        "Never gonna run around and desert you\n" +
                        "Never gonna make you cry\n" +
                        "Never gonna say goodbye\n" +
                        "Never gonna tell a lie and hurt you\n" +
                        "Never gonna give you up\n" +
                        "Never gonna let you down\n" +
                        "Never gonna run around and desert you\n" +
                        "Never gonna make you cry\n" +
                        "Never gonna say goodbye\n" +
                        "Never gonna tell a lie and hurt you",
                imgMap = R.drawable.img_map1
            )

            // TODO: Move to POI repository
            val amsterdam = POI(
                name = "Amsterdam",
                location = GeoPoint(52.3676, 4.9041),
                imgId = R.drawable.img_poi3,
                streetName = "street3",
                shortDescription = "description of Amsterdam",
                longDescription = "We're no strangers to love\n" +
                        "You know the rules and so do I (do I)\n" +
                        "A full commitment's what I'm thinking of\n" +
                        "You wouldn't get this from any other guy\n" +
                        "I just wanna tell you how I'm feeling\n" +
                        "Gotta make you understand\n" +
                        "Never gonna give you up\n" +
                        "Never gonna let you down\n" +
                        "Never gonna run around and desert you\n" +
                        "Never gonna make you cry\n" +
                        "Never gonna say goodbye\n" +
                        "Never gonna tell a lie and hurt you\n" +
                        "We've known each other for so long\n" +
                        "Your heart's been aching, but you're too shy to say it (say it)\n" +
                        "Inside, we both know what's been going on (going on)\n" +
                        "We know the game and we're gonna play it\n" +
                        "And if you ask me how I'm feeling\n" +
                        "Don't tell me you're too blind to see\n" +
                        "Never gonna give you up\n" +
                        "Never gonna let you down\n" +
                        "Never gonna run around and desert you\n" +
                        "Never gonna make you cry\n" +
                        "Never gonna say goodbye\n" +
                        "Never gonna tell a lie and hurt you\n" +
                        "Never gonna give you up\n" +
                        "Never gonna let you down\n" +
                        "Never gonna run around and desert you\n" +
                        "Never gonna make you cry\n" +
                        "Never gonna say goodbye\n" +
                        "Never gonna tell a lie and hurt you\n" +
                        "We've known each other for so long\n" +
                        "Your heart's been aching, but you're too shy to say it (to say it)\n" +
                        "Inside, we both know what's been going on (going on)\n" +
                        "We know the game and we're gonna play it\n" +
                        "I just wanna tell you how I'm feeling\n" +
                        "Gotta make you understand\n" +
                        "Never gonna give you up\n" +
                        "Never gonna let you down\n" +
                        "Never gonna run around and desert you\n" +
                        "Never gonna make you cry\n" +
                        "Never gonna say goodbye\n" +
                        "Never gonna tell a lie and hurt you\n" +
                        "Never gonna give you up\n" +
                        "Never gonna let you down\n" +
                        "Never gonna run around and desert you\n" +
                        "Never gonna make you cry\n" +
                        "Never gonna say goodbye\n" +
                        "Never gonna tell a lie and hurt you\n" +
                        "Never gonna give you up\n" +
                        "Never gonna let you down\n" +
                        "Never gonna run around and desert you\n" +
                        "Never gonna make you cry\n" +
                        "Never gonna say goodbye\n" +
                        "Never gonna tell a lie and hurt you",
                imgMap = R.drawable.img_map1
            )

            // TODO: Move to POI repository
            val cities = listOf(
                avans,
                breda,
                amsterdam,
                avans,
                breda,
                amsterdam,
                avans,
                breda,
                amsterdam,
                avans,
                breda,
                amsterdam,
                avans,
                breda,
                amsterdam,
                avans,
                breda,
                amsterdam,
            )


            return Route("testRoute", "a test route", cities)

        }
    }

}
