import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.esg.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Chama a função para buscar e exibir os artigos
        fetchAndDisplayArticles()
    }

    private fun fetchAndDisplayArticles() {
        GlobalScope.launch(Dispatchers.IO) {
            val articles = fetchArticlesFromAPI() // Chama a função para buscar os artigos da API

            runOnUiThread {
                displayArticles(articles) // Exibe os artigos na interface
            }
        }
    }

    private fun fetchArticlesFromAPI(): JSONArray? {
        val url = URL("http://localhost:3000/articles")
        val connection = url.openConnection() as HttpURLConnection

        try {
            val inputStream = connection.inputStream
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val response = StringBuilder()

            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                response.append(line)
            }
            bufferedReader.close()

            return JSONArray(response.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            connection.disconnect()
        }
        return null
    }

    private fun displayArticles(articles: JSONArray?) {
        val recyclerView: RecyclerView = findViewById(R.id.articlesRecyclerView)

        if (articles != null) {
            val layoutManager = LinearLayoutManager(this)
            recyclerView.layoutManager = layoutManager
            val adapter = ArticleAdapter(articles)
            recyclerView.adapter = adapter
        } else {
            // Exibe uma mensagem de erro, se aplicável
            // articlesTextView.text = "Erro ao buscar os artigos."
        }
    }
}

