package com.jcorp.exams

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.jcorp.exams.databinding.ActivityLoginBinding
import java.lang.Exception
import java.util.*

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("345942039389-je0lkre25blpih25ah5gpu8ckuve486f.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.btnFbLogin.setOnClickListener(this)
        binding.btnGoogleLogin.setOnClickListener(this)


    }

    private fun logInFb(token: String) {
        auth = Firebase.auth
        Log.d("FBLOG", "logInFb: COMEHERE")
        val credential = FacebookAuthProvider.getCredential(token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("FBLOG", "logInFb: SUCCESSFUL")
                    val user = auth.currentUser
                    if (user != null) {
                        updateUserData(auth.currentUser!!.email)
                        upDateToFirebaseGoogle(auth.currentUser!!)
                    }
                }
            }
    }

    private fun googleLogin(idToken: String) {
        auth = Firebase.auth
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        updateUserData(auth.currentUser!!.email)
                        upDateToFirebaseGoogle(auth.currentUser!!)
                        Log.d("----", "googleLogin: ${user.displayName}")
                    }
                }
            }
    }

    private fun updateUserData(email: String?) {
        val prefs = getSharedPreferences("UserData", MODE_PRIVATE)
        prefs.edit().putString("Email", email).apply()
    }

    private fun upDateToFirebaseGoogle(userGoogle: FirebaseUser) {
        val mUser = userGoogle
        val userMap = mutableMapOf<String, Any>()
        userMap["Email"] = mUser.email!!
        userMap["PhotoUrl"] = mUser.photoUrl.toString()
        userMap["NickName"] = mUser.displayName!!
        userMap["Id"] = mUser.email!!.toString().substring(0, mUser.email!!.length - 4)

        FirebaseFirestore.getInstance().collection("UserData")
            .document(userGoogle.email!!)
            .set(userMap)
            .addOnSuccessListener {
                Log.d("----", "upDateToFirebase: SUCCESS")
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                Log.d("----", "upDateToFirebase: $it")
            }
    }

    private fun updateToFirebaseFB(user: CurUserData) {
        val mUser = user
        val userMap = mutableMapOf<String, Any>()
        userMap["Email"] = mUser.Email.toString()
        userMap["PhotoUrl"] = mUser.PhotoUrl.toString()
        userMap["NickName"] = mUser.NickName.toString()

        FirebaseFirestore.getInstance().collection("UserData")
            .document(mUser.Email.toString())
            .set(userMap)
            .addOnSuccessListener {
                Log.d("----", "upDateToFirebase: SUCCESS")
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                Log.d("----", "upDateToFirebase: $it")
            }
    }


    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.btn_fb_login -> {
                Log.d("FBLOG", "onSuccess: CLICKED")
                val loginManager = LoginManager.getInstance()
                val callbackManager = CallbackManager.Factory.create()
                //loginManager.logInWithReadPermissions(this, Arrays.asList("public_profile"))
                loginManager.registerCallback(
                    callbackManager,
                    object : FacebookCallback<LoginResult> {
                        override fun onSuccess(result: LoginResult) {
                            Log.d("FBLOG", "onSuccess: COMEHERE")
                            logInFb(result.accessToken.token)
                        }

                        override fun onCancel() {
                            TODO("Not yet implemented")
                        }

                        override fun onError(error: FacebookException?) {
                            TODO("Not yet implemented")
                        }
                    })


                LoginManager.getInstance()
                    .retrieveLoginStatus(
                        this,
                        object :
                            LoginStatusCallback {
                            override fun onCompleted(accessToken: AccessToken?) {
                                logInFb(accessToken!!.token)
                            }

                            override fun onFailure() {
                                TODO("Not yet implemented")
                            }

                            override fun onError(exception: Exception?) {
                                TODO("Not yet implemented")
                            }

                        })
            }

            R.id.btn_google_login -> {
                val googleSignIntent = googleSignInClient.signInIntent
                startActivityForResult(googleSignIntent, 100)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                googleLogin(account.idToken!!)
            } catch (e: ApiException) {
                Log.d("----", "onActivityResult: Error - $e")
            }
        }
    }
}