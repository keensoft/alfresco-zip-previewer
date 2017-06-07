package es.keensoft.repo.content.transform;

import java.io.File;
import java.util.Enumeration;
import java.util.Set;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.alfresco.repo.content.transform.ContentTransformerWorker;
import org.springframework.beans.factory.InitializingBean;

public class ZipContentTransformerWorker extends CompressContentTransformerWorker implements ContentTransformerWorker, InitializingBean {

	@Override
	protected Set<String> listEntries(File compressedFile) throws Exception {
		
        Set<String> sortedEntries = new TreeSet<String>();
        ZipFile zipFile = new ZipFile(compressedFile);
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry zipEntry = entries.nextElement();
            if (!zipEntry.isDirectory()) {
              sortedEntries.add(zipEntry.getName());
            }
        }
        zipFile.close();
        return sortedEntries;
        
    }	
    
}
