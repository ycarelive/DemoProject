package demo.youngning.cn

import okhttp3.*
import java.io.IOException


fun main() {

    val url = "https://api.github.com/users/rengwuxian/repos"

    val client = OkHttpClient()
    val request: Request = Request.Builder()
        .url(url)
        .build()

    client.newCall(request)
        .enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {

            }

            override fun onResponse(call: Call, response: Response) {

            }

        })
}