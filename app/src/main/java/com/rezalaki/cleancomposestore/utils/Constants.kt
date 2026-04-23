package com.rezalaki.cleancomposestore.utils

import com.rezalaki.cleancomposestore.data.model.response.User

object Constants {
    val ME = User(id = 1, firstName = "Reza", lastName = "Laki")
    const val FAKE_BASE_URL = "https://fakebaseurl.test/"
    const val DB_NAME = "StoreDB"
}