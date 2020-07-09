package com.gvsoft.gofun_ad.util;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;

public class AdPathUtils {

    private static String rootDir = "ad_root";

    public static void clear(Context context) {

        String fileRoot = getFileRoot(context);
        File roots = new File(fileRoot);
        delAllFile(roots);
    }

    /**
     * 删除文件或文件夹
     *
     * @param directory
     */
    private static void delAllFile(File directory) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            // 空文件夹
            if (files.length == 0) {
                directory.delete();
                return;
            }
            // 删除子文件夹和子文件
            for (File file : files) {
                if (file.isDirectory()) {
                    delAllFile(file);
                } else {
                    file.delete();
                }
            }
        }
        // 删除文件夹本身
        directory.delete();
    }

    public static File getFilePathAndCreate(Context context, String url) {
        if (TextUtils.isEmpty(url)) return null;
        AdResourceType type = getAdTypeByUrl(url);
        if (type == null) return null;
        String name = getNameFromUrl(url);
        if (name == null) return null;
        String filePath = getFilePath(context, type);
        File fileDir = new File(filePath);
        //如果目标目录不存在，则创建
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        return new File(filePath, name);
    }

    private static String getFileRoot(Context context) {
        String cachePath;
        if ((Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) && context.getExternalCacheDir() != null) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath + File.separator + rootDir;
    }

    public static String getFilePath(Context context, AdResourceType type) {
        return getFileRoot(context) + File.separator + type.name + File.separator;
    }

    public static String getNameFromUrl(String url) {
        String name = "";
        if (!TextUtils.isEmpty(url)) {
            String[] split = url.split("/");
            if (split.length > 0) {
                name = split[split.length - 1];
            }
        }
        if (TextUtils.isEmpty(name)) {
            if (!TextUtils.isEmpty(url)) {
                name = url;
            }
        }
        return name;
    }

    public static AdResourceType getAdTypeByUrl(String url) {
//        try {
//            new URL(url);
//        } catch (Exception e) {
//            return null;
//        }
        if (!TextUtils.isEmpty(url)) {
            if (url.endsWith(".png") || url.endsWith(".PNG")
                    || url.endsWith(".jpg") || url.endsWith(".JPG")
                    || url.endsWith(".jpeg") || url.endsWith(".JPEG")) {
                return AdResourceType.AD_IMAGE;
            } else if (url.endsWith(".json") || url.endsWith(".JSON")) {
                return AdResourceType.AD_LOTTIE;
            } else if (url.endsWith(".mp4") || url.endsWith(".MP4")) {
                return AdResourceType.AD_VIDEO;
            } else {
                return null;
            }
        }
        return null;
    }

    public static boolean isFileExists(Context context, String url) {
        File file = AdPathUtils.getFilePathAndCreate(context, url);
        if (file != null && file.exists()) {
            long length = file.length();
            if (length > 0) {
                return true;
            }
        }
        return false;
    }

    public static boolean isFileExists(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            long length = file.length();
            if (length > 0) {
                return true;
            }
        }
        return false;
    }


    public static void deleteFile(Context context, String url) {
        File file = AdPathUtils.getFilePathAndCreate(context, url);
        if (file != null && file.exists()) {
            file.delete();
        }
    }
}
