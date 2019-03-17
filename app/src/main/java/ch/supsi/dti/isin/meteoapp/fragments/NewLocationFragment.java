package ch.supsi.dti.isin.meteoapp.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import ch.supsi.dti.isin.meteoapp.R;

public class NewLocationFragment extends DialogFragment{
    View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_new_location, null);

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Add new Location")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create();
    }

    private void sendResult(int resultCode, String name){
        if(getTargetFragment() == null)
            return;
        Intent intent = new Intent();
        intent.putExtra("name","ciaooo");
        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }
}
