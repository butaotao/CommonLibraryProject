package com.dachen.common.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtils {
	/**
	 * 根据文件绝对路径获取文件名
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFileName(String filePath) {
		if (TextUtils.isEmpty(filePath)) {
			return "";
		}
		// 有的文件可能没有扩展名
		int end = filePath.lastIndexOf(".") == -1 ? filePath.length() : filePath.lastIndexOf(".");

		return filePath.substring(filePath.lastIndexOf(File.separator) + 1, end);
	}

	public static byte[] getBytesFromFile(File f) {
		if (f == null) {
			return null;
		}
		try {
			FileInputStream stream = new FileInputStream(f);
			ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = stream.read(b)) != -1) {
				out.write(b, 0, n);
			}
			stream.close();
			out.close();
			return out.toByteArray();
		} catch (IOException e) {
		}
		return null;
	}
	/**
	 * 根据文件绝对路径获取文件名,包括扩展名
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFileNameWithEx(String filePath) {
		if (TextUtils.isEmpty(filePath)) {
			return "";
		}
		return filePath.substring(filePath.lastIndexOf(File.separator) + 1, filePath.length());
	}

	/**
	 * 判断SDCard是否存在,并可写
	 * 
	 * @return
	 */
	public static boolean checkSDCard() {
		String flag = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(flag)) {
			return true;
		}
		return false;
	}

	// 计算图片的缩放值
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	// 根据路径获得图片并压缩，返回bitmap用于显示
	public static Bitmap getSmallBitmap(String filePath) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, 480, 800);
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filePath, options);
	}

	/*
	 * 压缩图片，
	 */
	public static String compressImage(String filePath, int q) throws FileNotFoundException {

		Bitmap bm = getSmallBitmap(filePath);
		String fileName = new SimpleDateFormat("yyyy-MM-ddHHmmssSSS").format(new Date());
		File outputFile = new File(getAlbumDir(), fileName);
		FileOutputStream out = new FileOutputStream(outputFile);
		bm.compress(Bitmap.CompressFormat.JPEG, q, out);
		return outputFile.getPath();
	}

	/**
	 * 获取保存图片的目录
	 * 
	 * @return
	 */
	public static File getAlbumDir() {
		File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				getAlbumName());
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}

	/**
	 * 获取保存 隐患检查的图片文件夹名称
	 * 
	 * @return
	 */
	public static String getAlbumName() {
		return "doctor";
	}

    public static String saveFile(Bitmap bitmap) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"Night");
        if (bitmap == null)
            return "";
        OutputStream output = null;

        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File file = new File(mediaStorageDir.getPath() + File.separator+ "IMG_" + timeStamp + ".jpg");
            output = new FileOutputStream(file);
            Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
            bitmap.compress(format, 90, output);
            output.flush();

            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (output != null)
                    output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }
}
