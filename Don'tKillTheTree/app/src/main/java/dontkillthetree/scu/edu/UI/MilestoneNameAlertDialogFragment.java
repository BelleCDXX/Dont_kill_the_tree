package dontkillthetree.scu.edu.UI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;

/**
 * Created by jasonzhang on 5/20/16.
 */
public class MilestoneNameAlertDialogFragment extends DialogFragment{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // inflate and set the layout for the dialog
        builder.setView(inflater.inflate(R.layout.milestone_name_alertdialog, null))
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // get the new milestone name

                        // add the new milestone name into db
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public  void onClick(DialogInterface dialog, int d) {
                        MilestoneNameAlertDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
