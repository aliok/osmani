package tr.com.aliok.osmani.annotator.annotation;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.collect.TreeBasedTable;
import org.apache.commons.collections.list.SetUniqueList;
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

    private static final int FLUSH_LIMIT = 10;

    // Map<File Id, Set<Page image file path>>
    TreeMap<String, SetUniqueList> pageNumberMap = Maps.newTreeMap();

    // keep all annotation in the table, not just current file or current page
    // Table<FileID, PageNumber, TreeSet<Annotation>
    // marking specificatlly treeset to mark that it is ordered
    private TreeBasedTable<String, Integer, TreeSet<Annotation>> annotationTable = TreeBasedTable.create();
    private List<Annotation> toBeFlushed = new ArrayList<>();

    private final FilePageHelper filePageHelper = new FilePageHelper();
    private final AnnotationFileIOHelper annotationFileIOHelper = new AnnotationFileIOHelper();

    @PostConstruct
    public void initialize() throws IOException {
        this.pageNumberMap = filePageHelper.buildFilePagesMap();
        this.annotationTable = annotationFileIOHelper.readAllFiles();
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

        toBeFlushed.add(annotation);
        flushIfNecessary();

        return annotation;
    }

    private void flushIfNecessary() {
        if (toBeFlushed.size() >= FLUSH_LIMIT) {
            try {
                doFlush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void doFlush() throws IOException {
        addToBeFlushedListToTable();
        final SortedSet<String> fileIds = this.annotationTable.rowKeySet();
        for (String fileId : fileIds) {
            annotationFileIOHelper.createBackUp(fileId);
            annotationFileIOHelper.writeTableToFile(fileId, annotationTable);
            annotationFileIOHelper.backupCleanUp(fileId);
        }
    }


    private void addToBeFlushedListToTable() {
        for (Annotation annotation : toBeFlushed) {
            final TreeSet<Annotation> annotations = annotationTable.get(annotation.getFileId(), annotation.getPageNumber());
            if (annotations != null) {
                annotations.add(annotation);
            } else {
                final TreeSet<Annotation> objects = Sets.newTreeSet();
                objects.add(annotation);
                annotationTable.put(annotation.getFileId(), annotation.getPageNumber(), objects);
            }
        }
    }

    public int getNumberOfPagesForCurrentFile(String fileId) {
        return this.pageNumberMap.get(fileId).size();
    }

    public TreeSet<Annotation> getAnnotations(String fileId, int pageNumber) {
        return this.annotationTable.get(fileId, pageNumber);
    }
}
