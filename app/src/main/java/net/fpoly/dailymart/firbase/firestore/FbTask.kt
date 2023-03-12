package net.fpoly.dailymart.firbase.firestore

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import net.fpoly.dailymart.data.model.Task

private val TAG = "YingMing"

fun insertTask(task: Task) {
    val db = Firebase.firestore
    db.collection(Task.TABLE_NAME)
        .document(task.createAt.toString())
        .set(task)
        .addOnSuccessListener { }
        .addOnFailureListener { }
}

fun updateTask(task: Task) {
    val db = Firebase.firestore
    db.collection(Task.TABLE_NAME)
        .document(task.createAt.toString())
        .set(task)
        .addOnSuccessListener { }
        .addOnFailureListener { }
}

fun getTaskById(id: String, onGetFinish: (task: Task?) -> Unit) {
    val db = Firebase.firestore
    db.collection(Task.TABLE_NAME).document(id)
        .get()
        .addOnSuccessListener {
            onGetFinish(it.toObject(Task::class.java))
        }
}

fun getAllTask(onGetFinish: (listTask: List<Task>) -> Unit) {
    val listTask = ArrayList<Task>()
    val db = Firebase.firestore
    db.collection(Task.TABLE_NAME)
        .get()
        .addOnSuccessListener { result ->
            result.forEach { document ->
                listTask.add(document.toObject(Task::class.java))
            }
            onGetFinish(listTask.sortedByDescending { it.createAt })
        }
}

fun deleteTask(task: Task) {
    val db = Firebase.firestore
    db.collection(Task.TABLE_NAME)
        .document(task.createAt.toString())
        .delete()
}