package com.example.myapplication.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.example.myapplication.R;
import com.example.myapplication.dto.FavoriteLocationDTO;
import com.example.myapplication.dto.RideDTO;
import com.example.myapplication.services.IRideService;
import com.example.myapplication.tools.Retrofit;
import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddFavoriteDialog extends DialogFragment {

    RideDTO ride;
    static FragmentActivity parent;

    public static AddFavoriteDialog newInstance(RideDTO ride, FragmentActivity fragmentActivity) {
        AddFavoriteDialog d = new AddFavoriteDialog();
        AddFavoriteDialog.parent = fragmentActivity;

        Bundle args = new Bundle();
        args.putSerializable("ride", ride);
        d.setArguments(args);

        return d;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        ride = (RideDTO) getArguments().getSerializable("ride");

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_favorite, null);
        builder.setView(dialogView)
                .setTitle("Add route to favorites")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        EditText etName = dialogView.findViewById(R.id.edit_text_add_favorite_name);
                        IRideService rideService = Retrofit.retrofit.create(IRideService.class);
                        Call<FavoriteLocationDTO> fav = rideService.saveFavoriteLocation(new FavoriteLocationDTO(
                                etName.getText().toString(), ride.getLocations(),
                                ride.getVehicleType(), ride.isBabyTransport(), ride.isPetTransport()));
                        fav.enqueue(new Callback<FavoriteLocationDTO>() {
                            @Override
                            public void onResponse(Call<FavoriteLocationDTO> call, Response<FavoriteLocationDTO> response) {
                                if (response.code() == 200) Toast.makeText(parent, "Favorite location saved!", Toast.LENGTH_LONG).show();
                                if (response.code() == 400) Toast.makeText(parent, "Number of favorite locations can't exceed 10.", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFailure(Call<FavoriteLocationDTO> call, Throwable t) {
                                try {
                                    Toast.makeText(parent, "Error when saving favorite location.", Toast.LENGTH_LONG).show();
                                    System.out.println(t.getMessage());
                                } catch (Exception exception) {
                                    System.out.println(exception.toString());
                                }
                            }
                        });

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AddFavoriteDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

}
