package net.ericschrag.ud_demo;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.UrlConnectionDownloader;


public class GithubUserDetailActivity extends Activity {

    public static final String USER_LOGIN_NAME = "USER_LOGIN_NAME";
    public static final String USER_DETAIL_URL = "USER_DETAIL_URL";
    public static final String USER_AVATAR_URL = "USER_AVATAR_URL";
    public static final String EXTRA_IMAGE = "GithubUserDetailActivity:image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_github_user_detail);


        TextView userLogin = (TextView) findViewById(R.id.user_name);
        ImageView avatar = (ImageView) findViewById(R.id.user_avatar);
        String name = getIntent().getStringExtra(USER_LOGIN_NAME);
        String url = getIntent().getStringExtra(USER_DETAIL_URL);
        String avatar_url = getIntent().getStringExtra(USER_AVATAR_URL);
        userLogin.setText(name);

        ViewCompat.setTransitionName(avatar, EXTRA_IMAGE);
        new Picasso.Builder(this).downloader(new UrlConnectionDownloader(getApplicationContext())).build().load(Uri.parse(avatar_url)).into(avatar);
    }

}
