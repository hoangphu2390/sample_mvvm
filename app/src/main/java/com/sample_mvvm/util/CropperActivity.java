package com.sample_mvvm.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.opengl.GLES10;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.edmodo.cropper.CropImageView;
import com.sample_mvvm.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.microedition.khronos.opengles.GL10;


public class CropperActivity extends Activity {

    CropImageView cropImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String path = getIntent().getExtras().getString("path");
        setContentView(R.layout.activity_cropper);
        cropImageView = (CropImageView) findViewById(R.id.cropper_image);

        int size = Utils.getScreenWidth(this);
        cropImageView.setFixedAspectRatio(true);
        cropImageView.setAspectRatio(size, size);

        //check texture size on device
        int[] maxTextureSize = new int[1];
        GLES10.glGetIntegerv(GL10.GL_MAX_TEXTURE_SIZE, maxTextureSize, 0);
        int currentTextureSize = maxTextureSize[0];

        if (!TextUtils.isEmpty(path)) {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            int height = bitmap.getHeight();
            int width = bitmap.getWidth();
            if (currentTextureSize == 0) {
                if (height > 4096 || width > 4096) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 2;
                    Bitmap newBitmap = BitmapFactory.decodeFile(path, options);
                    cropImageView.setImageBitmap(newBitmap);
                } else {
                    cropImageView.setImageBitmap(bitmap);
                }
            } else {
                if (height > currentTextureSize || width > currentTextureSize) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 2;
                    Bitmap newBitmap = BitmapFactory.decodeFile(path, options);
                    cropImageView.setImageBitmap(newBitmap);
                } else {
                    cropImageView.setImageBitmap(bitmap);
                }
            }
        } else {
            Toast.makeText(CropperActivity.this, "Unable to load the image", Toast.LENGTH_SHORT).show();
        }

        findViewById(R.id.cropper_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        findViewById(R.id.cropper_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveButtonClick();
            }
        });

        findViewById(R.id.cropper_rotate_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropImageView.rotateImage(90);
            }
        });

        findViewById(R.id.cropper_rotate_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropImageView.rotateImage(-90);
            }
        });
    }

    private final Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

            cropImageView.setImageBitmap(bitmap);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            Dialog dialog = DialogFactory.createDialogError(CropperActivity.this, "Unable to load cropper");
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    setResult(RESULT_CANCELED);
                    finish();
                }
            });
            dialog.show();
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
        }
    };

    public void onSaveButtonClick() {
        FileCache fileCache = new FileCache(this);
        File imageFile = new File(fileCache.getCacheDir().getAbsolutePath()
                + "/"
                + System.currentTimeMillis() + ".jpg");

        Bitmap bitmap = cropImageView.getCroppedImage();
        try {
            OutputStream outputStream = new FileOutputStream(imageFile.getAbsolutePath());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
            Intent result = new Intent();
            result.putExtra("path", imageFile.getAbsolutePath());
            setResult(RESULT_OK, result);
            finish();
        } catch (FileNotFoundException e) {
            setResult(RESULT_CANCELED);
            finish();
            e.printStackTrace();
        }
    }
}
