package test;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;

import org.dcm4che2.data.BasicDicomObject;
import org.dcm4che2.data.DicomElement;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.Tag;
import org.dcm4che2.data.VR;
import org.dcm4che2.io.DicomInputStream;
import org.dcm4che2.io.DicomOutputStream;
import org.dcm4che2.util.TagUtils;
import org.xml.sax.Attributes;

public class Header {

static String input="/Volumes/DUNDEE_STUDY/IMAGES/DBT Multi-Reader Study/DBT/BATCH 2_B/Original/DBT_1044_27_01_2014_CT";
static String output="/Volumes/DUNDEE_STUDY/IMAGES/DBT Multi-Reader Study/DBT/BATCH 2_B/Original/bob";

private String tagValue;

	
	
	
public static void main(String[] args) throws IOException {
	Scanner reader = new Scanner(System.in);  
	
	

	   Header list = new Header();
	   list.updateTags();
	   File file = new File("");

	
}


private void updateTags() {		
	
	File[] tempImages = new File(output).listFiles();
    for (int j = 0; j < tempImages.length; j++) {
        tempImages[j].delete();
    }
    
    File[] allFiles = new File(input).listFiles();
    
    DicomObject dcmObj = new BasicDicomObject();
    DicomInputStream din = null;
    for (int i = 0; i < allFiles.length; i++) {
    	System.out.println(allFiles[i]);    	
    	
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
               	         if(tagName.matches("Instance Number")){
                	         System.out.println("VALUE: "+tagValue);
                	         
                	        	 val = tagValue;
                	        	 System.out.println("NEW VALUE: "+tagValue);
                	         
                	      } 
        	               continue;
        	            }
       	         }    
        	         tagValue = dcmObj.getString(tag);    
        	         if(tagName.matches("Instance Number")){
        	         System.out.println("VALUE: "+tagValue);
        	         
        	        	 val = tagValue;
        	        	 System.out.println("NEW VALUE: "+tagValue);
        	         
        	      } 
        	         }catch (Exception e) {
        	         e.printStackTrace();
        	      }
        	   }
     	
        	
        	
        	
        	
        	
        	
            din = new DicomInputStream(allFiles[i]);
            din.readDicomObject(dcmObj,-1);
       if (val!=null){
    	   System.out.println("Going to set: "+ val);
dcmObj.putString(Tag.AcquisitionNumber, VR.LO, val);

       }
            this.writeFile(dcmObj, output, "/" + allFiles[i].getName());
            din.close();
}catch (Exception ex) {
            ex.printStackTrace();
        }
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


