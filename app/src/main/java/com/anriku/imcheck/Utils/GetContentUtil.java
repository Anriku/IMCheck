package com.anriku.imcheck.Utils;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Anriku on 2017/8/20.
 */

public class GetContentUtil {

    public static final int CHOOSE_FROM_ALBUM = 0;
    public static final int CHOOSE_FILE = 1;

    public static void getContent(Context context,boolean isGetImage) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        if (isGetImage){
            intent.setType("image/*");
            ((Activity) context).startActivityForResult(intent, CHOOSE_FROM_ALBUM);
        }else {
            intent.setType("*/*");
            ((Activity) context).startActivityForResult(intent, CHOOSE_FILE);
        }

    }


    public static String getPath(Intent intent,Context context,int resultCode){
        String imagePath = null;
        if (resultCode == RESULT_OK){
            if (Build.VERSION.SDK_INT >= 19){
                imagePath = handleOnKitKat(intent,context);
            }else {
                imagePath = handleBeforeKitKat(context,intent);
            }
        }
        return imagePath;
    }

    //对从相册中取出的图片进行处理来获取图片的路径，4.4之后由于Uri进行了封装要进行很多的判断
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static String handleOnKitKat(Intent intent,Context context) {
        String imagePath = null;
        Uri uri = intent.getData();
        if (DocumentsContract.isDocumentUri(context, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getContentPath(context,MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads")
                        , Long.valueOf(docId));
                imagePath = getContentPath(context,contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            imagePath = getContentPath(context,uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            imagePath = uri.getPath();
        }
        return imagePath;
    }

    //4.4之前全部都当成content类型的Uri来进行处理
    private static String handleBeforeKitKat(Context context,Intent intent) {
        Uri uri = intent.getData();
        String imagePath = null;
        imagePath = getContentPath(context,uri, null);
        return imagePath;
    }


    private static String getContentPath(Context context, Uri imageUri, String selection) {
        String path = null;
        Cursor cursor = context.getContentResolver().query(imageUri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
}
