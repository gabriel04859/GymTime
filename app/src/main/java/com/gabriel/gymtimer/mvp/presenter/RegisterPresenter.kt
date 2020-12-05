package com.gabriel.gymtimer.mvp.presenter

import android.util.Log
import com.gabriel.gymtimer.Consts.Companion.USER_COLLECTION
import com.gabriel.gymtimer.Firebase.FirebaseSingleton
import com.gabriel.gymtimer.model.User
import com.gabriel.gymtimer.mvp.contract.RegisterContract
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*

class RegisterPresenter(private val view : RegisterContract.View) : RegisterContract.Presenter {

    override fun createUser(user: User) {
        val fbAuth = FirebaseAuth.getInstance()
        fbAuth.createUserWithEmailAndPassword(user.email!!,user.password!!)
            .addOnCompleteListener{
                if (it.isSuccessful){
                    val userFinal = User(fbAuth.uid,user.name,user.email,user.password,user.phone,user.imageUser,user.boss,user.frequentaGym)
                    saveUser(userFinal)
                    Log.i("TESTE", "Salvo com sucesso ${userFinal.idUser}")

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