package com.example.puppetmaster.vokabeltrainer.Adapter;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.example.puppetmaster.vokabeltrainer.R;
import com.example.puppetmaster.vokabeltrainer.models.images.GridButtonItem;
import com.example.puppetmaster.vokabeltrainer.models.images.GridItem;
import com.example.puppetmaster.vokabeltrainer.services.LocalStore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

/**
 * Adapter for image grid in Images Fragment.
 */
public class ImageGridViewAdapter extends ArrayAdapter {

    private static final String TAG = "ImageGridViewAdapter";

    // services
    private LocalStore localStore;

    private Activity activity;
    private int layoutResourceId;
    private ArrayList<GridItem> items;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public ImageGridViewAdapter(Activity activity, int layoutResourceId, ArrayList<GridItem> items) {
        super(activity, layoutResourceId, items);
        this.layoutResourceId = layoutResourceId;
        this.activity = activity;
        this.items = items;
        localStore = new LocalStore(activity);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (items.get(position) instanceof GridButtonItem) {
            LayoutInflater layoutInflater = LayoutInflater.from(activity);
            view = layoutInflater.inflate(R.layout.item_add_button_grid, parent, false);
            Button buttonAddImage = (Button) view.findViewById(R.id.add_button);
            buttonAddImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("addFoto", "add foto button pressed");
                    addPhoto();
                }
            });
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(layoutResourceId, parent, false);
            ImageView imageView = (ImageView) view.findViewById(R.id.image);
            Uri itemUri = items.get(position).getImageUri();
            try {
                ContentResolver contentResolver = activity.getContentResolver();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(contentResolver, itemUri);
                int bitmapHeight = bitmap.getHeight() / (bitmap.getWidth() / 100);
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 100, bitmapHeight, true);
                imageView.setImageBitmap(scaledBitmap);
            } catch (IOException e) {
                Log.e(TAG, e.getMessage(), e.getCause());
                Set<String> imageUrls = localStore.loadImages();
                imageUrls.remove(itemUri.toString());
                localStore.saveImages(imageUrls);
            }
        }

        return view;
    }

    private void addPhoto() {
        ActivityCompat.requestPermissions(
                activity,
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
        );

        int writePermission = ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        );

        int readPermission = ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.READ_EXTERNAL_STORAGE
        );

        if (writePermission == PackageManager.PERMISSION_GRANTED &&
                readPermission == PackageManager.PERMISSION_GRANTED) {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            Fragment fragment = activity.getFragmentManager().findFragmentById(R.id.images_fragment);
            fragment.startActivityForResult(photoPickerIntent, 1);
        }
    }
}
