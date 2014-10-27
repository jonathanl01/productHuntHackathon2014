package com.example.jonathanlei.producthuntandroidapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jonathanlei.producthuntandroidapp.model.Post;

import java.util.ArrayList;

/**
 * Created by anthony on 10/27/14.
 */
public class PostsArrayAdapter extends ArrayAdapter {

    private static class ViewHolder {
        TextView name;
        TextView tagline;
        ImageView img;
    }

    public PostsArrayAdapter(Context context, ArrayList<Post> posts) {
        super(context, R.layout.list_item_posts, posts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Post post = (Post) getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item_posts, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.post_nameView);
            viewHolder.tagline = (TextView) convertView.findViewById(R.id.post_tagLineView);
            viewHolder.img = (ImageView) convertView.findViewById(R.id.post_imgView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        viewHolder.name.setText(post.getName());
        viewHolder.tagline.setText(post.getTagline());
        ImageView imageView = viewHolder.img;
        new ImageDownloader(imageView).execute(post.getScreenshot_url_300px());
        // Return the completed view to render on screen
        return convertView;
    }
}
