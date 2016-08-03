package com.dachen.common.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import com.dachen.common.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.io.IOException;

/**
 * 
 * @author gaozhuo
 * @since : 2015年9月23日
 *
 */
public class BitmapUtils {

	/**
	 * 读取图片属性：旋转的角度
	 * 
	 * @param path
	 *            图片绝对路径
	 * @return degree旋转的角度
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/*
	 * 旋转图片
	 * 
	 * @param angle
	 * 
	 * @param bitmap
	 * 
	 * @return Bitmap
	 */
	public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}

	public static int[] getImageWH(Bitmap srcImg) {
		if (srcImg == null) {
			return null;
		}
		return getImageWH(srcImg.getWidth(), srcImg.getHeight());
	}

	public static int[] getImageWH(String fileName) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(fileName, options);
		return getImageWH(options.outWidth, options.outHeight);
	}
	public static int[] getImageSize(String fileName){
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(fileName, options);
		return new int[] { options.outWidth, options.outHeight };
	}

	/**
	 * 计算图片的最终WH
	 */
	public static int[] getImageWH(int srcWidth, int srcHeight) {

		/**
		 * 原尺寸都小于100，不压缩
		 */
		if (srcWidth <= 100 && srcHeight <= 100) {
			return new int[] { srcWidth, srcHeight };
		}

		int x = 0;
		int y = 0;
		int destWidth = 0;
		int destHeight = 0;
		/**
		 * 压缩比例
		 */
		double scaleF = Math.max(srcHeight, srcWidth) / 100.0;
		destWidth = (int) (srcWidth / scaleF);
		destHeight = (int) (srcHeight / scaleF);
		int min = Math.min(destWidth, destHeight);
		if (min < 50) {
			destWidth = (int) (destWidth * (50.0 / min));
			destHeight = (int) (destHeight * (50.0 / min));
		}

		int finalWidth = Math.min(destWidth, 100);
		int finalHeight = Math.min(destHeight, 100);

		boolean cut = false;

		if (destWidth > 100) {
			// 裁剪
			x = (destWidth - 100) / 2;
			cut = true;
		}
		if (destHeight > 100) {
			// 裁剪
			y = (destHeight - 100) / 2;
			cut = true;
		}

		return new int[] { finalWidth, finalHeight };
	}

	/**
	 * 计算采样率
	 * 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			} else {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			}
		}
		return inSampleSize;
	}

	/**
	 * 对本地图片文件进行采样
	 * 
	 * @param filename
	 * @param reqWidth
	 *            期望得到的width
	 * @param reqHeight
	 *            期望得到的height
	 * @return
	 */
	public static Bitmap decodeSampledBitmapFromFile(String filename, int reqWidth, int reqHeight) {

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filename, options);

		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filename, options);
	}

	public static DisplayImageOptions getHeadOptions() {
		return getOptions(R.drawable.ic_default_head);
	}

	public static DisplayImageOptions getOptions() {
		return getOptions(R.drawable.no_images);
	}

	public static DisplayImageOptions getOptions(int resId) {
		DisplayImageOptions options = new DisplayImageOptions.Builder().showImageForEmptyUri(resId)
				.showImageOnFail(resId).displayer(new FadeInBitmapDisplayer(300)).cacheInMemory(true).cacheOnDisk(true)
				.build();
		return options;
	}

	/**
	 * 得到图片url
	 * 
	 * @param resId
	 * @return
	 */
	public static String getImageUrl(int resId) {
		StringBuilder ss = new StringBuilder("drawable://");
		ss.append(resId);
		return ss.toString();
	}

	/**
	 * 得到图片url
	 * @return
	 */
	public static String getFileUrl(String filepath) {
		StringBuilder ss = new StringBuilder("file:///");
		ss.append(filepath);
		return ss.toString();
	}
	public static int[] getSuitableSize(int width, int height) {
		if (width < 2160 && height < 3840) {
			return new int[]{width, height};
		}

		int outWidth;
		int outHeight;
		float widthRatio = (float) width / 2160;
		float heightRatio = (float) height / 3840;
		if (widthRatio > heightRatio) {
			outWidth = (int) (width / widthRatio);
			outHeight = (int) (height / widthRatio);
		} else {
			outWidth = (int) (width / heightRatio);
			outHeight = (int) (height / heightRatio);
		}
		return new int[]{outWidth, outHeight};
	}
}
