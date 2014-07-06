package tr.com.aliok.osmani.annotator.annotation;

import com.google.common.io.ByteStreams;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.math.NumberUtils;
import tr.com.aliok.osmani.annotator.commons.AppProperties;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author Ali Ok (ali.ok@apache.org)
 */
public class ImageFileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String fileId = getRequiredStringParameter(req, "fileId");
        final int pageNumber = getRequiredIntegerParameter(req, "pageNumber");

        Validate.isTrue(pageNumber >= 0);

        resp.setContentType("image/jpg");
        final File externalFile = getImageFile(fileId, pageNumber);
        resp.setContentLength((int) externalFile.length());

        final ServletOutputStream outputStream = resp.getOutputStream();
        final BufferedInputStream from = new BufferedInputStream(new FileInputStream(externalFile));
        try {
            ByteStreams.copy(from, outputStream);
        } finally {
            outputStream.flush();
            outputStream.close();
            from.close();
        }
    }

    private File getImageFile(String fileId, int pageNumber) {
        final String pageNumberStr = String.format("%04d", pageNumber);
        final String fileName = FilenameUtils.getBaseName(fileId) + "_" + pageNumberStr + ".jpg";
        return new File(AppProperties.bookPagesFolder(), fileName);
    }

    private int getRequiredIntegerParameter(HttpServletRequest req, String key) {
        final String requiredStringParameter = getRequiredStringParameter(req, key);
        Validate.isTrue(NumberUtils.isNumber(requiredStringParameter));
        return Integer.parseInt(requiredStringParameter);
    }

    private String getRequiredStringParameter(HttpServletRequest req, String key) {
        final String parameter = req.getParameter(key);
        Validate.notBlank(parameter);
        return parameter.trim();
    }

}
