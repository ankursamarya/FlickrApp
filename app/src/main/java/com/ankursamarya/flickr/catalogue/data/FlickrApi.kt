package com.ankursamarya.flickr.catalogue.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface FlickrApi {
    @GET("?method=flickr.photos.search&api_key=ab726e418794ee185dc80ea739c331e0&format=json&nojsoncallback=1&per_page=10")
    suspend fun getImagesData(@Query("text") text: String?, @Query("page") page: Int): Response<Data>
}