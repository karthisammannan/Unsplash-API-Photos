package com.android.karthi.androidtask.DataService;

import com.android.karthi.androidtask.POJO.PhotoResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;
/**
 * Created by Karthi on 27/3/2018.
 */


public interface ApiService {
    @GET("photos")
    Single<PhotoResponse> getPhotos(@Query("query") String query,
                                    @Query("client_id") String client_id,
                                    @Query("page") int page,
                                    @Query("per_page") int per_page);
}
