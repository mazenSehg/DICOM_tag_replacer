package test;

import java.io.File;
import java.util.Iterator;

import org.dcm4che2.data.DicomElement;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.io.DicomInputStream;
import org.dcm4che2.util.TagUtils;

 
public class edit {
 
 
 
    public void listHeader(DicomObject object) {
    	   Iterator<DicomElement> iter = object.datasetIterator();
    	   while(iter.hasNext()) {
    	      DicomElement element = iter.next();
    	      int tag = element.tag();
    	      try {
    	         String tagName = object.nameOf(tag);
    	         String tagAddr = TagUtils.toString(tag);
    	         String tagVR = object.vrOf(tag).toString();
    	         if (tagVR.equals("SQ")) {
    	            if (element.hasItems()) {
    	               System.out.println(tagAddr +" ["+  tagVR +"] "+ tagName);
    	               listHeader(element.getDicomObject());
    	               continue;
    	            }
    	         }    
    	         String tagValue = object.getString(tag);    
    	         System.out.println(tagAddr +" ["+ tagVR +"] "+ tagName +" ["+ tagValue+"]");
    	      } catch (Exception e) {
    	         e.printStackTrace();
    	      }
    	   }  
    	}
 
    public static void main(String[] args) {
    	String input = "/Volumes/DUNDEE_STUDY/IMAGES/DBT Multi-Reader Study/DBT/BATCH 6_B/BATCH6_B BTO";
    	String tag = "";
    	

             System.out.println("INPUT FILE: "+ input);
             System.out.println("TAG TO REPLACE: "+ tag);
    	
    	   DicomObject object = null;  
    	   try {
    	      DicomInputStream dis = new DicomInputStream(new File(input));
    	      object = dis.readDicomObject();
    	      dis.close();
    	   } catch (Exception e) {
    	      System.out.println(e.getMessage());
    	      System.exit(0);
    	   }
    	   edit list = new edit();
    	   list.listHeader(object);
    	
    }
}