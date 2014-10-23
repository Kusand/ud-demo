package net.ericschrag.ud_demo;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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


public class GithubUserListFetchActivity extends Activity implements GithubUserSelectedCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_github_user_list_fetch);
    }

    @Override
    protected void onResume() {
        super.onResume();

        final RecyclerView userList = (RecyclerView) findViewById(R.id.users_list);
        final View spinner = findViewById(R.id.spinner);

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

        final GithubService service = restAdapter.create(GithubService.class);

        new AsyncTask<Void, Void, List<GithubUser>>() {
            @Override
            protected List<GithubUser> doInBackground(Void... params) {
                final List<GithubUser> users = service.getUsers();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void userSelected(GithubUser githubUser) {
        Intent intent = new Intent(this, GithubUserDetailActivity.class);
        startActivity(intent);
    }

}
