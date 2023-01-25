package com.example.myapplication.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.activities.PassengerAccountActivity;
import com.example.myapplication.adapters.RidePassengersAdapter;
import com.example.myapplication.dto.PassengerDTO;
import com.example.myapplication.dto.PassengerRequestDTO;
import com.example.myapplication.models.User;
import com.example.myapplication.services.AuthService;
import com.example.myapplication.services.IPassengerService;
import com.example.myapplication.tools.FragmentTransition;
import com.example.myapplication.tools.Retrofit;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PassengerEditAccountFragment extends Fragment {

    private PassengerDTO passenger;
    private int GALLERY_REQ_CODE = 4000;
    private AuthService authService;

    public static PassengerEditAccountFragment newInstance(PassengerDTO passenger) {
        PassengerEditAccountFragment fragment = new PassengerEditAccountFragment();
        Bundle args = new Bundle();
        args.putSerializable("passenger", passenger);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authService = new AuthService(getActivity());
        passenger = (PassengerDTO) getArguments().getSerializable("passenger");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_passenger_edit_account, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setUpData();
        setUpButtons();
    }

    private void setUpData() {
        EditText etName = getView().findViewById(R.id.et_passacc_name);
        etName.setText(passenger.getName());
        EditText etLastName = getView().findViewById(R.id.et_passacc_lastname);
        etLastName.setText(passenger.getSurname());
        EditText etPhone = getView().findViewById(R.id.et_passacc_phone);
        if (passenger.getTelephoneNumber() != null) etPhone.setText(passenger.getTelephoneNumber());
        EditText etAddress = getView().findViewById(R.id.et_passacc_address);
        if (passenger.getAddress() != null) etAddress.setText(passenger.getAddress());
        EditText etEmail = getView().findViewById(R.id.et_passacc_email);
        etEmail.setText(passenger.getEmail());
        ImageView imageView = (ImageView) getView().findViewById(R.id.img_passacc_picture);
        if (passenger.getProfilePicture() != null) {
            try {
                if (!passenger.getProfilePicture().startsWith("data")) new RidePassengersAdapter.DownloadImageTask(imageView).execute(passenger.getProfilePicture());
                else {
                    final String encodedString = passenger.getProfilePicture();
                    final String pureBase64Encoded = encodedString.substring(encodedString.indexOf(",")  + 1);
                    final byte[] decodedBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
                    Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                    imageView.setImageBitmap(decodedBitmap);
                }
            } catch (Exception exception) {
                Log.i("TAG", "Error when displaying passenger image.");
            }
        }

        etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                    Toast.makeText(getActivity(), "If you change email, you will be required to log in again.", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void setUpButtons() {

        IPassengerService passengerService = Retrofit.retrofit.create(IPassengerService.class);

        Button btnChangePicture = (Button) getView().findViewById(R.id.btn_passacc_changePicture);
        btnChangePicture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                galleryIntent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, GALLERY_REQ_CODE);
            }
        });
        Button btnSave = (Button) getView().findViewById(R.id.btn_passacc_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Call<PassengerDTO> passengerResponseCall = passengerService.updatePassenger(
                        passenger.getId().intValue(), extractRequestDTO());
                passengerResponseCall.enqueue(new Callback<PassengerDTO>() {
                    @Override
                    public void onResponse(Call<PassengerDTO> call, Response<PassengerDTO> response) {
                        if (response.code() == 200) {
                            Toast.makeText(getActivity(),
                                    "Changes saved", Toast.LENGTH_LONG).show();

                            EditText etEmail = getView().findViewById(R.id.et_passacc_email);
                            if (!etEmail.getText().toString().equals(passenger.getEmail())) {
                                authService.logout();
                            } else {
                                passenger = response.body();
                                setUpData();
                                try {
                                    ((PassengerAccountActivity) getActivity()).setUpData(passenger);
                                } catch (NullPointerException ex) {
                                    Toast.makeText(getActivity(), "Reopen activity page to see changes.", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                        else {
                            System.out.println(response.code());
                            System.out.println(response.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<PassengerDTO> call, Throwable t) {
                        Log.d("TAG", "greska", t);
                        Toast.makeText(getActivity(),
                                "Couldn't update account at the moment", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private PassengerRequestDTO extractRequestDTO() {
        PassengerRequestDTO requestDTO = new PassengerRequestDTO();
        EditText etName = getView().findViewById(R.id.et_passacc_name);
        requestDTO.setName(etName.getText().toString());
        EditText etLastName = getView().findViewById(R.id.et_passacc_lastname);
        requestDTO.setSurname(etLastName.getText().toString());
        EditText etPhone = getView().findViewById(R.id.et_passacc_phone);
        requestDTO.setTelephoneNumber(etPhone.getText().toString());
        EditText etAddress = getView().findViewById(R.id.et_passacc_address);
        requestDTO.setAddress(etAddress.getText().toString());
        EditText etEmail = getView().findViewById(R.id.et_passacc_email);
        requestDTO.setEmail(etEmail.getText().toString());

        ImageView imageView = (ImageView) getView().findViewById(R.id.img_passacc_picture);

        // Convert image drawable to Base64 String
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream); // bm is the bitmap object
        byte[] b = outputStream.toByteArray();
        String encodeString = Base64.encodeToString(b, Base64.NO_WRAP);
        requestDTO.setProfilePicture("data:image/jpeg;base64," + encodeString);

        return requestDTO;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == GALLERY_REQ_CODE) {
            ImageView imageView = (ImageView) getView().findViewById(R.id.img_passacc_picture);
            imageView.setImageURI(data.getData());
        }
    }

}