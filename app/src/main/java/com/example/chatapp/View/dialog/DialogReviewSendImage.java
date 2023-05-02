package com.example.chatapp.View.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.chatapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DialogReviewSendImage {

    private Context context;
    private Dialog dialog;
    private ProgressDialog progressDialog;
    private Uri imageUri;
    ImageView image;
    FloatingActionButton actionButton;


    public DialogReviewSendImage(Context context, Uri imageUri) {
        this.context = context;
        this.dialog = new Dialog(context);
        this.progressDialog = new ProgressDialog(context);
        this.imageUri = imageUri;
        initialize();


    }

    public void initialize() {

        dialog.requestWindowFeature(Window.FEATURE_ACTION_BAR);
        dialog.setContentView(R.layout.activity_review_send_image);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(lp);

        image = dialog.findViewById(R.id.zoomView);
        actionButton = dialog.findViewById(R.id.review_floatingButton);


    }


    public void show(OnCallBack callBack) {
        dialog.show();
        image.setImageURI(imageUri);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.onButtonSendClick();
                dialog.dismiss();
            }
        });
    }

    public interface OnCallBack {
        void onButtonSendClick();

    }
}
