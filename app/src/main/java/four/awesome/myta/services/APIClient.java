package four.awesome.myta.services;

import four.awesome.myta.models.User;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Help access the APIs
 */

public class APIClient {

    private User user;

    private RetrofitService service;

    public interface RetrofitService {
        @GET("auth")
        Observable<Response<User>> authorize(@Query("email") String email,
                                             @Query("password") String password);

        @FormUrlEncoded
        @POST("users")
        Observable<Response<User>> register(@Field("username") String username,
                                            @Field("password") String password,
                                            @Field("email") String email,
                                            @Field("type") String type);
    }

    public APIClient() {
        user = null;
        createRetrofitService();
    }

    public APIClient(User u) {
        user = u;
        createRetrofitService();
    }

    private void createRetrofitService() {
//        String HOST = "https://private-ee70cb-myta.apiary-mock.com/v1/";
        String HOST = "http://119.23.234.29/v1/";
        service = new Retrofit.Builder()
                .baseUrl(HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(RetrofitService.class);
    }

    public void subscribeAuthorize(Observer<Response<User>> observer,
                                   String email, String password) {
        service.authorize(email, password)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void subscribeRegister(Observer<Response<User>> observer, String username,
                                  String password, String email, String type) {
        service.register(username, password, email, type)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
