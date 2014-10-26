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

        productImage = (ImageView)findViewById(R.id.productImage);
        //bitmap = getBitmapFromURL(testString);

        new ImageDownloader(productImage).execute("https://api.url2png.com/v6/P5329C1FA0ECB6/be123d197e397a077581099b234283b3/png/?thumbnail_max_width=300&url=http%3A%2F%2Fmakeappicon.com%2F");

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
        String testString = "https://pbs.twimg.com/profile_images/2284174624/4l5krl3re8cpp0nfsgw6.png";
        private String mPostsStr;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
            Intent intent = getActivity().getIntent();


            if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
                this.mPostsStr = intent.getStringExtra(Intent.EXTRA_TEXT);

                //((TextView) rootView.findViewById(R.id.detailFragment_textView)).setText(mPostsStr);
            }




            return rootView;
        }


    }

}
