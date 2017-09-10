package in.siddharthshah.interchat;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 * Created by siddharthshah on 9/9/17.
 */

public class ChatAdapter extends ArrayAdapter<ChatMessage> {

    String myID;
    String senderID;

    public ChatAdapter(Context context, ArrayList<ChatMessage> chatMessages, String myID, String senderID) {
        super(context, 0, chatMessages);
        this.myID = myID;
        this.senderID = senderID;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ChatMessage chatMessage = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if(chatMessage.getUserid().equals(myID)){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_rightchat, parent, false);
        } else {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_leftchat, parent, false);
        }

        TextView tvChatMsg = convertView.findViewById(R.id.tv_chatmsg);
        TextView tvTime = convertView.findViewById(R.id.tv_time);
        tvChatMsg.setText(chatMessage.getMsg());
        Date d = new Date(chatMessage.getTimestamp());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        tvTime.setText(sdf.format(d));
        Log.d("CHAT", chatMessage.getMsg());

        return convertView;
    }
}
