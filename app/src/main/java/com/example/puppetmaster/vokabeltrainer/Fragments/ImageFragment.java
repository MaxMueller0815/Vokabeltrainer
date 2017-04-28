package com.example.puppetmaster.vokabeltrainer.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.example.puppetmaster.vokabeltrainer.R;
import com.example.puppetmaster.vokabeltrainer.Introduction.ImageDetailsActivity;
import com.example.puppetmaster.vokabeltrainer.Adapter.ImageGridViewAdapter;
import com.example.puppetmaster.vokabeltrainer.models.images.GridButtonItem;
import com.example.puppetmaster.vokabeltrainer.models.images.GridImageItem;
import com.example.puppetmaster.vokabeltrainer.models.images.GridItem;
import com.example.puppetmaster.vokabeltrainer.services.LocalStore;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static android.app.Activity.RESULT_OK;


public class ImageFragment extends Fragment {

    public static final String TAG = "ImageFragment";

    // services
    private LocalStore localStore;
    private View view;

    private ArrayList<GridItem> gridItems;
    private GridView imageGridView;
    private ImageGridViewAdapter gridAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "start onCreateView");
        view = inflater.inflate(R.layout.fragment_intro_fourth_image, container, true);

        // init services
        localStore = new LocalStore(getActivity());

        // init view's elements
        repaintGridAdapter();



        // button's listeners
        imageGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                GridItem gridItem = (GridItem) parent.getItemAtPosition(position);

                if (gridItem instanceof GridImageItem) {
                    Uri imageUri = gridItem.getImageUri();

                    // сreate intent
                    Intent intent = new Intent(getActivity(), ImageDetailsActivity.class);
                    intent.putExtra("imageUri", imageUri);

                    // start details activity
                    startActivityForResult(intent, 2);
                }
            }
        });

        return view;
    }

    public ArrayList<GridItem> getGridItems() {
        return gridItems;
    }

    /**
     * For running image chooser and getting result.
     *
     * @param requestCode         - identificator for know who return result.
     * @param resultCode          - result code of operation (success or fail).
     * @param imageReturnedIntent - data of operation.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        Log.d(TAG, "onActivityResult");
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    gridItems.add(gridItems.size() - 1, new GridImageItem(selectedImage));
                    Set<String> uris = localStore.loadImages();
                    uris.add(selectedImage.toString());
                    localStore.saveImages(uris);
                    imageGridView.setAdapter(gridAdapter);
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    repaintGridAdapter();
                }
                break;
        }
    }

    void repaintGridAdapter() {
        Set<String> uris = localStore.loadImages();
        Log.d(TAG, "saved images == > " + uris);
        gridItems = new ArrayList<>(uris.size() + 1);
        Set<String> checkedUris = new HashSet<>();
        for (String imageUri : uris) {
            try {
                File file = new File(new URI(imageUri));
                if (file.exists()) {
                    checkedUris.add(imageUri);
                    gridItems.add(new GridImageItem(Uri.parse(imageUri)));
                }
            } catch (URISyntaxException e) {
                Log.e(TAG, e.getMessage(), e.getCause());
            }
        }
        localStore.saveImages(checkedUris);
        gridItems.add(new GridButtonItem());
        imageGridView = (GridView) view.findViewById(R.id.image_grid_view);
        gridAdapter = new ImageGridViewAdapter(getActivity(), R.layout.item_photo_grid, gridItems);
        imageGridView.setAdapter(gridAdapter);
    }
}
