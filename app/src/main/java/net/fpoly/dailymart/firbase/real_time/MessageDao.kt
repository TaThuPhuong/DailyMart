package net.fpoly.dailymart.firbase.real_time

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import net.fpoly.dailymart.data.model.Message

object MessageDao {
    fun senMessage(message: Message,onSend :() ->Unit) {
        Log.e("YingMing", "senMessage: $message", )
        val database = Firebase.database
        val myRef = database.getReference("message").child(message.time.toString())
        myRef.setValue(message)
            .addOnSuccessListener {
                onSend()
                Log.e("YingMing", "sen: ")
            }
            .addOnFailureListener {
                Log.e("YingMing", "ex: $it")
            }
    }

    fun getAllMessage(onGetSuccess: (List<Message>) -> Unit) {
        val listMessage = ArrayList<Message>()
        val database = Firebase.database
        val myRef = database.getReference("message")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listMessage.clear()
                snapshot.children.forEach {
                    val value = it.getValue<Message>() ?: Message()
                    listMessage.add(value)
                }
                onGetSuccess(listMessage)
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    fun delete() {
        val database = Firebase.database
        val myRef = database.getReference("message")
        myRef.removeValue()
    }
}