package cn.youngning.retrofitsourcedemo.net

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface GithubService {


    @GET("user/{user}/repos")
    fun listRepos(@Path("user") user : String?) : Call<List<Repo>>

}