import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.coroutineScope
import androidx.paging.PageKeyedDataSource
import com.ankursamarya.flickr.catalogue.data.FlickrApi
import com.ankursamarya.flickr.catalogue.data.Image
import com.ankursamarya.flickr.network.NetworkState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.IOException

class PageKeyedImageDataSource(
    private val flickrApi: FlickrApi,
    private val searchedText: String,
    private val lifecycleOwner: LifecycleOwner
) : PageKeyedDataSource<Int, Image>() {

    // keep a function reference for the retry event
    private var retry: (() -> Any)? = null

    /**
     * There is no sync on the state because paging will always call loadInitial first then wait
     * for it to return some success value before calling loadAfter.
     */
    val networkState = MutableLiveData<NetworkState>()

    val initialLoad = MutableLiveData<NetworkState>()

    fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.let { retry ->
            lifecycleOwner.lifecycle.coroutineScope.launch {
                with(Dispatchers.IO) {
                    retry()
                }
            }
        }
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, Image>
    ) {
        // ignored, since we only ever append to our initial load
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Image>) {
        networkState.postValue(NetworkState.LOADING)
        lifecycleOwner.lifecycle.coroutineScope.launch {
            with(Dispatchers.IO) {
                try {
                    val imagesResponse = flickrApi.getImagesData(searchedText, params.key)
                    if (imagesResponse.isSuccessful) {
                        val data = imagesResponse.body()?.photos?.photo
                        retry = null
                        callback.onResult(data ?: emptyList(), params.key + 1)
                        networkState.postValue(NetworkState.LOADED)
                    } else {
                        retry = {
                            loadAfter(params, callback)
                        }
                        networkState.postValue(
                            NetworkState.error("error code: ${imagesResponse.code()}")
                        )
                    }
                } catch (e: Exception) {
                    retry = {
                        loadAfter(params, callback)
                    }
                    networkState.postValue(NetworkState.error(e.message ?: "unknown err"))
                }
            }
        }
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Image>
    ) {
        runBlocking {
            try {
                val imagesResponse = flickrApi.getImagesData(searchedText, 0)
                val data = imagesResponse.body()?.photos?.photo
                retry = null
                networkState.postValue(NetworkState.LOADED)
                initialLoad.postValue(NetworkState.LOADED)
                callback.onResult(data ?: emptyList(), null, 1)

            } catch (e: IOException) {
                retry = {
                    loadInitial(params, callback)
                }
                val error = NetworkState.error(e.message ?: "unknown error")
                networkState.postValue(error)
                initialLoad.postValue(error)
            }
        }
    }
}