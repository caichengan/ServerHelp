package com.xht.android.serverhelp.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.xht.android.serverhelp.App;
import com.xht.android.serverhelp.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

public class BitmapUtils {


	/**
	 *
	 * @param mContext
	 * @param uri
	 * @param reqWidth
	 * @param reqHeight
     * @return
	 * 图片的绝对路径获取bitmap
	 *
	 */




	/* File f = new File(filePath);
	if (f.exists()) { //产生Bitmap对象，并放入mImageView中
	Bitmap bm = BitmapFactory.decodeFile(filePath);
	mImageView.setImageBitmap(bm);
} else {
		toast("文件不存在");
		}*/




	public static Bitmap decodeSampledFromUri(Context mContext, Uri uri,
			int reqWidth, int reqHeight) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		InputStream inputStream = null;
		InputStream inputStreamSampled = null;
		try {
			inputStream = mContext.getContentResolver().openInputStream(uri);
			BitmapFactory.decodeStream(inputStream, null, options);

			options.inSampleSize = calculateInSampleSize(options, reqWidth,reqHeight);
			options.inJustDecodeBounds = false;

			inputStreamSampled = mContext.getContentResolver().openInputStream(
					uri);
			Bitmap localBitmap = BitmapFactory.decodeStream(inputStreamSampled,
					null, options);
			return localBitmap;
		} catch (IOException e) {
			Log.e("BitmapUtils", "Error");
			Bitmap bitmap = BitmapFactory.decodeResource(
					mContext.getResources(), R.mipmap.p_head_fail);
			return bitmap;
		} finally {
			try {
				try {
					inputStream.close();
					inputStreamSampled.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (NullPointerException e) {
				Log.e("BitmapUtils", "Failed to close streams");
			}
		}
	}


	/**
	 * 加载网络图片
	 * @param imgFile
	 * @param mImgFile
     */
	public static void loadImgageUrl( String imgFile, final ImageView mImgFile) {


		ImageRequest request = new ImageRequest(imgFile, new Response.Listener<Bitmap>() {

			@Override
			public void onResponse(Bitmap arg0) {
			/*	ScaleAnimation scaleAnimation=new ScaleAnimation(0.3f, 1.0f ,0.3f, 1.0f);
				scaleAnimation.setRepeatMode(TRIM_MEMORY_COMPLETE);
				scaleAnimation.setDuration(500);
				mImgFile.startAnimation(scaleAnimation);*/
				mImgFile.setImageBitmap(arg0);

			}
		}, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				mImgFile.setImageDrawable(null);
				//mImgFile.setImageResource(R.mipmap.ic_action_add);
			}
		});
		//存储到队列中
		App.getInstance().getRequestQueue().add(request);
	}




	/**
	 * Try to return the absolute file path from the given Uri
	 *
	 * @param context   知道图片路径 Uri 转换为 String 路径            TODO
	 * @param uri
	 * @return the file path or null
	 */
	public static String getRealFilePath( final Context context, final Uri uri ) {
		if ( null == uri ) return null;
		final String scheme = uri.getScheme();
		String data = null;
		if ( scheme == null )
			data = uri.getPath();
		else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
			data = uri.getPath();
		} else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
			Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
			if ( null != cursor ) {
				if ( cursor.moveToFirst() ) {
					int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
					if ( index > -1 ) {
						data = cursor.getString( index );
					}
				}
				cursor.close();
			}
		}
		return data;
	}


	public static Bitmap drawTextToBitmap(Context mContext, Bitmap bitmap,
			String text, Integer offsetX, Integer offsetY, float textSize,
			Integer textColor) {
		Resources resources = mContext.getResources();
		float scale = resources.getDisplayMetrics().density;

		Bitmap.Config bitmapConfig = bitmap.getConfig();

		if (bitmapConfig == null) {
			bitmapConfig = Bitmap.Config.RGB_565;
		}

		if (!bitmap.isMutable()) {
			bitmap = bitmap.copy(bitmapConfig, true);
		}
		Canvas canvas = new Canvas(bitmap);

		Paint paint = new Paint(1);

		paint.setColor(textColor.intValue());

		textSize = (int) (textSize * scale * bitmap.getWidth() / 100.0F);

		textSize = textSize < 15.0F ? textSize : 15.0F;
		paint.setTextSize(textSize);

		paint.setShadowLayer(1.0F, 0.0F, 1.0F, -1);

		Rect bounds = new Rect();
		paint.getTextBounds(text, 0, text.length(), bounds);
		int x;
		if (offsetX == null) {
			x = (bitmap.getWidth() - bounds.width()) / 2;
		} else {
			if (offsetX.intValue() >= 0) {
				x = offsetX.intValue();
			} else {
				x = bitmap.getWidth() - bounds.width() - offsetX.intValue();
			}
		}
		int y;
		if (offsetY == null) {
			y = (bitmap.getHeight() - bounds.height()) / 2;
		} else {
			if (offsetY.intValue() >= 0) {
				y = offsetY.intValue();
			} else {
				y = bitmap.getHeight() - bounds.height() + offsetY.intValue();
			}
		}

		canvas.drawText(text, x, y, paint);

		return bitmap;
	}

	public static Uri getUri(Context mContext, int resource_id) {
		Uri uri = Uri.parse("android.resource://" + mContext.getPackageName()
				+ "/" + resource_id);
		return uri;
	}


	/**
	 * 对图片进行压缩命名存储到指定的路径targetPath
	 * @param filePath
	 * @param targetPath
	 * @param quality
     * @return
     */
	public static String compressImage(String filePath, String targetPath, int quality)  {
		Bitmap bm = getSmallBitmap(filePath);//获取一定尺寸的图片
		int degree = readPictureDegree(filePath);//获取相片拍摄角度
		if(degree!=0){//旋转照片角度，防止头像横着显示
			bm=rotateBitmap(bm,degree);
		}
		File outputFile=new File(targetPath);
		try {
			if (!outputFile.exists()) {
				outputFile.getParentFile().mkdirs();
				//outputFile.createNewFile();
			}else{
				outputFile.delete();
			}
			FileOutputStream out = new FileOutputStream(outputFile);
			bm.compress(Bitmap.CompressFormat.JPEG, quality, out);
		}catch (Exception e){}
		return outputFile.getPath();
	}

	/**
	 * 根据路径获得图片信息并按比例压缩，返回bitmap
	 */
	public static Bitmap getSmallBitmap(String filePath) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;//只解析图片边沿，获取宽高
		BitmapFactory.decodeFile(filePath, options);
		// 计算缩放比
		options.inSampleSize = calculateInSampleSize(options, 480, 800);
		// 完整解析图片返回bitmap
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filePath, options);
	}


	/**
	 * 调用此方法自动计算指定文件或指定文件夹的大小
	 * @param filePath 文件路径
	 * @return 计算好的带B、KB、MB、GB的字符串
	 */
	public static String getAutoFileOrFilesSize(String filePath){
		File file=new File(filePath);
		long blockSize=0;
		try {
			if(file.isDirectory()){
				blockSize = getFileSize(file);
			}else{
				blockSize = getFileSize(file);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("获取文件大小","获取失败!");
		}
		return FormetFileSize(blockSize);
	}

	/*
	获取文件大小
	 */
	public static double getFileOrFilesSize(String filePath,int sizeType){
		File file=new File(filePath);
		long blockSize=0;
		try {
			if(file.isDirectory()){
				blockSize = getFileSize(file);
			}else{
				blockSize = getFileSize(file);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("获取文件大小","获取失败!");
		}
		return FormetFileSize(blockSize, sizeType);
	}


	/**
	 * 获取指定文件大小
	 * @param
	 * @return
	 * @throws Exception
	 */
	private static long getFileSize(File file) throws Exception
	{
		long size = 0;
		if (file.exists()){
			FileInputStream fis = null;
			fis = new FileInputStream(file);
			size = fis.available();
		}
		else{
			file.createNewFile();
			Log.e("获取文件大小","文件不存在!");
		}
		return size;
	}

	/**
	 * 转换文件大小,指定转换的类型
	 * @param fileS
	 * @param sizeType
	 * @return
	 */
	private static double FormetFileSize(long fileS,int sizeType)
	{
		DecimalFormat df = new DecimalFormat("#.00");
		double fileSizeLong = 0;
		switch (sizeType) {
			case 1:
				fileSizeLong=Double.valueOf(df.format((double) fileS));
				break;
			case 2:
				fileSizeLong=Double.valueOf(df.format((double) fileS / 1024));
				break;
			case 3:
				fileSizeLong=Double.valueOf(df.format((double) fileS / 1048576));
				break;
			case 4:
				fileSizeLong=Double.valueOf(df.format((double) fileS / 1073741824));
				break;
			default:
				break;
		}
		return fileSizeLong;
	}

	/**
	 * 转换文件大小
	 * @param fileS
	 * @return
	 */
	private static String FormetFileSize(long fileS)
	{
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		String wrongSize="0B";
		if(fileS==0){
			return wrongSize;
		}
		if (fileS < 1024){
			fileSizeString = df.format((double) fileS) + "B";
		}
		else if (fileS < 1048576){
			fileSizeString = df.format((double) fileS / 1024) + "KB";
		}
		else if (fileS < 1073741824){
			fileSizeString = df.format((double) fileS / 1048576) + "MB";
		}
		else{
			fileSizeString = df.format((double) fileS / 1073741824) + "GB";
		}
		return fileSizeString;
	}

	/**
	 * 获取照片角度
	 * @param path
	 * @return
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
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

	/**
	 * 旋转照片
	 * @param bitmap
	 * @param degress
	 * @return
	 */
	public static Bitmap rotateBitmap(Bitmap bitmap,int degress) {
		if (bitmap != null) {
			Matrix m = new Matrix();
			m.postRotate(degress);
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
					bitmap.getHeight(), m, true);
			return bitmap;
		}
		return bitmap;
	}
	public static int calculateInSampleSize(BitmapFactory.Options options,
											int reqWidth, int reqHeight) {
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

	private static Bitmap scaleImage(Context mContext, Bitmap bitmap,
			int reqWidth, int reqHeight) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int boundingX = dpToPx(mContext, reqWidth);
		int boundingY = dpToPx(mContext, reqHeight);

		float xScale = boundingX / width;
		float yScale = boundingY / height;
		float scale = xScale >= yScale ? xScale : yScale;

		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);

		Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);

		return scaledBitmap;
	}

	public static Bitmap rotateImage(Bitmap bitmap, String filePath) {
		Bitmap resultBitmap = bitmap;
		try {
			ExifInterface exifInterface = new ExifInterface(filePath);
			int orientation = exifInterface.getAttributeInt("Orientation", 1);

			Matrix matrix = new Matrix();

			if (orientation == 6)
				matrix.postRotate(6.0F);
			else if (orientation == 3)
				matrix.postRotate(3.0F);
			else if (orientation == 8) {
				matrix.postRotate(8.0F);
			}

			resultBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
					bitmap.getHeight(), matrix, true);
		} catch (Exception exception) {
			Log.d("AndroidTouchGallery", "Could not rotate the image");
		}
		return resultBitmap;
	}

	public static InputStream getBitmapInputStream(Bitmap bitmap) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
		byte[] bitmapdata = bos.toByteArray();
		ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);
		return bs;
	}

	private static int dpToPx(Context mContext, int dp) {
		float density = mContext.getResources().getDisplayMetrics().density;
		return Math.round(dp * density);
	}
}
