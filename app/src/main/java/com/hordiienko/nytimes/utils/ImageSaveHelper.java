package com.hordiienko.nytimes.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class ImageSaveHelper {
    private static final String DIR_NAME = "article_images";

    public static String saveImage(Context context, ImageView thumb) {
        String fileName = UUID.randomUUID().toString().replace("-", "");

        File dir = context.getDir(DIR_NAME, Context.MODE_PRIVATE);
        File file = new File(dir, fileName);

        BitmapDrawable bitmapDrawable = (BitmapDrawable) thumb.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();

        try {
            FileOutputStream outputStream = new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

            outputStream.close();

            return file.getPath();
        } catch (IOException e) {
            return null;
        }
    }

    public static boolean deleteImage(String image) {
        return new File(image).delete();
    }
}
