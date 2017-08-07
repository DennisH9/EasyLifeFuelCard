package com.newland.base.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import com.newland.payment.common.Const;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.StringUtils;


public class FileUtils {

	/**
	 * 写文件
	 * 
	 * @param pos
	 *            写入文件位置
	 * @param bs
	 *            写入字节流
	 * @param path
	 *            文件路径
	 * @throws Exception
	 */
	public static void write(int pos, byte[] bs, String path) {

		if (new File(path).exists() && pos == 0)
			new File(path).delete();
		RandomAccessFile raff;
		try {
			raff = new RandomAccessFile(path, "rw");
			raff.seek(pos);
			raff.write(bs);
			raff.close();
		} catch (Exception e) {
			LoggerUtils.e(e.getMessage());
		}

	}

	/**
	 * 外部存储是否可用
	 * 
	 * @return
	 */
	public static boolean isExternalStorageMounted() {
		return Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState());
	}

	/**
	 * 外部SD卡读取是否正常
	 * */
	public static boolean isSdcardNormal() {
		if (!FileUtils.isExternalStorageMounted()) {
			return false;
		}
		boolean result = false;
		File dir = new File(Const.PathConst.SDCARD_PATH);
		if (!dir.exists() || !dir.isDirectory()) {
			dir.mkdirs();
		}
		File file = new File(Const.PathConst.SDCARD_PATH + "test.txt");
		BufferedWriter writer = null;
		BufferedReader reader = null;
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			writer = new BufferedWriter(new FileWriter(file));
			reader = new BufferedReader(new FileReader(file));
			writer.write("IM81");
			writer.flush();
			String text = reader.readLine().trim();
			if ("IM81".equals(text)) {
				result = true;
			}
		} catch (Exception e) {
			LoggerUtils.e("SDCard写文件发生异常");
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (Exception e) {
					LoggerUtils.e("SDCard写关闭发生异常");
				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception e) {
					LoggerUtils.e("SDCard读关闭发生异常");
				}
			}
		}
		return result;
	}

	/**
	 * 获取手机RAM可用空间大小
	 * 
	 * @param context
	 * @return
	 */
	public static long getAvailableRamSize(Context context) {
		try {
			ActivityManager am = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			ActivityManager.MemoryInfo ramInfo = new ActivityManager.MemoryInfo();
			am.getMemoryInfo(ramInfo);
			return ramInfo.availMem;
		} catch (Exception e) {
			LoggerUtils.e("获取RAM的剩余大小发生异常");
		}
		return -1;
	}

	/**
	 * 获取手机RAM空间大小
	 * 
	 * @return
	 */
	public static long getTotalRamSize() {
		String filePath = "/proc/meminfo";
		try {
			BufferedReader reader = new BufferedReader(
					new FileReader(filePath), 8192);
			String str = reader.readLine();
			reader.close();
			String[] subArr = str.split("\\s+");
			return Long.valueOf(subArr[1]).longValue() * 1024;
		} catch (Exception e) {
			LoggerUtils.e("获取RAM的总大小发生异常");
		}
		return -1;
	}

	/**
	 * 获取手机ROM可用空间大小
	 * 
	 * @return
	 */
	public static long getAvailableRomSize() {
		try {
			File path = Environment.getDataDirectory();
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long availableBlocks = stat.getAvailableBlocks();
			return availableBlocks * blockSize;
		} catch (Exception e) {
			LoggerUtils.e("获取手机ROM可用空间大小发生异常");
		}
		return -1;
	}

	/**
	 * 获取手机ROM空间大小
	 * 
	 * @return
	 */
	public static long getTotalRomSize() {
		try {
			File path = Environment.getDataDirectory();
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long totalBlocks = stat.getBlockCount();
			return totalBlocks * blockSize;
		} catch (Exception e) {
			LoggerUtils.e("获取手机ROM空间大小发生异常");
		}
		return -1;
	}

	/**
	 * 获取手机外部可用空间大小
	 * 
	 * @return
	 */
	public static long getAvailableExternalMemorySize() {
		try {
			if (isExternalStorageMounted()) {
				File path = Environment.getExternalStorageDirectory();
				StatFs stat = new StatFs(path.getPath());
				long blockSize = stat.getBlockSize();
				long availableBlocks = stat.getAvailableBlocks();
				return availableBlocks * blockSize;
			}
		} catch (Exception e) {
			LoggerUtils.e("获取手机外部可用空间大小发生异常");
		}
		return -1;
	}

	/**
	 * 获取手机外部空间大小
	 * 
	 * @return
	 */
	public static long getTotalExternalMemorySize() {
		try {
			if (isExternalStorageMounted()) {
				File path = Environment.getExternalStorageDirectory();
				StatFs stat = new StatFs(path.getPath());
				long blockSize = stat.getBlockSize();
				long totalBlocks = stat.getBlockCount();
				return totalBlocks * blockSize;
			}
		} catch (Exception e) {
			LoggerUtils.e("获取手机外部空间大小发生异常");
		}
		return -1;
	}

	/**
	 * 格式化数值
	 * 
	 * @param size
	 * @return
	 */
	public static String format(double size) {
		String suffix = null;
		if (size >= 1024) {
			suffix = "KB";
			size /= 1024;
			if (size >= 1024) {
				suffix = "MB";
				size /= 1024;
				if (size >= 1024) {
					suffix = "GB";
					size /= 1024;
				}
			}
		}
		DecimalFormat df = new DecimalFormat("#.00");
		StringBuilder resultBuffer = new StringBuilder(df.format(size));
		if (suffix != null) {
			resultBuffer.append(suffix);
		}
		return resultBuffer.toString();
	}

	/**
	 * 获取file文件大小
	 * @param file
	 * @return
	 */
	public static long getFileTotalSize(File file) {
		// 判断文件是否存在
		if (file.exists()) {
			// 如果是目录则递归计算其内容的总大小
			if (file.isDirectory()) {
				File[] children = file.listFiles();
				long size = 0;
				for (File f : children)
					size += getFileTotalSize(f);
				return size;
			} else {// 如果是文件则直接返回其大小,以“兆”为单位
				long size = (long) file.length();
				return size;
			}
		}
		return 0;
	}

	/**
	 * 创建目录
	 * @param dir
	 * @return
	 */
	public static boolean makeDir(String dir) {
		LoggerUtils.i("待创建的目录：" + dir);
		if (StringUtils.isEmpty(dir)) {
			return false;
		}
		try {
			File file = new File(dir);
			if (!file.exists()) {
				// 创建目录
				if (file.mkdirs()) {
					// 修改目录权限
					LoggerUtils.i("目录创建成功：" + dir);
					return true;
				}
			} else {
				return true;
			}
		} catch (Exception e) {
			LoggerUtils.e("创建目录发生异常");
		}
		return false;
	}

	/**
	 * 拷贝目录
	 * @param src
	 * @param dst
	 * @return
	 */
	public static boolean copyDir(File src, File dst) {
		if (src == null || !src.exists() || !src.canRead() || dst == null) {
			return false;
		}
		try {
			// 创建目录
			if (!dst.exists() && !dst.mkdirs()) {
				return false;
			}
			String dstPath = dst.getPath() + "/";
			File[] files = src.listFiles();
			if (files != null && files.length > 0) {
				//int len = files.length;
				//for (int i=len-1; i>=0;i--) {
				//	File file = files[i];
				for(File file:files) {
					if (file.isFile()) {
						File dstFile = new File(dstPath + file.getName());
						if (!copyFile(file, dstFile)) {
							return false;
						}
						dstFile = null;
					} else if (file.isDirectory()) {
						File dstFile = new File(dstPath + file.getName());
						if (!copyDir(file, dstFile)) {
							return false;
						}
						dstFile = null;
					}
					file = null;
				}
			}
		} catch (Exception e) {
			LoggerUtils.e("拷贝目录发生异常");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 拷贝文件
	 * @param src
	 * @param dst
	 * @return
	 */
	public static boolean copyFile(File src, File dst) {
		if (src == null || !src.exists() || !src.canRead() || dst == null) {
			return false;
		}
		if (dst.exists()) {
			dst.delete();
		}
		FileInputStream in = null;
		FileOutputStream out = null;
		try {
			dst.createNewFile();
			if (!dst.getParentFile().exists() && !dst.getParentFile().mkdirs()) {
				return false;
			}
			in = new FileInputStream(src);
			out = new FileOutputStream(dst);
			int len = 0;
			byte[] buf = new byte[4096];
			while ((len = in.read(buf)) != -1) {
				out.write(buf, 0, len);
				out.flush();
			}
		} catch (Exception e) {
			LoggerUtils.e("拷贝文件发生异常");
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (null != in) {
					in.close();
					in = null;
				}
				if (null != out) {
					out.flush();
					out.close();
					out = null;
				}
			} catch (Exception e) {
				LoggerUtils.e("拷贝文件关闭IO流发生异常");
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	/**
	 * 拷贝文件
	 * @param in 文件输入流
	 * @param dst
	 * @return
	 */
	public static boolean copyFileByInputStream(InputStream in, File dst) {
		if (in == null || dst == null) {
			return false;
		}
		if (dst.exists()) {
			dst.delete();
		}
		FileOutputStream out = null;
		try {
			dst.createNewFile();
			if (!dst.getParentFile().exists() && !dst.getParentFile().mkdirs()) {
				return false;
			}
			out = new FileOutputStream(dst);
			int len = 0;
			byte[] buf = new byte[4096];
			while ((len = in.read(buf)) != -1) {
				out.write(buf, 0, len);
				out.flush();
			}
		} catch (Exception e) {
			LoggerUtils.e("拷贝文件发生异常");
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (null != in) {
					in.close();
					in = null;
				}
				if (null != out) {
					out.flush();
					out.close();
					out = null;
				}
			} catch (Exception e) {
				LoggerUtils.e("拷贝文件关闭IO流发生异常");
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 清理文件夹
	 * @param dir 文件夹
	 * @param flag 是否重新生成子文件
	 * @return
	 */
	public static boolean clearDir(File dir, boolean flag) {
		if (dir == null || !dir.exists()) {
			return true;
		}
		if (!dir.isDirectory() || !dir.canRead()) {
			return false;
		}
		try {
			File[] files = dir.listFiles();
			if (files != null && files.length > 0) {
				for (File file : files) {
					if (file.isFile()) {
						if (!file.delete()) {
							return false;
						}
						if (flag && !file.createNewFile()) {
							return false;
						}
					} else if (file.isDirectory()) {
						if (!clearDir(file, flag)) {
							return false;
						}
						if (!flag && !file.delete()) {
							return false;
						}
					}
				}
			}
		} catch (Exception e) {
			LoggerUtils.e("清理目录发生异常");
			return false;
		}
		return true;
	}

	/**
	 * 以字节为单位读取文件，常用于读取二进制文件，如图片、声音、影像等文件
	 * 
	 * @param fileName
	 *            文件名
	 * @return 字节数组
	 */
	public static byte[] readFileByBytes(File file) {

		BufferedInputStream bufferedInputStream = null;
		byte[] tempBuf = new byte[100];
		int byteRead = 0;
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		try {
			bufferedInputStream = new BufferedInputStream(new FileInputStream(
					file));
			while ((byteRead = bufferedInputStream.read(tempBuf)) != -1) {
				byteArrayOutputStream.write(tempBuf, 0, byteRead);
			}
			bufferedInputStream.close();
			return byteArrayOutputStream.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 以字节为单位读取文件，常用于读取assets目录下的文件
	 * 
	 * @param fileName
	 *            文件名
	 * @return 字节数组
	 */
	public static byte[] readFileByBytes(String fileName) {

		BufferedInputStream bufferedInputStream = null;
		byte[] tempBuf = new byte[100];
		int byteRead = 0;
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		try {
			bufferedInputStream = new BufferedInputStream(
					FileUtils.class.getResourceAsStream(fileName));
			while ((byteRead = bufferedInputStream.read(tempBuf)) != -1) {
				byteArrayOutputStream.write(tempBuf, 0, byteRead);
			}
			bufferedInputStream.close();
			return byteArrayOutputStream.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 以字节为单位写文件，常用于生成二进制文件
	 * 
	 * @param file
	 *            文件
	 * @param contentBytes
	 *            要写入文件的字节数组
	 * @param append
	 *            是否以追加的方式写文件
	 */
	public static void writeFileByBytes(File file, byte[] contentBytes,
			boolean append) {
		BufferedOutputStream bufferedOutputStream = null;

		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			bufferedOutputStream = new BufferedOutputStream(
					new FileOutputStream(file, append));
			bufferedOutputStream.write(contentBytes);
			bufferedOutputStream.flush();
			bufferedOutputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	/**
	 * 以字符为单位读取文件，常用于读取文本、数字等类型的文件
	 * 
	 * @param fileName
	 *            文件名
	 * @return 字符数组
	 */
	public static char[] readFileByChars(File file) {

		BufferedReader bufferedReader = null;
		CharArrayWriter charArrayWriter = new CharArrayWriter();
		char[] tempBuf = new char[100];
		int charRead = 0;

		try {
			bufferedReader = new BufferedReader(new InputStreamReader(
					new FileInputStream(file)));
			while ((charRead = bufferedReader.read(tempBuf)) != -1) {
				charArrayWriter.write(tempBuf, 0, charRead);
			}
			bufferedReader.close();
			return charArrayWriter.toCharArray();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void writeFileByChars(File file, char[] content,
			boolean append) {

		BufferedWriter bufferedWriter = null;

		try {
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file)));
			bufferedWriter.write(content);
			bufferedWriter.flush();
			bufferedWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	/**
	 * 以行为单位读取文件，常用于读取以行为单位为格式的文件
	 * 
	 * @param fileName
	 *            文件名
	 * @return 文件内容
	 */
	public static String readFileByLines(File file) {

		// String charset = getFileCharset(file);
		String charset = "gbk";
		BufferedReader bufferedReader = null;
		StringBuilder stringBuilder = new StringBuilder();

		try {
			bufferedReader = new BufferedReader(new InputStreamReader(
					new FileInputStream(file), charset));
			String tempString = null;
			while ((tempString = bufferedReader.readLine()) != null) {
				stringBuilder.append(tempString).append("\n");
			}
			bufferedReader.close();
			return stringBuilder.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 以行为单位读取文件，常用于读取以行为单位为格式的文件
	 * 
	 * @param fileName
	 *            文件名
	 * @return 文件内容
	 * @throws IOException
	 */
	public static String readFileByLines2(File file) throws IOException {

		BufferedReader bufferedReader = null;
		StringBuilder stringBuilder = new StringBuilder();

		try {
			bufferedReader = new BufferedReader(new InputStreamReader(
					new FileInputStream(file), "utf-8"));
			String tempString = null;
			while ((tempString = bufferedReader.readLine()) != null) {
				stringBuilder.append(tempString).append("\n");
			}
			bufferedReader.close();
			return stringBuilder.toString();
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 以字符流方式写文件，常用于保存文本文件
	 * 
	 * @param fileName
	 *            文件名
	 * @param content
	 *            要保存的内容
	 */
	public static void writeFileByString(File file, String content,
			boolean append) {

		BufferedWriter bufferedWriter = null;

		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file, append)));
			bufferedWriter.write(content);
			bufferedWriter.flush();
			bufferedWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解压缩功能.sZipPathFile 需要解压文件的完整路径。sDestPath 解压后文件的路径
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	public static void upZipIncType(String sZipPathFile, String sDestPath)
			throws Exception {
		File destDir = new File(sDestPath);
		if (!destDir.exists()) {
			destDir.mkdirs();			
		}
		
		String myString = null;
		// 先指定压缩档的位置和档名，建立FileInputStream对象
		FileInputStream fins = new FileInputStream(sZipPathFile);
		// 将fins传入ZipInputStream中
		ZipInputStream zins = new ZipInputStream(fins);
		ZipEntry ze = null;
		byte ch[] = new byte[8192];
		while ((ze = zins.getNextEntry()) != null) {
			File zfile = new File(sDestPath + "/" + ze.getName());
			File fpath = new File(zfile.getParentFile().getPath());
			if (ze.isDirectory()) {
				if (!zfile.exists())
					zfile.mkdirs();
				zins.closeEntry();
			} else {
				if (!fpath.exists())
					fpath.mkdirs();
				FileOutputStream fouts = new FileOutputStream(zfile);
				int i;
				while ((i = zins.read(ch)) != -1)
					fouts.write(ch, 0, i);
				zins.closeEntry();
				fouts.close();
			}
		}
		fins.close();
		zins.close();
	}
	
	/**
	 * 解压缩功能.sZipPathFile 需要解压文件的完整路径。sDestPath 解压后文件的路径
	 * 
	 * @throws Exception
	 */
	public static void upZip(String sZipPathFile, String sDestPath)
			throws Exception {
		File destDir = new File(sDestPath);
		if (destDir.exists()) {
			destDir.delete();			
		}
		destDir.mkdirs();
		// 先指定压缩档的位置和档名，建立FileInputStream对象
		FileInputStream fins = new FileInputStream(sZipPathFile);
		// 将fins传入ZipInputStream中
		ZipInputStream zins = new ZipInputStream(fins);
		ZipEntry ze = null;
		byte ch[] = new byte[8192];
		while ((ze = zins.getNextEntry()) != null) {
			File zfile = new File(sDestPath + "/" + ze.getName());
			File fpath = new File(zfile.getParentFile().getPath());
			if (ze.isDirectory()) {
				if (!zfile.exists())
					zfile.mkdirs();
				zins.closeEntry();
			} else {
				if (!fpath.exists())
					fpath.mkdirs();
				FileOutputStream fouts = new FileOutputStream(zfile);
				int i;
				while ((i = zins.read(ch)) != -1)
					fouts.write(ch, 0, i);
				zins.closeEntry();
				fouts.close();
			}
		}
		fins.close();
		zins.close();
	}

	/**
	 * *文件重命名
	 * 
	 * @param oldFile
	 *            需要重命名的文件
	 * @param newpath
	 *            文件目录
	 * @param newName
	 *            新文件名
	 */
	public static boolean renameFile(File oldFile, String path, String newName) {
		if (!oldFile.getName().equals(newName)) {// 新的文件名和以前文件名不同时,才有必要进行重命名
			File newfile = new File(path + "/" + newName);
			if (!oldFile.exists()) {
				return false;// 重命名文件不存在
			}
			if (newfile.exists()) {// 若在该目录下已经有一个文件和新文件名相同，则不允许重命名
				newfile.delete();
			} 
			return oldFile.renameTo(newfile);

		}
		return true;
	}

	/**
	 * 搜索出path路径下指定文件,找到第一个就停止搜索
	 * 
	 * @param path
	 * @return
	 */
	public static File findFile(String path, String fileName) {
		File appFile = null;
		File file = new File(path);
		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].getName().equals(fileName)) {
				appFile = files[i];
				break;
			}
		}
		return appFile;
	}

	/**
	 * 搜索出path路径下的apk文件,找到第一个就停止搜索
	 * 
	 * @param path
	 * @return
	 */
	public static File findApkFile(String path) {
		File appFile = null;
		File file = new File(path);
		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].getName().endsWith(".apk")) {
				appFile = files[i];
				break;
			}
		}
		return appFile;
	}

	/**
	 * 搜索出path路径下的NLP文件,找到第一个就停止搜索
	 * 
	 * @param path
	 * @return
	 */
	public static File findNLPFile(String path) {
		File appFile = null;
		File file = new File(path);
		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].getName().endsWith(".NLP")) {
				appFile = files[i];
				break;
			}
		}
		return appFile;
	}

	/**
	 * 递归删除文件和文件夹
	 * @param isAll
	 * 			  是否删除根目录 
	 * @param file
	 *            要删除的根目录
	 */
	public static void deleteFile(File file, boolean isAll) {
		if (!file.exists()) {
			return;
		} else {
			if (file.isFile()) {
				file.delete();
				return;
			}

			if (file.isDirectory()) {
				File[] childFile = file.listFiles();
				if (childFile == null || childFile.length == 0) {
					if(isAll)
						file.delete();
					return;
				}
				for (File f : childFile) {
					deleteFile(f, true);
				}
				if(isAll)
					file.delete();
			}
		}
	}

	/**
	 * 搜索指定目录下的更新包
	 * 
	 * @param dir
	 *            需要搜索的文件
	 * @return
	 */
	public static List<File> getOfflinePackages(File dir) {

		List<File> files = new ArrayList<File>();
		List<File> list = new ArrayList<File>();
		if (dir.isDirectory()) {// 如果是目录
			File[] documentArr = dir.listFiles();// 取目录下的所有文件
			if (documentArr != null) {
				// 遍历目录下所有文件 执行递归
				for (File document : documentArr) {
					files.addAll(getOfflinePackages(document));
				}
			}
		} else {// 如果是文件 加入到list中
			files.add(dir);
		}
		String string = Build.TYPE;
		String type=null;
		if (string.equals("eng")) {
			type="demo";
		}else if(string.equals("user")){
			type="release";
		}

		for (File file : files) {
			String filename = file.getName();
			String[] nameItems = filename.split("_");
			if (filename.endsWith(".zip") && nameItems[0].equals("NEWLAND")
					&& nameItems[1].equals("NLIM81")
					&& nameItems[2].compareTo("06") < 0
					&& nameItems[3].equals(type)) {
				list.add(file);
			}
		}
	
		return list;
	}
	/**
	 * 字节流读取Asset文件
	 * 
	 * @param context
	 * @param fileName
	 * @return
	 */
	public static String getAssets(Context context, String fileName) {
		final String text;

		try {
			InputStream is = context.getResources().getAssets().open(fileName);
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			text = new String(buffer, "UTF-8");

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return text;
	}

	/**
	 * 检查文件夹是否存在，不存在则创建
	 * 
	 * @param path
	 * @return
	 */
	public static String createDir(String path) {
		// 如果sPath不以文件分隔符结尾，自动添加文件分隔符
		if (!path.endsWith(File.separator)) {
			path = path + File.separator;
		}
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir.toString();
	}

	/**
	 * 创建文件
	 * 
	 * @param path
	 * @param fileName
	 * @return
	 */
	public static File createFile(String path, String fileName) {
		// 如果sPath不以文件分隔符结尾，自动添加文件分隔符
		if (!path.endsWith(File.separator)) {
			path = path + File.separator;
		}
		
		//创建文件夹
		createDir(path);
		
		File file = new File(path + fileName);
		

		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}

	/**
	 * 从文件中读取对象
	 * 
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T readObject(String path, String name) {
		// 如果sPath不以文件分隔符结尾，自动添加文件分隔符
		if (!path.endsWith(File.separator)) {
			path = path + File.separator;
		}

		//创建文件夹
		createDir(path);
		
		T t = null;
		try {
			File file = createFile(path, name);
			ObjectInputStream oin = new ObjectInputStream(new FileInputStream(file));
			t = ((T) oin.readObject());
			oin.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}

	/**
	 * 将对象保存到文件
	 * 
	 * @param name
	 * @param object
	 */
	public static void writeObject(String name, Object object, String path) {
		// 如果sPath不以文件分隔符结尾，自动添加文件分隔符
		if (!path.endsWith(File.separator)) {
			path = path + File.separator;
		}
		
		ObjectOutputStream oos;
		if (object != null) {
			try {
				createFile(path,name);
				oos = new ObjectOutputStream(new FileOutputStream(path + name));
				oos.writeObject(object);
				oos.close();
				oos = null;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
