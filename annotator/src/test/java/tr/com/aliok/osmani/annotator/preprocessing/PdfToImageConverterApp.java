package tr.com.aliok.osmani.annotator.preprocessing;

import org.apache.commons.io.FilenameUtils;
import org.junit.runner.RunWith;
import tr.com.aliok.osmani.annotator.commons.App;
import tr.com.aliok.osmani.annotator.commons.AppProperties;
import tr.com.aliok.osmani.annotator.commons.AppRunner;

import java.io.File;

/**
 * @author Ali Ok (ali.ok@apache.org)
 */
@RunWith(AppRunner.class)
public class PdfToImageConverterApp {

    @App
    public void convertBooksToImages(){
        final PdfToImageConverter pdfToImageConverter = new PdfToImageConverter();

        final File sourceFolder = new File(AppProperties.bookSourceFolder());

        final File[] files = sourceFolder.listFiles();
        for (File file : files) {
            if(file.isDirectory())
                continue;
            if(!FilenameUtils.getExtension(file.getAbsolutePath()).equalsIgnoreCase("pdf"))
                continue;

            pdfToImageConverter.convertPdfToImage(file, new File(AppProperties.bookPagesFolder()));
        }
    }
}
