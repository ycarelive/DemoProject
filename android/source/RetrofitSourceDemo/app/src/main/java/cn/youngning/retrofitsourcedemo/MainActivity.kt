package cn.youngning.retrofitsourcedemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cn.youngning.retrofitsourcedemo.net.GithubService
import cn.youngning.retrofitsourcedemo.net.Repo
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Retrofit简单用法
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(GithubService::class.java)

        val repos : Call<List<Repo>> = service.listRepos("octocat")

        repos.enqueue(object : Callback<List<Repo>>{
            override fun onFailure(call: Call<List<Repo>>, t: Throwable) {

            }

            override fun onResponse(call: Call<List<Repo>>, response: Response<List<Repo>>) {
                println("Response: ${response.body()!![0].name}")
            }

        })

        // Okhttp的简单用法
        val url  = "https://api.github.com/users/octocat/repos"
        val client : OkHttpClient = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request)
            .enqueue(object : okhttp3.Callback{
                override fun onFailure(call: okhttp3.Call, e: IOException) {

                }

                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    println("request status code  :${response.code()}" )
                }

            })
    }
}