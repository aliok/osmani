package tr.com.aliok.osmani.annotator.annotation;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.collect.TreeBasedTable;
import org.apache.commons.collections.list.SetUniqueList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import tr.com.aliok.osmani.annotator.model.Annotation;
import tr.com.aliok.osmani.annotator.model.AnnotationBuilder;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * @author Ali Ok (ali.ok@apache.org)
 */
@ManagedBean(name = "annotationDataController")
@ApplicationScoped
public class AnnotationDataController implements Serializable {
    protected Log log = LogFactory.getLog(getClass());


    private static final int FLUSH_LIMIT = 10;

    // Map<File Id, Set<Page image file path>>
    TreeMap<String, SetUniqueList> pageNumberMap = Maps.newTreeMap();

    // keep all annotation in the table, not just current file or current page
    // Table<FileID, PageNumber, TreeSet<Annotation>
    // marking specificatlly treeset to mark that it is ordered
    private TreeBasedTable<String, Integer, TreeSet<Annotation>> annotationTable = TreeBasedTable.create();
    private int flushCounter = 0;

    private final FilePageHelper filePageHelper = new FilePageHelper();
    private final AnnotationFileIOHelper annotationFileIOHelper = new AnnotationFileIOHelper();

    @PostConstruct
    public void initialize() {
        try {
            this.pageNumberMap = filePageHelper.buildFilePagesMap();
            this.annotationTable = annotationFileIOHelper.readAllFiles();
        } catch (IOException e) { // postConstruct methods cannot have throws expression in the signature
            // MyFaces uses java.util.logging, this log yourself!
            log.error(e);
            throw new RuntimeException(e);
        }
    }


    public Annotation addNew(String fileId, int pageNumber, int x, int y, int w, int h, String tr_latin, String tr_arabic, String annotationId) {
        final Annotation annotation = new AnnotationBuilder()
                .setFileId(fileId)
                .setPageNumber(pageNumber)
                .setX(x)
                .setY(y)
                .setW(w)
                .setH(h)
                .setTr_latin(tr_latin)
                .setTr_arabic(tr_arabic)
                .setAnnotationId(annotationId)
                .createAnnotation();

        final TreeSet<Annotation> annotations = annotationTable.get(annotation.getFileId(), annotation.getPageNumber());
        if (annotations != null) {
            annotations.add(annotation);
        } else {
            final TreeSet<Annotation> objects = Sets.newTreeSet();
            objects.add(annotation);
            annotationTable.put(annotation.getFileId(), annotation.getPageNumber(), objects);
        }

        flushIfNecessary();

        return annotation;
    }

    private void flushIfNecessary() {
        if (flushCounter >= FLUSH_LIMIT) {
            try {
                doFlush();
                flushCounter = 0;
            } catch (IOException e) {
                // MyFaces uses java.util.logging, this log yourself!
                log.error(e);
                throw new RuntimeException(e);
            }
        }
    }

    protected void doFlush() throws IOException {
        final SortedSet<String> fileIds = this.annotationTable.rowKeySet();
        for (String fileId : fileIds) {
            annotationFileIOHelper.createBackUp(fileId);
            annotationFileIOHelper.writeTableToFile(fileId, annotationTable);
            annotationFileIOHelper.backupCleanUp(fileId);
        }
    }

    public int getNumberOfPagesForCurrentFile(String fileId) {
        return this.pageNumberMap.get(fileId).size();
    }

    public TreeSet<Annotation> getAnnotations(String fileId, int pageNumber) {
        return this.annotationTable.get(fileId, pageNumber);
    }

    public Set<String> getFileIds() {
        return this.pageNumberMap.keySet();
    }
}
