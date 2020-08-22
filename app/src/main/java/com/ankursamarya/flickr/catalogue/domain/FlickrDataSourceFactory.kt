
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.ankursamarya.flickr.catalogue.data.FlickrApi
import com.ankursamarya.flickr.catalogue.data.Image


class FlickrDataSourceFactory(
    private val flickrApi: FlickrApi,
    private val serchedText: String,
    private val lifecycleOwner: LifecycleOwner
) : DataSource.Factory<Int, Image>() {

    val sourceLiveData = MutableLiveData<PageKeyedImageDataSource>()

    override fun create(): DataSource<Int, Image> {

        val source = PageKeyedImageDataSource(flickrApi, serchedText, lifecycleOwner)

        sourceLiveData.postValue(source)

        return source
    }
}
