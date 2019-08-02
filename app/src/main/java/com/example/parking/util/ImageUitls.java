package com.example.parking.util;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;
import android.widget.ImageView;

import com.example.parking.activety.LoginActivity;
import com.example.parking.activety.MainBaseActivity;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class ImageUitls {



	private static final String TAG = "ImageUtil<图片工具类>";

	//设置网络图片
	public static void setImageHttpURL(final Activity activity, final ImageView user_photo, final String path) {


	    Log.i(TAG,"加载图片:"+path);
		new Thread() {
			public void run() {


		InputStream inputStream = null;
		try {
			//把传过来的路径转成URL
			URL url = new URL(path);
			//获取连接
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			//使用GET方法访问网络
			connection.setRequestMethod("GET");
			//超时时间为10秒
			connection.setConnectTimeout(10000);
			//获取返回码
			int code = connection.getResponseCode();

			inputStream = connection.getInputStream();
			//使用工厂把网络的输入流生产Bitmap
			final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {

					user_photo.setImageBitmap(bitmap);
				}
			});
		} catch (Exception e) {

			Log.w(TAG, e);
		} finally {

			if (inputStream != null)try{ inputStream.close(); } catch (IOException e) { Log.w(TAG, e); }
		}

			}
		}.start();
	}

	public static int getSampleSize(BitmapFactory.Options options){
		return computeSampleSize(options, 1000, 1000 * 1000);
	}

	/**
	 * 图片压缩算法
	 * @param options Bitmap.Options
	 * @param minSideLength 最小显示区
	 * @param maxNumOfPixels 你想要的宽度 * 你想要的高度
	 * @return
	 */
	public static int computeSampleSize(BitmapFactory.Options options,
                                        int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	public static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));
		if (upperBound < lowerBound) {
			return lowerBound;
		}
		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	/**
	 * 读取图片属性：旋转的角度
	 * @param path 图片绝对路径
	 * @return degree旋转的角度
	 */
	public static int readPictureDegree(String path) {
		int degree  = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
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

	/**
	 * 旋转图片 
	 * @param angle 
	 * @param bitmap 
	 * @return Bitmap 
	 */ 
	public static Bitmap rotaingImageView(int angle , Bitmap bitmap) {
		//旋转图片 动作   
		Matrix matrix = new Matrix();;
		matrix.postRotate(angle);  
		System.out.println("angle2=" + angle);
		// 创建新的图片   
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);  
		return resizedBitmap;  
	}

	/**
	 * 将Bitmap转换成InputStream 
	 * @param bm
	 * @param isCompress 是否压缩
	 * @return
	 */
	@SuppressLint("NewApi")
	public static InputStream bitmapToInputStream(Bitmap bm, boolean isCompress){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if(isCompress){
			bm.compress(Bitmap.CompressFormat.WEBP, 10, baos);
		}else{
			bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		}
		InputStream is = new ByteArrayInputStream(baos.toByteArray());
		return is;
	}

	/**
	 * 将图片保存到文件
	 * @param bitmap
	 * @param pPath
	 * @return
	 */
	@SuppressLint("NewApi")
	public static boolean saveFrameToPath(Bitmap bitmap, String pPath) {
		int BUFFER_SIZE = 1024 * 8;
		try {
			File file = new File(pPath);
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			final BufferedOutputStream bos = new BufferedOutputStream(fos, BUFFER_SIZE);
			bitmap.compress(CompressFormat.JPEG, 90, bos);
			bos.flush();
			bos.close();
			fos.close();
			Log.e("ImageUitls", "执行保存图片文件");
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void deleteImageFile(String pPath){
		if(pPath!=null){
			File file = new File(pPath);
			if (file!=null&&file.exists()){
				if (file.isFile()){
					file.delete();
				}
			}
		}
	}




	//图片转字节数组
	public static byte []  bitmapByte(Bitmap bmp){
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte [] bitmapByte =baos.toByteArray();
		return bitmapByte;
	}

	//数组转图片
	public static Bitmap byteBitmap(byte [] bitmapByte){
		InputStream is = new ByteArrayInputStream(bitmapByte);
		Bitmap srcbmp = BitmapFactory.decodeStream(is);
		return srcbmp;

	}

	

	//是否压缩图片
	public static InputStream getBitmapInputStream(String netType, Bitmap resultBitmap) {
		InputStream bitmapToInputStream = null;
		if(netType.equals("0")){
			bitmapToInputStream = ImageUitls.bitmapToInputStream(resultBitmap,true);
		}else if(netType.equals("1")){
			bitmapToInputStream = ImageUitls.bitmapToInputStream(resultBitmap,false);
		}
		return bitmapToInputStream;
	}



}
