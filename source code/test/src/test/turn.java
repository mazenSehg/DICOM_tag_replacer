package test;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import org.dcm4che2.data.BasicDicomObject;
import org.dcm4che2.data.DicomElement;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.Tag;
import org.dcm4che2.data.VR;
import org.dcm4che2.io.DicomInputStream;
import org.dcm4che2.io.DicomOutputStream;
import org.dcm4che2.util.TagUtils;

public class turn {

	static String input="/Volumes/DUNDEE_STUDY/IMAGES/DBT Multi-Reader Study/DBT/BATCH 2_B/Original/DBT_1044_27_01_2014_CT";
	static String output="/Volumes/DUNDEE_STUDY/IMAGES/DBT Multi-Reader Study/DBT/BATCH 2_B/Original/bob";
	private String tagValue;
	
	public static void main(String[] args) throws IOException {
		listFilesAndFilesSubDirectories(input);

	}
	
	
	
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
					   turn list = new turn();
					   list.updateTags(file.getAbsolutePath());
				}
				
				File folder = new File(file.getAbsolutePath());

			} else if (file.isDirectory()) {
				listFilesAndFilesSubDirectories(file.getAbsolutePath());
			}
		}
	}
	
	


	
	
	//
	private void updateTags(String file){
		

	    File allFiles = new File(file);

	    DicomObject dcmObj = new BasicDicomObject();
	    DicomInputStream din = null;

	System.out.println(allFiles);    	

	        try {
	        	String val = null;
	        	   
	        	Iterator<DicomElement> iter = dcmObj.datasetIterator();
	        	   while(iter.hasNext()) {
	        	      DicomElement element = iter.next();
	        	      int tag = element.tag();
	        	      try {
	        	         String tagName = dcmObj.nameOf(tag);
	        	         String tagAddr = TagUtils.toString(tag);
	        	         String tagVR = dcmObj.vrOf(tag).toString();
	        	         if (tagVR.equals("SQ")) {
	        	            if (element.hasItems()) {
	        	               continue;
	        	            }
	       	         }    
	        	         tagValue = dcmObj.getString(tag); 
	        	         System.out.println(tagName.toString() + "- " + tagAddr.toString()+ "- " + tagValue);
	   
	        	         if(tagName.matches("Instance Number")){
	        	         System.out.println("VALUE: "+tagValue);

	        	        	 val = tagValue;
	        	        	 System.out.println("NEW VALUE: "+tagValue);
	        	      } 
	        	         }catch (Exception e) {
	        	         e.printStackTrace();
	        	      }
	        	   }
	     	
	        	
	        	
	        	
	        	
	        	
	        	
	            din = new DicomInputStream(allFiles);
	            din.readDicomObject(dcmObj,-1);
	       if (val!=null){
	    	   System.out.println("Going to set: "+ val);
	dcmObj.putString(Tag.AcquisitionNumber, VR.LO, val);

	       }
	            this.writeFile(dcmObj, output, "/" + allFiles.getName());
	            din.close();
	}catch (Exception ex) {
	            ex.printStackTrace();
	        }
	    	   
	    }

		








	public void writeFile(DicomObject obj, String copyServer, String fileName) {
		 
	    File f = new File(copyServer + fileName);
	    FileOutputStream fos;
	    try {
	        fos = new FileOutputStream(f);
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	        return;
	    }
	    BufferedOutputStream bos = new BufferedOutputStream(fos);
	    DicomOutputStream dos = new DicomOutputStream(bos);
	    try {
	        dos.writeDicomFile(obj);
	    } catch (IOException e) {
	        e.printStackTrace();
	        return;
	    } finally {
	        try {
	            dos.close();
	        } catch (IOException ignore) {
	        }
	    }
	}
	
	


	
	



	
	
}
