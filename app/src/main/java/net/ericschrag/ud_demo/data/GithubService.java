package net.ericschrag.ud_demo.data;

import net.ericschrag.ud_demo.data.model.GithubUser;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Headers;
import rx.Observable;

public interface GithubService {
    @Headers("Accept: application/vnd.github.v3+json")
    @GET("/users")
    public Observable<List<GithubUser>> getUsers();
}
