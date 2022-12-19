package com.example.mobile_development_2_2.gui.fragments.home

import com.example.mobile_development_2_2.R

class HelpItem(title: String, imgId: Int) {
    val title = title
    val imgId = imgId


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
