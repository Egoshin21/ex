package com.example.ex

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import org.json.JSONObject
import java.lang.Exception

class Login : AppCompatActivity() {
    private lateinit var emailText:EditText
    private lateinit var passText:EditText
    private lateinit var math:String
    private lateinit var username:String
    private lateinit var logButton:Button
    private lateinit var password:String
    private lateinit var token:String
    private lateinit var authButton:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        emailText = findViewById(R.id.login)
        passText = findViewById(R.id.password)
        logButton = findViewById(R.id.login_button)
        val re = Regex("""[a-z0-9]+@[a-z0-9]\.[a-z]{1,3}$""")
        math = re.find(emailText.toString()).toString()
        authButton = findViewById(R.id.auth_button)
    }

    fun login(view: android.view.View) {
        if(emailText.text.isNotEmpty() && passText.text.isNotEmpty() && math!=null ){

            username = emailText.text.toString()
            password = passText.text.toString()
            HTTP.requestPOST("http://s4a.kolei.ru/login",
                JSONObject().put("username",username).put("password", password),
                mapOf("Content-Type" to "application/json")
            ){result, error, code ->
                runOnUiThread {
                    if(result!=null){
                        try {
                            val json = JSONObject(result)
                            if (!json.has("notice"))
                                throw Exception("Не верный формат ответа, ожидался объект notice")
                            if (json.getJSONObject("notice").has("answer"))
                                throw Exception(json.getJSONObject("notice").getString("answer"))
                            if (json.getJSONObject("notice").has("token")) {
                                token = json.getJSONObject("notice").getString("token")
                                runOnUiThread {
                                    Toast.makeText(
                                        this,
                                        "success get token: $token",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }

                            }
                            else{
                                throw Exception("Не верный формат ответа, ожидался объект token")
                            }
                        }
                        catch (e:Exception){
                            runOnUiThread {
                                AlertDialog.Builder(this)
                                    .setTitle("Не работает")
                                    .setMessage(e.message)
                                    .setPositiveButton("Cancel", null)
                                    .create()
                                    .show()
                            }

                        }
                    }
                    else{
                        runOnUiThread {
                            AlertDialog.Builder(this)
                                .setTitle("Ошибка")
                                .setMessage(error)
                                .setPositiveButton("Cancel", null)
                                .create()
                                .show()
                        }
                    }
                }
            }

        }

        else{
            AlertDialog.Builder(this)
                .setTitle("Ошибка")
                .setMessage("Поля пустые, или заполненые не верно")
                .setPositiveButton("Cancel", null)
                .create()
                .show()
        }

    }

    fun logout(view: android.view.View) {
        username = emailText.text.toString()
        password = passText.text.toString()
        HTTP.requestPOST("http://s4a.kolei.ru/logout",
            JSONObject().put("username", username),
            mapOf("Content-Type" to "application/json")
        ){result, error, code ->
            token = ""
            runOnUiThread {
                if(result!=null){

                    Toast.makeText(this, "logoutSucess", Toast.LENGTH_LONG).show()

                }
                else{
                    AlertDialog.Builder(this)
                        .setTitle("Ошибка http-запроса")
                        .setMessage(error)
                        .setPositiveButton("OK", null)
                        .create()
                        .show()
                }

            }
        }
    }
}


