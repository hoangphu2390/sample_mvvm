package com.sample_mvvm.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.sample_mvvm.R;

/**
 * Created by HP on 05/12/2017.
 */

public class DialogFactory {

    public static Dialog createConfirmDialog(Context context, String title, String leftBtn, String rightBtn) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_confirm_inbox);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        TextView tvTitle = dialog.findViewById(R.id.txtHeading);
        Button btnLeft = dialog.findViewById(R.id.btnCancel);
        Button btnRight = dialog.findViewById(R.id.btnAccept);

        tvTitle.setText(title);
        btnLeft.setText(leftBtn);
        btnRight.setText(rightBtn);
        btnLeft.setOnClickListener(view -> dialog.dismiss());
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.getAttributes().windowAnimations = R.style.dialogAnimation;
        return dialog;
    }

    public static Dialog createAcceptDialog(Context context, String title) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_accept_inbox);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        TextView tvTitle = dialog.findViewById(R.id.txtHeading);
        tvTitle.setText(title);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        return dialog;
    }

    public static Dialog createDialogError(Context context, String message) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_error);

        TextView txtError = dialog.findViewById(R.id.txtError);
        txtError.setText(message);

        dialog.findViewById(R.id.btnOkay).setOnClickListener(v -> dialog.cancel());
        dialog.show();
        return dialog;
    }
}

