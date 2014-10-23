package net.ericschrag.ud_demo;

import net.ericschrag.ud_demo.data.model.GithubUser;

public interface GithubUserSelectedCallback {
    void userSelected(GithubUser githubUser);
}