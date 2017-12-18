package four.awesome.myta.services;

import android.content.Context;
import android.support.annotation.NonNull;

import four.awesome.myta.models.User;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Help access the APIs
 */

public class APIAccessService {

    private User user;

    private RetrofitService service;

    public interface RetrofitService {
        @FormUrlEncoded
        @POST("auth")
        Observable<User> authorize(@Field("username") String username,
                                   @Field("password") String password);
    }

    public APIAccessService() {
        user = null;
        createRetrofitService();
    }

    public APIAccessService(User u) {
        user = u;
        createRetrofitService();
    }

    private void createRetrofitService() {
        String HOST = "https://private-ee70cb-myta.apiary-mock.com/v1/";
        service = new Retrofit.Builder()
                .baseUrl(HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(RetrofitService.class);
    }

    public void subcribeAuthorize(Observer<User> observer, String username, String password) {
        service.authorize(username, password)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
