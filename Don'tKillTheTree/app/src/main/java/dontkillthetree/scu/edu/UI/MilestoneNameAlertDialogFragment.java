package dontkillthetree.scu.edu.UI;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.EditText;

/**
 * Created by jasonzhang on 5/20/16.
 */
public class MilestoneNameAlertDialogFragment extends DialogFragment{
    // fail to deliver the events back to the host activity
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        // get the layout inflater
//        LayoutInflater inflater = getActivity().getLayoutInflater();
//
//        // inflate and set the layout for the dialog
//        builder.setView(inflater.inflate(R.layout.milestone_name_alertdialog, null))
//                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int id) {
//                        // get the new milestone name
//                        EditText newMilestoneName = (EditText)
//
//                        // add the new milestone name into db
//                    }
//                })
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public  void onClick(DialogInterface dialog, int d) {
//                        MilestoneNameAlertDialogFragment.this.getDialog().cancel();
//                    }
//                });
//        return builder.create();
//    }
    public interface MilestoneNameAlertDialogListener {
        /* The activity that creates an instance of this dialog fragment must
         * implement this interface in order to receive event callbacks.
         * Each method passes the DialogFragment in case the host needs to query it. */
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    MilestoneNameAlertDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the MilestoneNameAlertDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (MilestoneNameAlertDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }
}
