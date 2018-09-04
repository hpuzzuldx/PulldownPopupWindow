package com.ldx.pulldown.popupwindow;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.ldx.pulldown.R;

public class InputDialogView extends DialogFragment {

    public SendBackListener sendBackListener;

    public interface SendBackListener {
        void sendBack(String inputText);
    }

    private ProgressDialog progressDialog;
    private String texthint;

    private Dialog dialog;
    private EditText inputDlg;

    public InputDialogView() {
    }

    @SuppressLint("ValidFragment")
    public InputDialogView(String texthint, SendBackListener sendBackListener) {
        this.texthint = texthint;
        this.sendBackListener = sendBackListener;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = new Dialog(getActivity(), R.style.xiLandingpageappnumDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        View contentview = View.inflate(getActivity(), R.layout.xilandingpage_applinum_commitdialogview, null);
        dialog.setContentView(contentview);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.alpha = 1;
        lp.dimAmount = 0.5f;
        lp.width = dip2px(getContext(),270);
        window.setAttributes(lp);
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        inputDlg = (EditText) contentview.findViewById(R.id.xilandingpage_applinum_commitpush_txt);
        inputDlg.setHint(texthint);
        final TextView tv_send = (TextView) contentview.findViewById(R.id.channel_edit_save);
        final TextView tv_cancle = (TextView) contentview.findViewById(R.id.channel_edit_cancle);
        inputDlg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    tv_send.setEnabled(true);
                    //  tv_send.setBackgroundColor(getActivity().getResources().getColor(R.color.xilandingpage_applicationnum_commitdialogview_inputtxt_sendtxtcolor));
                } else {
                    tv_send.setEnabled(false);
                    // tv_send.setBackgroundColor(getActivity().getResources().getColor(R.color.xilandingpage_applicationnum_commitdialogview_inputtxt_nosendtxtcolor));
                }

            }
        });

        tv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(inputDlg.getText().toString())) {

                    return;
                } else {
                    progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    sendBackListener.sendBack(inputDlg.getText().toString());
                }
            }
        });

        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        inputDlg.setFocusable(true);
        inputDlg.setFocusableInTouchMode(true);
        inputDlg.requestFocus();
        final Handler hanler = new Handler();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                hanler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hideSoftkeyboard();
                    }
                }, 200);
            }
        });
        return dialog;
    }

    public boolean isProgressShow() {
        try {
            if (progressDialog != null) {
                return progressDialog.isShowing();
            } else {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void hideProgressdialog() {
        try {
            if (progressDialog != null)
                progressDialog.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideSoftkeyboard() {
        try {
            ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}