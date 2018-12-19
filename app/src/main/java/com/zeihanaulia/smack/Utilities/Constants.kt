package com.zeihanaulia.smack.Utilities

const val BASE_URL = "https://cd689fce.ngrok.io/v1/"
//const val BASE_URL = "https://bc6295f0.ngrok.io/v1/"
//const val  URL_REGISTER = "http://bc6295f0.ngrok.io/v1/account/register"
const val URL_REGISTER = "${BASE_URL}account/register"
const val URL_LOGIN = "${BASE_URL}account/login"
const val URL_CREATE_USER = "${BASE_URL}user/add"

// Broadcast content

const  val  BROADCAST_USER_DATA_CHANGE = "BROADCAST_USER_DATA_CHANGE"