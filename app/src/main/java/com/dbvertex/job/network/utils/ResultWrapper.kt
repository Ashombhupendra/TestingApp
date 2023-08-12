package com.dbvertex.job.network.utils

sealed class ResultWrapper<T> {
    class Success<T>(val response: T) : ResultWrapper<T>()
    class Failure(val errorMessage: String,val responsecode:Int ) : ResultWrapper<Nothing>()
}