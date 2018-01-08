package four.awesome.myta.services;

import java.util.Currency;
import java.util.List;
import java.util.Vector;

import four.awesome.myta.models.Assignment;
import four.awesome.myta.models.Attendance;
import four.awesome.myta.models.Course;
import four.awesome.myta.models.User;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
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
                                            @Field("name") String name,
                                            @Field("campus_id") String campusID,
                                            @Field("phone") String phone,
                                            @Field("email") String email,
                                            @Field("type") String type);

        @FormUrlEncoded
        @PATCH("users/{ID}/courses")
        Observable<Response<User>> appendCourse(@Field("course") int courseID,
                                                @Path("ID") int userID,
                                                @Query("api_key") String apiKey);

        @FormUrlEncoded
        @PUT("users/{ID}")
        Observable<Response<User>> updateUser(@Path("ID") int userID,
                                              @Query("api_key") String apiKey,
                                              @Field("username") String username,
                                              @Field("password") String password,
                                              @Field("name") String name,
                                              @Field("campus_id") String campusID,
                                              @Field("phone") String phone,
                                              @Field("email") String email,
                                              @Field("type") String type);

        @FormUrlEncoded
        @POST("courses")
        Observable<Response<Course>> createCourse(@Query("api_key") String apiKey,
                                                  @Field("course_name") String courseName,
                                                  @Field("teacher_id") int teacherID,
                                                  @Field("teacher_name") String teacherName);

        @GET("courses")
        Observable<Response<List<Course>>> listAllCourses(@Query("api_key") String apiKey);

        @GET("courses")
        Observable<Response<List<Course>>> listCoursesByUserID(@Query("api_key") String apiKey,
                                                               @Query("user_id") int userID);
        @GET("assignments")
        Observable<Response<List<Assignment>>> getAssignments(@Query("api_key") String apiKey,
                                                        @Query("user_id") int userId);

        @GET("users")
        Observable<Response<List<User>>> getUsers(@Query("api_key") String apiKey,
                                               @Query("course_id") int courseId,
                                               @Query("type") String type);

        @FormUrlEncoded
        @POST("assignments")
        Observable<Response<Assignment>> createAssignment(@Query("api_key") String apiKey,
                                                          @Field("ass_name") String asgmName,
                                                          @Field("publish_time") String pTime,
                                                          @Field("end_time") String eTime,
                                                          @Field("detail") String detail,
                                                          @Field("course_id") int courseId,
                                                          @Field("course_name") String courseName);
        @DELETE("assignments/{ID}")
        Observable<Response<Void>> deleteAssignment(@Path("ID") int assignId, @Query("api_key") String apiKey);

        @FormUrlEncoded
        @POST("rollcalls")
        Observable<Response<Attendance>> createAttendance(@Query("api_key") String apiKey,
                                                          @Field("course_id") int courseId,
                                                          @Field("last") int last);

        @GET
        Observable<Response<Attendance>> getAttendanceCode(@Query("api_key") String apiKey,
                                                           @Query("course_id") int courseId,
                                                           @Query("attendance_id") int attendanceId);

        @FormUrlEncoded
        @PATCH
        Observable<Response<Attendance>> callAttendance(@Query("api_key") String apiKey,
                                                        @Field("user_id") int userId,
                                                        @Field("code") String code);

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

    public void subscribeDeleteAssign(Observer<Response<Void>> observer, int assignId, String apiKey) {
        service.deleteAssignment(assignId, apiKey)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void subscribeGetAssign(Observer<Response<List<Assignment>>> observer, String apiKey, int userId) {
        service.getAssignments(apiKey, userId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void subscribeCreateAssign(Observer<Response<Assignment>> observer, String apiKey,
                                      String asgName, String pTime, String eTime,
                                      String detail, int courseId, String courseName) {
        service.createAssignment(apiKey, asgName, pTime, eTime, detail, courseId, courseName)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void subscribeRegister(Observer<Response<User>> observer, String username,
                                  String password, String name, String campusID,
                                  String phone, String email, String type) {
        service.register(username, password, name, campusID, phone, email, type)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void subscribeUpdateUser(Observer<Response<User>> observer, String apiKey,
                                    int userID, String username, String password, String name,
                                    String campusID, String phone, String email, String type) {
        service.updateUser(userID, apiKey, username, password, name, campusID, phone, email, type)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }


    public void subscribeAllCourse(Observer<Response<List<Course>>> observer, String apiKey) {
        service.listAllCourses(apiKey)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
    public void subscribeGetUsers(Observer<Response<List<User>>> observer, String apiKey, int courseId, String type) {
        service.getUsers(apiKey, courseId, type)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
    public void subscribeCourse(Observer<Response<List<Course>>> observer, String apiKey, int id) {
        service.listCoursesByUserID(apiKey, id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void subscribeNewCourse(Observer<Response<Course>> observer, String apiKey,
                                   String courseName, int teacherID, String teacherName) {
        service.createCourse(apiKey, courseName, teacherID, teacherName)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void subscribeAppendCourse(Observer<Response<User>> observer, int courseId,
                                      int userId, String apiKey) {
        service.appendCourse(courseId, userId, apiKey)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void subscribeCreateAttendanceCheck(Observer<Response<Attendance>> observer, int courseId,
                                            int last, String apiKey) {
        service.createAttendance(apiKey, courseId, last)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void subscribeGetAttendanceCode(Observer<Response<Attendance>> observer, int courseId,
                                           int attendanceId, String apiKey) {
        service.getAttendanceCode(apiKey, courseId, attendanceId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void subscribeCallAttendance(Observer<Response<Attendance>> observer,
                                        int userId, String code, String apiKey) {
        service.callAttendance(apiKey, userId, code)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        // TODO: 18-1-8 有bug，"java.lang.IllegalArgumentException: Missing either @PATCH URL or @Url parameter."
    }
}
