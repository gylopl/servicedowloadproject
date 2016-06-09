package makdroid.servicesproject.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import makdroid.servicesproject.R;


public class AlertDialogBuilder {

    public static AlertDialog createErrorDialog(String message, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.setCancelable(false);
        builder.setTitle("Error");
        builder.setMessage(message);
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        return builder.create();
    }
}
