package tr.com.aliok.osmani.annotator.preprocessing;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.util.ImageIOUtil;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Ali Ok (ali.ok@apache.org)
 */
public class PdfToImageConverter {

    private static final int IMAGE_TYPE = BufferedImage.TYPE_INT_RGB;
    private static final int RESOULUTION = 96;
    private static final String IMAGE_FORMAT = "jpg";

    protected Log log = LogFactory.getLog(getClass());

    public File convertPdfToImage(File sourceFile, File targetFolder) {
        log.info(String.format("Gonna convert file %s to images into folder %s", sourceFile, targetFolder));

        PDDocument document = null;
        try {
            document = PDDocument.loadNonSeq(sourceFile, null);
            final int numberOfPages = document.getNumberOfPages();
            final int digitsForFileName = (int) Math.ceil(Math.log10(numberOfPages));

            final String sourceBaseName = FilenameUtils.getBaseName(sourceFile.getName());

            final String targetBaseName = FilenameUtils.concat(targetFolder.getAbsolutePath(), sourceBaseName) + "_%1$0" + digitsForFileName + "d";
            //noinspection unchecked
            final List<PDPage> allPages = document.getDocumentCatalog().getAllPages();
            for (int i = 0; i < allPages.size(); i++) {
                final PDPage page = allPages.get(i);
                final String targetFileName = String.format(targetBaseName, i);
                log.info(String.format("\tConverting page %d", i));
                this.contertPdfPageToImage(page, new File(targetFileName));
            }

        } catch (IOException e) {
            log.error("Error creating preview files for document: " + sourceFile, e);
        } finally {
            if (document != null)
                try {
                    document.close();
                } catch (IOException e) {
                    log.error(e, e);
                    //swallow
                }
        }

        return null;
    }

    public void contertPdfPageToImage(PDPage pdfPage, File dumpTargetFile) throws IOException {
        final BufferedImage pageImage = pdfPage.convertToImage(IMAGE_TYPE, RESOULUTION);

        // unfortunately, following method stupidly adds the extension again: abc.jpg -> abc.jpg.jpg
        // so give the path without the extension
        final String pathWithoutExtension = FilenameUtils.getFullPath(dumpTargetFile.getPath()) + FilenameUtils.getBaseName(dumpTargetFile.getName());
        final boolean result = ImageIOUtil.writeImage(pageImage, IMAGE_FORMAT, pathWithoutExtension, IMAGE_TYPE, RESOULUTION);
        if (!result) {
            throw new RuntimeException("Something went wrong. It is reported, but the exact reason is unknown.");
        }
    }

}
