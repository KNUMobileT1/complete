package com.bmh.trackchild.UI;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.bmh.trackchild.R;
import com.bmh.trackchild.Tools.StaticValues;

public class ConfirmDialogs extends DialogFragment {


    public static ConfirmDialogs newInstance(String message, int dialogType) {
        ConfirmDialogs confirmDialogs = new ConfirmDialogs();
        Bundle args = new Bundle();
        args.putString("message", message);
        args.putInt("dialogType", dialogType);
        confirmDialogs.setArguments(args);
        return confirmDialogs;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof ConfirmDialogInterface)) {
            throw new ClassCastException(activity.toString() + " must implement ConfirmDialogInterface");
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog=null;
        String message = getArguments().getString("message", "");
        int dialogType = getArguments().getInt("dialogType", 0);
        if(dialogType== StaticValues.DIALOG_TYPE_SYSTEM_ALERT){
            dialog= getSystemAlert(message);
        }
        else if(dialogType== StaticValues.DIALOG_TYPE_YES_NO){
            dialog= getYesNoDialog(message);
        }
        else if(dialogType== StaticValues.DIALOG_TYPE_OK){
            dialog=getOkDialog(message);
        }
        return dialog;

    }

    private Dialog getSystemAlert(String message) {
        Dialog confirmDialog = new Dialog(getActivity());
        confirmDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        confirmDialog.setContentView(R.layout.confirmation_dialog);
        confirmDialog.show();
        ((TextView) confirmDialog.findViewById(R.id.txtTitle)).setText(message);
        confirmDialog.findViewById(R.id.btnYes).setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                // Send the positive button event back to the host activity
                ((ConfirmDialogInterface) getActivity()).onDialogPositiveClick(ConfirmDialogs.this);
            }
        });
        confirmDialog.findViewById(R.id.btnNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Send the negative button event back to the host activity
                ((ConfirmDialogInterface) getActivity()).onDialogNegativeClick(ConfirmDialogs.this);
            }
        });

        confirmDialog.setCancelable(false);
        confirmDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        confirmDialog.getWindow().getAttributes().gravity = Gravity.CENTER;

        return confirmDialog;
    }
    private Dialog getOkDialog(String message) {
        Dialog confirmDialog = new Dialog(getActivity());
        confirmDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        confirmDialog.setContentView(R.layout.message);
        confirmDialog.show();
        ((TextView) confirmDialog.findViewById(R.id.txtTitle)).setText(message);
        confirmDialog.findViewById(R.id.btnOk).setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                // Send the positive button event back to the host activity
                ((ConfirmDialogInterface) getActivity()).onDialogPositiveClick(ConfirmDialogs.this);
            }
        });

        return confirmDialog;
    }

    private Dialog getYesNoDialog(String message) {
        Dialog confirmDialog = new Dialog(getActivity());
        confirmDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        confirmDialog.setContentView(R.layout.confirmation_dialog);
        confirmDialog.show();
        ((TextView) confirmDialog.findViewById(R.id.txtTitle)).setText(message);
        confirmDialog.findViewById(R.id.btnYes).setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                // Send the positive button event back to the host activity
                ((ConfirmDialogInterface) getActivity()).onDialogPositiveClick(ConfirmDialogs.this);
            }
        });
        confirmDialog.findViewById(R.id.btnNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Send the negative button event back to the host activity
                ((ConfirmDialogInterface) getActivity()).onDialogNegativeClick(ConfirmDialogs.this);
            }
        });
        return confirmDialog;
    }

}
