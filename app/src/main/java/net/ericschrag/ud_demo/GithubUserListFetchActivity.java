package net.ericschrag.ud_demo;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import net.ericschrag.ud_demo.data.GithubService;
import net.ericschrag.ud_demo.data.model.GithubUser;

import java.util.ArrayList;
import java.util.List;

import retrofit.RestAdapter;


public class GithubUserListFetchActivity extends Activity {

    private static final String[] FAKE_NAME_ARRAY = new String[]{"Anna", "Brian", "Cantor", "Diana", "Eric", "Frank", "George", "Harry", "Ingrid", "Michu", "Nero", "Olivia"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_github_user_list_fetch);
    }

    @Override
    protected void onResume() {
        super.onResume();

        final ListView userList = (ListView) findViewById(R.id.users_list);
        ArrayAdapter<String> nameAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, FAKE_NAME_ARRAY);
        userList.setAdapter(nameAdapter);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://api.github.com")
                .build();

        final GithubService service = restAdapter.create(GithubService.class);

        new AsyncTask<Void, Void, List<String>>() {
            @Override
            protected List<String> doInBackground(Void... params) {
                service.getUsers();
                List<String> fetchedNames = new ArrayList<String>();
                final List<GithubUser> users = service.getUsers();
                for (GithubUser user : users) {
                    fetchedNames.add(user.login);
                }
                return fetchedNames;
            }

            @Override
            protected void onPostExecute(List<String> strings) {
                String[] fetchedNames = new String[strings.size()];
                strings.toArray(fetchedNames);
                ArrayAdapter<String> nameAdapter = new ArrayAdapter<String>(GithubUserListFetchActivity.this, android.R.layout.simple_list_item_1, fetchedNames);
                userList.setAdapter(nameAdapter);
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
}
