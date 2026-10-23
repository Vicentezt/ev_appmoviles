package com.example.dsy1105_004v2026.data.model.repository

import com.example.dsy1105_004v2026.data.model.Credential


class AuthRepository (
    private val validCredential: Credential = Credential.Admin  //viene model
) {

    fun login(username:String,password:String): Boolean{  //viene view

        return username==validCredential.username && password==validCredential.password
    }// fin login


}

