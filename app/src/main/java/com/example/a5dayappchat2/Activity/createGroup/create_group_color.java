package com.example.a5dayappchat2.Activity.createGroup;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.a5dayappchat2.R;
import com.example.a5dayappchat2.databinding.FragmentCreateGroupColorBinding;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link create_group_color#newInstance} factory method to
 * create an instance of this fragment.
 */
public class create_group_color extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public create_group_color() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment create_group_color.
     */
    // TODO: Rename and change types and number of parameters
    public static create_group_color newInstance(String param1, String param2) {
        create_group_color fragment = new create_group_color();
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

    private FragmentCreateGroupColorBinding createGroupColorBinding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        createGroupColorBinding = FragmentCreateGroupColorBinding.inflate(inflater,container,false);

        createGroupColorBinding.btChoseColor.setText(R.string.Choose_Color);

        createGroupColorBinding.btChoseColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                OpenPopUpColor();

            }
        });

        return createGroupColorBinding.getRoot();
    }

    public static int ColorCode ;

    public static String ColorSTR ;

    private void OpenPopUpColor() {

        AlertDialog builder = new ColorPickerDialog.Builder(getActivity())
                .setTitle("ColorPicker Dialog")
                .setPreferenceName("MyColorPickerDialog")
                .setPositiveButton(getString(R.string.Choose_Color),
                        new ColorEnvelopeListener() {
                            @Override
                            public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                                int color = envelope.getColor();
                                ColorCode = color;

                                create_group_picture.PICTURESTR = "null";

                                ColorSTR = "Color";
                                createGroupColorBinding.ViewColor.setBackgroundColor(color);

                            }
                        })
                .setNegativeButton(getString(R.string.Cancel),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                .attachAlphaSlideBar(true)
                .attachBrightnessSlideBar(true)
                .setBottomSpace(12)
                .show();
    }
}