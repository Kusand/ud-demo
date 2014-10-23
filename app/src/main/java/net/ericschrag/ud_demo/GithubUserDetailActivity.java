package net.ericschrag.ud_demo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;


public class GithubUserDetailActivity extends Activity {

    public static final String USER_LOGIN_NAME = "USER_LOGIN_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_github_user_detail);

        TextView userLogin = (TextView) findViewById(R.id.user_name);
        String name = getIntent().getStringExtra(USER_LOGIN_NAME);
        userLogin.setText(name);
    }

}
