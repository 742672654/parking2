package com.example.parking.util;



import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;


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

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
				StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
				StrictMode.setVmPolicy(builder.build());
			}

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

	//TODO http请求下载文件保存到本地
	public static String downloadFile1( String url, String path, String filename ) {


		if ( !StringUtil.is_valid(url) || !StringUtil.is_valid(path) || !StringUtil.is_valid(filename) ){
			return null;
		}

		InputStream is = null;
		URLConnection conn = null;
		try{
			//下载路径，如果路径无效了，可换成你的下载路径
//			String url = "http://c.qijingonline.com/test.mkv";
//			String path = Environment.getExternalStorageDirectory().getAbsolutePath();

			String suffix = getSuffix(url);

			final long startTime = System.currentTimeMillis();
			Log.i(TAG+"文件下载","startTime="+startTime);

			//获取文件名
			URL myURL = new URL(url);
			conn = myURL.openConnection();
			is = conn.getInputStream();
			int fileSize = conn.getContentLength();//根据响应获取文件大小
			if (fileSize <= 0) throw new RuntimeException("无法获知文件大小 ");
			if (is == null) throw new RuntimeException("stream is null");
			File file1 = new File(path);
			if(!file1.exists()){
				file1.mkdirs();
			}
			//把数据存入路径+文件名
			FileOutputStream fos = new FileOutputStream(path+"/"+filename+"."+suffix);
			byte buf[] = new byte[1024];
			int downLoadFileSize = 0;
			do{
				//循环读取
				int numread = is.read(buf);
				if (numread == -1)
				{
					break;
				}
				fos.write(buf, 0, numread);
				downLoadFileSize += numread;
				//更新进度条
			} while (true);

			Log.i("DOWNLOAD","totalTime="+ (System.currentTimeMillis() - startTime));

			return path+"/"+filename+"."+suffix;
		} catch (Exception ex) {
			Log.w(TAG+"文件下载",  ex);
		}finally {
			try {
				is.close();
			} catch (Exception e) {
				Log.w(TAG+"文件下载",  e);
			}
			try {
				conn.connect();
			} catch (Exception e) {
				Log.w(TAG+"文件下载",  e);
			}
		}
		return null;
	}


	//TODO 取http连接的文件后缀
	public static String getSuffix(String url){

		Log.i(TAG,"取http连接的文件后缀="+url);

		String suffix = "";
		if (url.contains("?")){
			url = url.split("\\?")[0];
		}

		if (url.contains(".")){
			String[] urlsplit = url.split("\\.");

			suffix = urlsplit[urlsplit.length-1];
		}

		return suffix;
	}

}
