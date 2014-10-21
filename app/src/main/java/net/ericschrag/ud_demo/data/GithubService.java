package net.ericschrag.ud_demo.data;

import net.ericschrag.ud_demo.data.model.GithubUser;

import java.util.List;

import retrofit.http.GET;

public interface GithubService {
    @GET("/users")
    public List<GithubUser> getUsers();
}
