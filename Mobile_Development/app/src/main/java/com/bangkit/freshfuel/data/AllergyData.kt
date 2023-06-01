package com.bangkit.freshfuel.data

import com.bangkit.freshfuel.model.AllergyItem

class AllergyData {
    companion object {
        fun getAllergy(): List<AllergyItem> = listOf(
            AllergyItem("alcoholic"),
            AllergyItem("almond"),
            AllergyItem("peanut"),
            AllergyItem("egg"),
            AllergyItem("milk"),
            AllergyItem("wheat"),
            AllergyItem("soy"),
            AllergyItem("chicken"),
            AllergyItem("beef"),
            AllergyItem("cashew"),
            AllergyItem("cheese"),
            AllergyItem("clam"),
            AllergyItem("shrimp"),
            AllergyItem("coconut"),
            AllergyItem("dairy"),
            AllergyItem("fish"),
            AllergyItem("pork"),
            AllergyItem("seafood"),
            AllergyItem("squid")
        )
    }
}