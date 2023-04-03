package net.fpoly.dailymart.data.model.root

import net.fpoly.dailymart.data.model.User

data class LoginRoot(
    var status: Int = 0,
    var message: String = "",
    var data: User
)
