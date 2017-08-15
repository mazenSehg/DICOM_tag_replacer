package test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Scanner;

import org.dcm4che2.data.DicomElement;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.io.DicomInputStream;
import org.dcm4che2.util.TagUtils;

/***
 * 
 * @author mazen
 * 
 */
public class extractor {

	static PrintWriter writer;
	static File current;

	/***
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
//		Scanner in = new Scanner(System.in);
//		System.out.println("Folder containing images:");
//		String input = in.nextLine();
//		File file = new File(input);
//
		writer = new PrintWriter("BATCH_6.txt", "UTF-8");
//		System.out.println("TOSTRING: "+file.toString());
//		System.out.println("PATH: "+file.getAbsolutePath());
		String qwe = "/Volumes/DUNDEE_STUDY/IMAGES/DBT Multi-Reader Study/DBT/BATCH 6_B/Cached/BATCH6_B BTO";
		
//		listFilesAndFilesSubDirectories(file.toString());
		listFilesAndFilesSubDirectories(qwe);
		writer.close();
		
		
		
		
	}

	/***
	 * 
	 * @param directoryName
	 * @throws IOException 
	 */
	public static void listFilesAndFilesSubDirectories(String directoryName) throws IOException {
		File directory = new File(directoryName);
		File[] fList = directory.listFiles();
		for (File file : fList) {
			if (file.isFile()) {

				String extension = "";
				String fileName = file.getAbsolutePath();

				int i = fileName.lastIndexOf('.');
				int p = Math.max(fileName.lastIndexOf('/'),
						fileName.lastIndexOf('\\'));

				if (i > p) {
					extension = fileName.substring(i + 1);
				}
				System.out.println("File: " + file.getAbsolutePath());
				System.out.println(extension);
				boolean isDicom = com.pixelmed.dicom.DicomFileUtilities.isDicomOrAcrNemaFile(file.getAbsolutePath());
				if(isDicom!=false){
					writer.println();
					writer.print(file.getName() + " < ");
					current = file.getAbsoluteFile();
					pull(file.getAbsolutePath());
					

				}
				
				File folder = new File(file.getAbsolutePath());

			} else if (file.isDirectory()) {
				listFilesAndFilesSubDirectories(file.getAbsolutePath());
			}
		}
	}

	/***
	 * 
	 * @param string
	 * @throws IOException 
	 */
	public static void pull(String string) throws IOException {
		DicomObject object = null;
		try {
			DicomInputStream dis = new DicomInputStream(new File(string + ""));
			object = dis.readDicomObject();
			dis.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
		extractor list = new extractor();
		list.listHeader(object);
	}

	/**
	 * 
	 * @param object
	 * @throws IOException 
	 */
	public void listHeader(DicomObject object) throws IOException {
		Iterator<DicomElement> iter = object.datasetIterator();
		while (iter.hasNext()) {
			DicomElement element = iter.next();
			int tag = element.tag();
			try {
				
				String tagName = object.nameOf(tag);
				Iterator<DicomElement> xx = object.fileMetaInfoIterator();
				String meta = object.fileMetaInfo().toString();
				String tagAddr = TagUtils.toString(tag);
				String tagVR = object.vrOf(tag).toString();
				if (tagVR.equals("SQ")) {
					if (element.hasItems()) {
//						System.out.println(tagAddr + " [" + tagVR + "] "+ tagName);
						listHeader(element.getDicomObject());
						continue;
					}
				}

				String tagValue = object.getString(tag);
//				System.out.println(tagAddr + " [" + tagVR + "] " + tagName + " [" + tagValue + "]");
				if (tagName.matches("Frame Laterality") && tagName != null) {
					writer.print(tagValue + ",");
				} else if (tagName.matches("SOP Instance UID")
						&& tagName != null) {
					writer.print(tagValue + ", ");
				} else if (tagAddr.contains("0055,1001") && tagAddr != null) {
					writer.print(tagValue + ", ");
				} else if (tagAddr.contains("0018,5101") && tagAddr != null) {
					writer.print(tagValue + ", ");
				} else if (tagAddr.contains("0020,0062") && tagAddr != null) {
					writer.print(tagValue + ", ");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	       DicomInputStream dis = new DicomInputStream(current);
	       DicomObject metaInfo = dis.readFileMetaInformation();
//	       System.out.println(metaInfo);
	       String text = metaInfo.toString(); 
	       String textStr[] = text.split("\\r\\n|\\n|\\r");
	       for(int i = 0; i<textStr.length;i++){
			   writer.print(textStr[i] + " < ");
		//	   System.out.println(textStr[i]);
	       }

	       
	}

}
