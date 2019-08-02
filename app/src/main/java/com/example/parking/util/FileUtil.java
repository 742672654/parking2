package com.example.parking.util;



import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;



public class FileUtil {

	private static final String TAG = "FileUtil<文件工具类>";


	//TODO 获取Uri的绝对路径
	public static String getFile_Path(Uri uri){

		if (uri == null) { return null; }
		String path = null;
		try {
			path = uri.getPath();
			if (StringUtil.is_valid(path)) { return path; }
			path = uri.getEncodedPath();
			if (StringUtil.is_valid(path)) { return path; }
		} catch (Exception e) {
			Log.w(TAG, e);
		}
		return path;
	}

	//TODO 返回文件后缀<无后缀时返回原文件名>
	public static String getFile_Suffix(Object path){

		try{
			String filePath = path instanceof Uri?getFile_Path((Uri)path):path instanceof String?filePath = (String) path:null;

			String [] photoPath = filePath.split(filePath.contains("/")?"/":".");
			if (photoPath==null || photoPath.length==0){ return filePath; }

			return photoPath[photoPath.length-1];
		}catch (Exception e){
			Log.w(TAG,e);
		}
		return null;
	}

	//TODO 文件转byte[]
	//TODO uri instanceof Uri或String绝对路径
	public static byte[] getFile_Byte(Object uri){

		InputStream in = null;
		try {

			in = new FileInputStream( uri instanceof String ? (String)uri: uri instanceof Uri? getFile_Path((Uri)uri):null );
			ByteArrayOutputStream out = new ByteArrayOutputStream();

			byte[] buffer = new byte[1024 * 4];
			int n = 0;
			while ((n = in.read(buffer)) != -1) { out.write(buffer, 0, n); }

			return out.toByteArray();
		} catch (Exception e) {
			Log.w(TAG,e);
		}finally {
			try { if (in!=null)in.close(); } catch (IOException e) { Log.w(TAG,e); }
		}
		return null;
	}

	//TODO 获取SDCard的目录真实路径
	public static String getSDCardPath() {

		String result = null;
		try {
			// 判断SDCard是否存在
			boolean sdcardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);

			Log.e("存在sdcard路径：", "" + sdcardExist);
			if (sdcardExist) {
				File sdcardDir = Environment.getExternalStorageDirectory();
				result = sdcardDir.toString();
			}
		} catch (Exception e) {
			Log.w(TAG,e);
		}
		return result;
	}

	//TODO 在SD卡上创建文件
	public static File createSDFile(){

		File f = null;
		try {
			File file = new File(getSDCardPath() + "/parkings");
			if (!file.exists()) { file.mkdirs(); }

			f = new File(file.getAbsoluteFile() + "/data.txt");
			if (f.exists()) { f.createNewFile(); }

			return f;
		} catch (Exception e) {
			Log.w(TAG, e);
		}
		return f;
	}

	//TODO 写入内容到SD卡中的txt文本中 str为内容
	public static void writeSDFile(String describe, String str) {

		FileWriter fw = null;
		try {

			fw = new FileWriter(createSDFile().getAbsolutePath(), true);
			fw.write(TimeUtil.getDateTime() + "  " + describe + "--->>>" + str + "\n");
		} catch (Exception e) {

			Log.w(TAG,e);
		}finally {

			try { if (fw != null) fw.flush(); } catch (IOException e) { Log.w(TAG, e); }
			try { if (fw != null) fw.close(); } catch (IOException e) { Log.w(TAG, e); }
		}
	}


}
