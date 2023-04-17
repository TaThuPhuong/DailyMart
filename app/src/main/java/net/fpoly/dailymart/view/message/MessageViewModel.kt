package net.fpoly.dailymart.view.message

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.fpoly.dailymart.data.model.Message
import net.fpoly.dailymart.firbase.real_time.MessageDao
import net.fpoly.dailymart.utils.SharedPref
import net.fpoly.dailymart.utils.sendNotification

class MessageViewModel(private val app: Application) : ViewModel() {

    private val message = MutableLiveData(Message())
    val listMessage = MutableLiveData<List<Message>>(ArrayList())
    val user = SharedPref.getUser(app)
    val isGetDone = MutableLiveData(false)

    init {
        message.value = message.value?.copy(
            idSend = user.id,
            name = user.name,
            avatar = user.avatar,
        )
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)
    }

    fun getAllMessage() {
        MessageDao.getAllMessage {
            listMessage.value = it
            isGetDone.postValue(true)
        }
    }

    fun onMessageChange(value: String) {
        message.value = message.value?.copy(message = value)
    }

    fun onSendMessage(onSend: () -> Unit) {
        if (message.value!!.message.trim().isNotEmpty()) {
            message.value = message.value?.copy(
                time = System.currentTimeMillis()
            )
            MessageDao.senMessage(message = message.value!!) {
                viewModelScope.launch(Dispatchers.IO) {
                    sendNotification(
                        "${message.value!!.name} đã gửi 1 tin nhắn",
                        message.value!!.message,
                        "",
                        user.id,
                        TOPIC
                    )
                }
                onSend()
            }
        }
    }

    fun onDelete() {
        MessageDao.delete()
        getAllMessage()
    }

    companion object {
        const val TOPIC = "/topics/daily_mart"
    }
}