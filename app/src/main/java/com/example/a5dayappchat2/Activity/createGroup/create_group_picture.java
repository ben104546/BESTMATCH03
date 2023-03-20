package com.example.a5dayappchat2.Activity.createGroup;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.a5dayappchat2.R;
import com.example.a5dayappchat2.databinding.FragmentCreateGroupPictureBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link create_group_picture#newInstance} factory method to
 * create an instance of this fragment.
 */
public class create_group_picture extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public create_group_picture() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment create_group_picture.
     */
    // TODO: Rename and change types and number of parameters
    public static create_group_picture newInstance(String param1, String param2) {
        create_group_picture fragment = new create_group_picture();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private FragmentCreateGroupPictureBinding createGroupPictureBinding;
    private static final int REQUEST_CODE2 = 102;
    public static Uri imageUriWallpaperGroup ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        createGroupPictureBinding = FragmentCreateGroupPictureBinding.inflate(inflater,container,false);

        createGroupPictureBinding.btColor.setText(R.string.Choose_Picture);

        createGroupPictureBinding.btColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,REQUEST_CODE2);

            }
        });

        return createGroupPictureBinding.getRoot();


    }

    public static String PICTURESTR ;

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE2 && resultCode == RESULT_OK && data != null){
            imageUriWallpaperGroup=data.getData();

            PICTURESTR = "Picture";
            create_group_color.ColorSTR = "null";

            createGroupPictureBinding.ivSettingBgGroup.setImageURI(imageUriWallpaperGroup);



        }


    }
}