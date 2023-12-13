import android.util.Log
import okhttp3.*
import java.io.IOException

class ApiClient {
    fun fetchArticles() {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("http://localhost:3000/articles")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("ApiClient", "Erro ao buscar os artigos: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                Log.d("ApiClient", "Resposta da API: $responseBody")
            }
        })
    }
}