package net.ericschrag.ud_demo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.UrlConnectionDownloader;

import net.ericschrag.ud_demo.data.GithubService;
import net.ericschrag.ud_demo.data.model.GithubUser;

import java.io.File;
import java.io.IOException;
import java.util.List;

import retrofit.RestAdapter;
import retrofit.client.OkClient;


public class GithubUserListFetchActivity extends ActionBarActivity implements GithubUserSelectedCallback {

    private GithubService githubService;
    private RecyclerView userList;
    private View spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_github_user_list_fetch);
    }

    @Override
    protected void onResume() {
        super.onResume();

        userList = (RecyclerView) findViewById(R.id.users_list);
        spinner = findViewById(R.id.spinner);

        final OkHttpClient okHttpClient = new OkHttpClient();
        try {
            int cacheSize = 10 * 1024 * 1024; // 10 MiB
            File cacheDirectory = new File(this.getCacheDir().getAbsolutePath(), "HttpCache");
            Cache cache = new Cache(cacheDirectory, cacheSize);
            okHttpClient.setCache(cache);
        } catch (IOException e) {
            Log.w("NoCache", "Could not create cache");
        }
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setClient(new OkClient(okHttpClient))
                .setEndpoint("https://api.github.com")
                .build();

        githubService = restAdapter.create(GithubService.class);

        fetchGithubUsers();

    }

    private void fetchGithubUsers() {
        spinner.setVisibility(View.VISIBLE);
        userList.setVisibility(View.GONE);
        new AsyncTask<Void, Void, List<GithubUser>>() {
            @Override
            protected List<GithubUser> doInBackground(Void... params) {
                final List<GithubUser> users = githubService.getUsers();
                return users;
            }

            @Override
            protected void onPostExecute(List<GithubUser> users) {
                spinner.setVisibility(View.GONE);
                userList.setVisibility(View.VISIBLE);
                RecyclerView.Adapter<UserListRecyclerAdapter.UserHolder> userAdapter = new UserListRecyclerAdapter(users, R.layout.user_list_item, GithubUserListFetchActivity.this, new Picasso.Builder(getApplicationContext()).downloader(new UrlConnectionDownloader(getApplicationContext())).build());
                userList.setAdapter(userAdapter);
                userList.setLayoutManager(new LinearLayoutManager(GithubUserListFetchActivity.this, LinearLayoutManager.VERTICAL, false));
            }
        }.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_github_user_list_fetch, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = menuItem.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            fetchGithubUsers();
            return true;
        }

        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void userSelected(GithubUser githubUser) {
        Intent intent = new Intent(this, GithubUserDetailActivity.class);
        intent.putExtra(GithubUserDetailActivity.USER_LOGIN_NAME, githubUser.login);
        intent.putExtra(GithubUserDetailActivity.USER_DETAIL_URL, githubUser.url);
        intent.putExtra(GithubUserDetailActivity.USER_AVATAR_URL, githubUser.avatar_url);
        startActivity(intent);
    }
}
