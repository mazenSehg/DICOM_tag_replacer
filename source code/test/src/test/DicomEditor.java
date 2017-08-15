package test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import org.dcm4che2.data.DicomElement;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.io.DicomInputStream;
import org.dcm4che2.util.TagUtils;

public class DicomEditor {
	static PrintWriter writer;

	public void listHeader(DicomObject object) {
		Iterator<DicomElement> iter = object.datasetIterator();
		while (iter.hasNext()) {
			DicomElement element = iter.next();
			int tag = element.tag();
			try {
				String tagName = object.nameOf(tag);
				String tagAddr = TagUtils.toString(tag);
				String tagVR = object.vrOf(tag).toString();
				if (tagVR.equals("SQ")) {
					if (element.hasItems()) {
						System.out.println(tagAddr + " [" + tagVR + "] "
								+ tagName);
						listHeader(element.getDicomObject());
						continue;
					}
				}
				String tagValue = object.getString(tag);
				if (tagName.matches("Instance Number")) {
					System.out.println("VALUE: " + tagValue);

					System.out.println(tagAddr + " [" + tagVR + "] " + tagName
							+ " [" + tagValue + "]");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws IOException {

		writer = new PrintWriter("text.txt", "UTF-8");
		String qwe = "/Volumes/DUNDEE_STUDY/IMAGES/DBT Multi-Reader Study/DBT/BATCH 6_B/BATCH6_B BTO/";
		listFilesAndFilesSubDirectories(qwe);
		writer.close();

	}

	public static void listFilesAndFilesSubDirectories(String directoryName) {
		File directory = new File(directoryName);
		File[] fList = directory.listFiles();
		for (File file : fList) {
			if (file.isFile()) {
				System.out.println("File: " + file.getAbsolutePath());
				writer.println(file.getName());
				// pull(file.getAbsolutePath());
				File folder = new File(file.getAbsolutePath());
			} else if (file.isDirectory()) {
				listFilesAndFilesSubDirectories(file.getAbsolutePath());
			}
		}
	}

	public static void pull(String string) {
		DicomObject object = null;
		try {
			DicomInputStream dis = new DicomInputStream(new File(string + ""));
			object = dis.readDicomObject();
			dis.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
		DicomEditor list = new DicomEditor();
		list.listHeader(object);
	}

}