package com.gabriel.gymtimer.mvp.presenter

import android.util.Log
import com.gabriel.gymtimer.Constants
import com.gabriel.gymtimer.Consts.Companion.USER_COLLECTION
import com.gabriel.gymtimer.Firebase.FirebaseSingleton
import com.gabriel.gymtimer.model.User
import com.gabriel.gymtimer.mvp.contract.RegisterContract
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

class RegisterPresenter(private val view : RegisterContract.View) : RegisterContract.Presenter {

    override fun createUser(user: User) {
        FirebaseSingleton.getFirebaseAuth().createUserWithEmailAndPassword(user.email!!,user.password!!)
            .addOnCompleteListener{
                if (it.isSuccessful){
                    saveUser(user)
                    Log.i("TESTE", "Salvo com sucesso $it")

                }else{
                    Log.i("TESTE", "Erro ao salvar ${it.getResult()}")
                    getExceptions(it)
                    view.onFailure("Houve um erro. Tente novamente")
                    return@addOnCompleteListener

                }
            }.addOnFailureListener {
                Log.i("TESTE", "Erro ao salvar ${it.message}")
                view.onFailure("Houve um erro. Tente novamente")

            }
    }

    private fun saveUser(user: User) {
        FirebaseSingleton.getFirebaseFirestore().collection(USER_COLLECTION)
            .document(FirebaseSingleton.getFirebaseAuth().uid!!)
            .set(user)
            .addOnSuccessListener {

                view.onSuccess()
                Log.i("TESTE", "User salvo ${user.name}")

            }.addOnFailureListener {
                view.onFailure("Houve um erro. Tente novamente")
                Log.i("TESTE", "Error ao salvar user ${it.message}")
            }
    }



    private fun getExceptions(task : Task<AuthResult>){
        try {
            throw task.exception!!
        }catch (e : FirebaseAuthWeakPasswordException){
            view.onFailure("Senha fraca")
        }catch (e : FirebaseAuthInvalidCredentialsException){

            view.onFailure("Email inválido")
        }catch (e : FirebaseAuthUserCollisionException){
            view.onFailure("Usuário já cadastrado")

        }catch (e : Exception){
            view.onFailure("Um erro aconteceu")
            Log.i("TESTE", "Erro ao cadastrar usuario: ${e.message}")
        }
    }


}