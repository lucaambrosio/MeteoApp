package ch.supsi.dti.isin.meteoapp.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import ch.supsi.dti.isin.meteoapp.R;

public class NewLocationFragment extends DialogFragment{

    EditText input;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_new_location, null);
        input = view.findViewById(R.id.editText);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Aggiungi una nuova località:")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendResultBack(Activity.RESULT_OK, input.getText().toString());
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create();
    }

    private void sendResultBack(int resultCode, String name){
        if(getTargetFragment() == null)
            return;
        Intent intent = new Intent();
        intent.putExtra("name", name);
        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }
}
