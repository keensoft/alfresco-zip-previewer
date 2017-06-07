package es.keensoft.repo.content.transform;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.alfresco.error.AlfrescoRuntimeException;
import org.alfresco.repo.content.transform.ContentTransformerHelper;
import org.alfresco.repo.content.transform.ContentTransformerWorker;
import org.alfresco.service.cmr.repository.ContentReader;
import org.alfresco.service.cmr.repository.ContentWriter;
import org.alfresco.service.cmr.repository.TransformationOptions;
import org.alfresco.util.TempFileProvider;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.InitializingBean;

public abstract class CompressContentTransformerWorker extends ContentTransformerHelper implements ContentTransformerWorker, InitializingBean {

	// Entry extraction to be implemented by compression format (ZIP, RAR...)
	protected abstract Set<String> listEntries(File compressedFile) throws Exception;	
    
	@Override
	public void transform(ContentReader reader, ContentWriter writer, TransformationOptions options) throws Exception {
		
        String sourceMimetype = getMimetype(reader);
        String targetMimetype = getMimetype(writer);
        
        String sourceExtension = getMimetypeService().getExtension(sourceMimetype);
        String targetExtension = getMimetypeService().getExtension(targetMimetype);
        if (sourceExtension == null || targetExtension == null)
        {
            throw new AlfrescoRuntimeException("Unknown extensions for mimetypes: \n" +
                    "   source mimetype: " + sourceMimetype + "\n" +
                    "   source extension: " + sourceExtension + "\n" +
                    "   target mimetype: " + targetMimetype + "\n" +
                    "   target extension: " + targetExtension);
        }
        
        File sourceFile = TempFileProvider.createTempFile(getClass().getSimpleName() + "_source_", "." + sourceExtension);
        File targetFile = TempFileProvider.createTempFile(getClass().getSimpleName() + "_target_", "." + targetExtension);
                
        reader.getContent(sourceFile);
        convertToPDF(sourceFile, targetFile);
        writer.putContent(targetFile);
        
	}
	
    private void convertToPDF(File sourceFile, File targetFile) throws Exception {
		
        PDDocument doc = new PDDocument();
        PDPage page = new PDPage();

        doc.addPage(page);

        PDPageContentStream content = new PDPageContentStream(doc, page);
        
        String compressedEntries = formatCompressEntries(sourceFile);
        
        float fontSize = 8;
        float leading = 1.5f * fontSize;
        
        PDRectangle mediabox = page.findMediaBox();
        float margin = 72;
        float startX = mediabox.getLowerLeftX() + margin;
        float startY = mediabox.getUpperRightY() - margin;
        
        float currentY = startY;
        
        content.beginText();
        content.setFont(PDType1Font.COURIER, fontSize);
        content.moveTextPositionByAmount(startX, startY);
        String[] lines = compressedEntries.split("\n");
        for (String line : lines) {
        	
        	if (currentY < margin) {
        		
                content.endText();
                content.close();
                
                page = new PDPage();
                doc.addPage(page);
                content = new PDPageContentStream(doc,page);
                
                content.beginText();
                content.setFont(PDType1Font.COURIER, fontSize);
                content.moveTextPositionByAmount(startX, startY);
                
                currentY = startY;
        		
        	}
        	
            content.drawString(line);
            content.moveTextPositionByAmount(0, -leading);
            currentY = currentY - leading;

        }
        content.endText();
        
        content.close();
        doc.save(targetFile);
        doc.close();
        
    }
    
    private String formatCompressEntries(File compressedFile) throws Exception {
    	
		StringBuffer compressEntries = new StringBuffer();
    	
    	Set<String> entries = listEntries(compressedFile);
		
		compressEntries.append(".\n");
		
		List<String> directories = new ArrayList<String>();
		
		for (String entry : entries) {
			
			// TODO Find a better way to split paths in a platform independent way
			String[] parts = entry.split("/");
			if (parts.length == 1) {
				parts = entry.split("\\\\");
			}
			
			String tabs = "";
			for (String part : parts) {
				if (!directories.contains(tabs + "+-- " + part)) {
					String line = tabs + "+-- " + part;
					directories.add(line);
					if (tabs.length() > 0) {
						for (int i = 1; i <= tabs.length() / 2; i++) {
							line = line.substring(0, i*2 - 2) + "|" + line.substring(i*2 - 1);	
						}
					}
					compressEntries.append(line+"\n");
				}
				tabs = tabs + "  ";
			}
		}
		
		return compressEntries.toString();
    	
    }
    
	@Override
	public boolean isTransformable(String sourceMimetype, String targetMimetype, TransformationOptions options) {
		return true;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@Override
	public boolean isAvailable() {
		return true;
	}

	@Override
	public String getVersionString() {
		return "1.0";
	}

}
