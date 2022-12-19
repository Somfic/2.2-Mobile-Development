package com.example.mobile_development_2_2.gui.fragments.home

import com.example.mobile_development_2_2.R

class HelpItem(title: String, imgId: Int) {
    val title = title
    val imgId = imgId
    val description = "We're no strangers to love\n" +
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
            "Never gonna tell a lie and hurt you"


    companion object{
        fun getItems(): List<HelpItem> {
            var item1 = HelpItem("helpItem 1", R.drawable.img_help)
            var item2 = HelpItem("helpItem 2", R.drawable.img_help)
            var item3 = HelpItem("helpItem 3", R.drawable.img_help)
            var item4 = HelpItem("helpItem 4", R.drawable.img_help)
            var item5 = HelpItem("helpItem 5", R.drawable.img_help)
            var item6 = HelpItem("helpItem 6", R.drawable.img_help)
            var item7 = HelpItem("helpItem 7", R.drawable.img_help)
            var item8 = HelpItem("helpItem 8", R.drawable.img_help)

            return listOf(item1, item2, item3, item4, item5, item6, item7, item8)
        }
    }
}
