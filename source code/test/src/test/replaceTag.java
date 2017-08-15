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

public class replaceTag {

static String input="/Volumes/DUNDEE_STUDY/IMAGES/DBT Multi-Reader Study/DBT/BATCH 2_B/Original/DBT_1044_27_01_2014_CT";
static String output="/Volumes/DUNDEE_STUDY/IMAGES/DBT Multi-Reader Study/DBT/BATCH 2_B/Original/bob/DBT_1044_27_01_2014_CT";

private String tagValue;
private String val;

	
	
	
public static void main(String[] args) throws IOException {


	   replaceTag list = new replaceTag();
	   list.updateTags();
	   File file = new File("");
	
}


void updateTags() {
		
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
        	
            din = new DicomInputStream(allFiles[i]);
            din.readDicomObject(dcmObj,-1);
           
     	   DicomObject object = null; 
            try {
     	      DicomInputStream dis = new DicomInputStream(allFiles[i]);
     	      object = dis.readDicomObject();
     	      dis.close();
     	   } catch (Exception e) {
     	      System.out.println(e.getMessage());
     	      System.exit(0);
     	   }
     	   replaceTag list = new replaceTag();
     	   val = list.listHeader(object);
     	  int foo = Integer.parseInt(val);
     	   
     	   System.out.println("Value to replace - "+val);
       if (val!=null){
    	   System.out.println("Going to set: "+ foo);
dcmObj.putString(Tag.AcquisitionNumber, VR.LO, ""+0);

       }
            this.writeFile(dcmObj, output, "/" + allFiles[i].getName());
            din.close();
            System.out.println("---SAVED NEW VALUE---");
}catch (Exception ex) {
            ex.printStackTrace();
        }
    	   }
    }


public String listHeader(DicomObject object) throws IOException {
	String vax = null;
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
//					System.out.println(tagAddr + " [" + tagVR + "] "+ tagName);
					listHeader(element.getDicomObject());
					continue;
				}
			}

			String tagValue = object.getString(tag);
			if (tagName.matches("Instance Number") && tagValue != null) {
				System.out.println(tagValue);
				vax = tagValue;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	val = vax;
	System.out.println("VALUE FOUND: "+val);
	return vax;
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


