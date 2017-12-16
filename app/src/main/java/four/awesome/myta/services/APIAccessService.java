package four.awesome.myta.services;

import android.support.annotation.NonNull;

import four.awesome.myta.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.POST;

/**
 * Help access the APIs
 */

public class APIAccessService {

    private User user;

    private RetrofitService service;

    public interface RetrofitService {
        @POST("auth")
        Call<User> authorize(@Field("username") String username,
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
        String HOST = "https://private-ee70cb-myta.apiary-mock.com/v1";
        service = new Retrofit.Builder()
                .baseUrl(HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RetrofitService.class);
    }

    public Call<User> authorize(String username, String password) {
        return service.authorize(username, password);
    }
}
