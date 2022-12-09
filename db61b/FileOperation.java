package db61b;
import java.io.File;
import java.io.IOException;
import static db61b.Utils.*;

public class FileOperation {
	public static void judge_file_exists(File file){
		if (file.exists()){
			System.out.println("Loading "+file.toString() + " ...");
		} else{
			System.out.println(file.toString() + " not exists, create it ...");
			try{
				file.createNewFile();
			} catch(IOException e){
				throw error("OS Error. Cannot create files");
			}
		}
	}
	public static void judge_dir_exists(File file){
		if (file.exists()){
			if(file.isDirectory()) {
				System.out.println("Loading snapshots ...");
			} else{
				throw error("The file whose name is \"snapshots\" exists, cannot create directory");
			}
		} else {
			System.out.println("snapshots, create it ...");
			file.mkdir();
		}
	}
}
