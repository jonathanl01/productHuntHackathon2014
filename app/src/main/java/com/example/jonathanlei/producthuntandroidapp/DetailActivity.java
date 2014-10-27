package com.example.jonathanlei.producthuntandroidapp;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jonathanlei.producthuntandroidapp.model.Post;


public class DetailActivity extends Activity {
    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    private ImageView productImage;
    private String productDetail;
    private Bitmap bitmap;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        private static final String LOG_TAG = PlaceholderFragment.class.getSimpleName();

        public PlaceholderFragment() { }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
            Intent intent = getActivity().getIntent();

            if (intent != null) {
                Post post = (Post) intent.getSerializableExtra("post");
                ImageView prodImgView = (ImageView) rootView.findViewById(R.id.detailFragment_productImageView);
                new ImageDownloader(prodImgView).execute(post.getScreenshot_url_300px());
                TextView nameTextView = (TextView) rootView.findViewById(R.id.detailFragment_productTagLineView);
                nameTextView.setText(post.getTagline());
            }

            return rootView;
        }
    }
}
