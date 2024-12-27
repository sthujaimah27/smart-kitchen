import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androidmobile_sub02.data.remote.response.Recipe
import com.example.androidmobile_sub02.data.remote.response.RecipeResponse
import com.example.androidmobile_sub02.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class RecipeViewModel : ViewModel() {

    private val _listDataRecipes = MutableLiveData<List<Recipe>>()
    val listDataRecipes: LiveData<List<Recipe>> = _listDataRecipes

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getRecipes() {
        _isLoading.value = true
        val reqBodyMap = mapOf("ingredients" to "ayam")

        // Convert Map to JSON string using Gson
        val json = Gson().toJson(reqBodyMap)

        // Create RequestBody from JSON string
        val reqBody = json.toRequestBody("application/json".toMediaTypeOrNull())

        // Make the network request using the API service
        val client = ApiConfig.getApiService().recommendRecipes(reqBody)
        client.enqueue(object : Callback<RecipeResponse> {
            override fun onResponse(call: Call<RecipeResponse>, response: Response<RecipeResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    // Update the listDataRecipes LiveData with the fetched recipes
                    response.body()?.data?.recommendedRecipes?.let { recipes ->
                        _listDataRecipes.value = recipes
                    }
                } else {
                    Log.e("RecipeViewModel", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RecipeResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("RecipeViewModel", "onFailure: ${t.message}")
            }
        })
    }
}
