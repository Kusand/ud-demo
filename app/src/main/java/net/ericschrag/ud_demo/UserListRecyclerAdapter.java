package net.ericschrag.ud_demo;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.ericschrag.ud_demo.data.model.GithubUser;

import java.util.List;

public class UserListRecyclerAdapter extends RecyclerView.Adapter<UserListRecyclerAdapter.UserHolder> {

    private final List<GithubUser> users;
    private int itemLayoutResource;

    public UserListRecyclerAdapter(List<GithubUser> users, int itemLayoutResource) {
        this.users = users;
        this.itemLayoutResource = itemLayoutResource;
    }

    @Override
    public UserHolder onCreateViewHolder(ViewGroup parentViewGroup, int type) {
        final View view = LayoutInflater.from(parentViewGroup.getContext()).inflate(itemLayoutResource, parentViewGroup, false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(UserHolder userHolder, int position) {
        GithubUser user = users.get(position);
        userHolder.nameText.setText(user.login);
        Picasso.with(userHolder.itemView.getContext()).load(Uri.parse(user.avatar_url)).placeholder(R.color.material_blue_grey_800).into(userHolder.userAvatar);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class UserHolder extends RecyclerView.ViewHolder {

        public final TextView nameText;
        public final ImageView userAvatar;

        public UserHolder(View itemView) {
            super(itemView);
            nameText = (TextView) itemView.findViewById(R.id.user_name);
            userAvatar = (ImageView) itemView.findViewById(R.id.user_avatar);
        }
    }
}
