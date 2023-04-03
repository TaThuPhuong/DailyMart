import net.fpoly.dailymart.data.api.UserApi
import net.fpoly.dailymart.data.model.User
import net.fpoly.dailymart.data.model.param.RegisterParam

class UserRepositor(private val myApiService: UserApi) {
    fun postUser(user: RegisterParam): User {
        return myApiService.register(user)
    }
}