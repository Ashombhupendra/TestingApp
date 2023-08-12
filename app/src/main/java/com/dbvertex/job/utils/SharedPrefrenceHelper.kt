package com.dbvertex.job.utils

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.dbvertex.job.JobApp
import com.dbvertex.job.data.UserEntity
import com.google.gson.Gson
import java.lang.Exception

object SharedPrefrenceHelper {
    private const val LOGIN_STATUS = "LOGIN_STATUS"
    private const val SIGNUP_STATUS = "SIGNUP_STATUS"
    private const val INTRODUCTION = "INTRO"
    private const val USER = "USER"


    private val sharedPrefs: SharedPreferences

    init {
        val context = JobApp.instance
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context)
    }

    var isLoggedIn: Boolean
        get() {
            return sharedPrefs.getBoolean(LOGIN_STATUS, false)
        }
        set(value) {
            sharedPrefs.edit {
                putBoolean(LOGIN_STATUS, value)
            }
        }

    var isSignupComleted: Boolean
        get() {
            return sharedPrefs.getBoolean(SIGNUP_STATUS, false)
        }
        set(value) {
            sharedPrefs.edit {
                putBoolean(SIGNUP_STATUS, value)
            }
        }

    var isIntro : Boolean
             get() {
                 return sharedPrefs.getBoolean(INTRODUCTION, false)
             }
           set(value) {
               sharedPrefs.edit{
                   putBoolean(INTRODUCTION, value)
               }
           }


    var user: UserEntity
        set(value) = sharedPrefs.edit {
            val userJson = Gson().toJson(value)
            putString(USER, userJson)
            Log.d("Users_", userJson.toString())
        }
        get() {
            val userString = sharedPrefs.getString(USER, null) ?: throw Exception("User not Found")
            Log.d("Users_", userString)



            return  Gson().fromJson(userString, UserEntity::class.java)
        }

}