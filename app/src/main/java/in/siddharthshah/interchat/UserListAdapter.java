package in.siddharthshah.interchat;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by siddharthshah on 9/9/17.
 */

public class UserListAdapter extends ArrayAdapter<User> {

    public UserListAdapter(Context context, ArrayList<User> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        User user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_chatpreview, parent, false);
        }
        Drawable dr = ContextCompat.getDrawable(getContext(), R.drawable.circle);
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        dr.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        ImageView iv = convertView.findViewById(R.id.iv_chaticon);
        iv.setImageDrawable(dr);

        TextView tvName = convertView.findViewById(R.id.tv_username);
        tvName.setText(user.getName());
        return convertView;
    }

}
