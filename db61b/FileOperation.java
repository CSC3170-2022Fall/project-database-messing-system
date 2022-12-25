package db61b;

import java.io.File;
import java.io.IOException;
import static db61b.Utils.*;

public class FileOperation {
	public static void judge_file_exists(File file) {
		if (file.exists()) {
			System.out.println("Note: loading " + file.toString() + " ...");
		} else {
			System.out.println("FileNotFound: file \"" + file.toString() + "\" does not exist, creating it ...");
			try {
				file.createNewFile();
			} catch (IOException e) {
				throw error("OS Error: creating file failed.");
			}
		}
	}

	public static void judge_dir_exists(File file) {
		if (file.exists()) {
			if (file.isDirectory()) {
				System.out.println("Note: loading " + file.toString() + " ...");
			} else {
				throw error("FileNotFound: file \"%s\" already exists, cannot create directory.", file.toString());
			}
		} else {
			System.out.println("FileNotFound: file \"" + file.toString() + "\" does not exist, creating it ...");
			file.mkdir();
		}
	}
}
