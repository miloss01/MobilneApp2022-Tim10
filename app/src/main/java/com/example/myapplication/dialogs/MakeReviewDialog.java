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

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.myapplication.R;
import com.example.myapplication.dto.DriverReviewRequestDTO;
import com.example.myapplication.dto.ReasonDTO;
import com.example.myapplication.dto.VehicleReviewRequestDTO;
import com.example.myapplication.services.IReviewService;
import com.example.myapplication.services.IRideService;
import com.example.myapplication.tools.Retrofit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MakeReviewDialog extends DialogFragment {

    public Integer rideId;

    public static MakeReviewDialog newInstance(int rideId) {
        MakeReviewDialog d = new MakeReviewDialog();

        Bundle args = new Bundle();
        args.putInt("rideId", rideId);
        d.setArguments(args);

        return d;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        rideId = getArguments().getInt("rideId");

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_make_review, null);
        builder.setView(dialogView)
                .setTitle("Make review")
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        CheckBox driverCb = dialogView.findViewById(R.id.make_review_driver_cb);
                        if (driverCb.isChecked()) {
                            EditText driverNum = dialogView.findViewById(R.id.make_review_driver_text);
                            EditText driverComment = dialogView.findViewById(R.id.make_review_driver_comment);
                            sendReview(driverNum.getText().toString(), driverComment.getText().toString(), true);
                        }

                        CheckBox vehicleCb = dialogView.findViewById(R.id.make_review_vehicle_cb);
                        if (vehicleCb.isChecked()) {
                            EditText vehicleNum = dialogView.findViewById(R.id.make_review_vehicle_text);
                            EditText vehicleComment = dialogView.findViewById(R.id.make_review_vehicle_comment);
                            sendReview(vehicleNum.getText().toString(), vehicleComment.getText().toString(), false);
                        }

                    }
                })
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MakeReviewDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    private void sendReview(String num, String comment, Boolean forDriver) {

        IReviewService reviewService = Retrofit.retrofit.create(IReviewService.class);
        Call<Void> reviewResponseCall;
        if (forDriver)
            reviewResponseCall = reviewService.addDriverReview(rideId, new DriverReviewRequestDTO(Integer.parseInt(num), comment));
        else
            reviewResponseCall = reviewService.addVehicleReview(rideId, new VehicleReviewRequestDTO(Integer.parseInt(num), comment));

        reviewResponseCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("TAG", "poslat review");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("TAG", "greska u slanju review", t);
            }
        });

    }


}
