package es.keensoft.repo.content.transform;

import java.io.File;
import java.util.Enumeration;
import java.util.Set;
import java.util.TreeSet;

import org.alfresco.repo.content.transform.ContentTransformerWorker;
import org.springframework.beans.factory.InitializingBean;

import com.adarshr.raroscope.RAREntry;
import com.adarshr.raroscope.RARFile;

public class RARContentTransformerWorker extends CompressContentTransformerWorker implements ContentTransformerWorker, InitializingBean {

	@Override
	protected Set<String> listEntries(File compressedFile) throws Exception {
		
        Set<String> sortedEntries = new TreeSet<String>();
		RARFile rarFile = new RARFile(compressedFile);
		Enumeration<RAREntry> entries = rarFile.entries();
		while (entries.hasMoreElements()) {
		    RAREntry rarEntry = entries.nextElement();
		    if (!rarEntry.isDirectory()) {
		        sortedEntries.add(rarEntry.getName());
		    }
		}		
        rarFile.close();
        return sortedEntries;
        
	}	
    
}
