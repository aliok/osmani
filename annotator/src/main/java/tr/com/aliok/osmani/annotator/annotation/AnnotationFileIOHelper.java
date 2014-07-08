package tr.com.aliok.osmani.annotator.annotation;

import com.google.common.base.Charsets;
import com.google.common.base.Predicate;
import com.google.common.collect.*;
import com.google.common.io.Files;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.Validate;
import tr.com.aliok.osmani.annotator.commons.AppProperties;
import tr.com.aliok.osmani.annotator.model.Annotation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

/**
 * @author Ali Ok (ali.ok@apache.org)
 */
public class AnnotationFileIOHelper implements Serializable {

    private static final String BACKUP_TIME_FORMAT = "YYYY_MM_d_HH_mm_ss_SSS";

    private static final int BACKUP_CLEANUP_LIMIT = 5;

    private AnnotationFormatter annotationFormatter;

    public void writeTableToFile(String fileId, Table<String, Integer, TreeSet<Annotation>> annotationTable) throws IOException {
        final File annotationFile = getAnnotationFile(fileId);
        BufferedWriter writer = null;
        try {
            writer = Files.newWriter(annotationFile, Charsets.UTF_8);

            writer.write(annotationFormatter.getFormatCommentLine());
            writer.newLine();

            final Map<Integer, TreeSet<Annotation>> fileAnnotations = annotationTable.rowMap().get(fileId);
            for (Map.Entry<Integer, TreeSet<Annotation>> pageAnnotationsEntry : fileAnnotations.entrySet()) {
                // annotations have page number in them anyway, so ignore the entry key (pageNumber)
                final TreeSet<Annotation> annotations = pageAnnotationsEntry.getValue();
                for (Annotation annotation : annotations) {
                    final String line = annotationFormatter.formatAnnotation(annotation);
                    writer.write(line);
                    writer.newLine();
                }
            }

            writer.flush();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private File getAnnotationFile(String fileId) {
        final String annotationFolder = AppProperties.bookAnnotationFolder();
        return new File(FilenameUtils.concat(annotationFolder, fileId + ".annotation.txt"));
    }

    public void createBackUp(String fileId) throws IOException {
        final File annotationFile = getAnnotationFile(fileId);
        if (!annotationFile.exists()) {
            // nothing to back up yet
            return;
        }

        final String timeString = new SimpleDateFormat(BACKUP_TIME_FORMAT).format(new Date());
        final File backupFile = new File(annotationFile.getAbsolutePath() + "_" + timeString + ".bak");
        FileUtils.copyFile(annotationFile, backupFile);
    }

    public void backupCleanUp(final String fileId) throws IOException {
        final String annotationFolder = AppProperties.bookAnnotationFolder();
        final List<String> allFiles = Lists.newArrayList(new File(annotationFolder).list());

        final TreeSet<String> backupFilesForGiven = Sets.newTreeSet(Iterables.filter(allFiles, new Predicate<String>() {
            @Override
            public boolean apply(String input) {
                final String name = FilenameUtils.getName(input);
                return name.startsWith(fileId) && !name.equals(fileId+".annotation.txt");
            }
        }));

        if (backupFilesForGiven.size() > BACKUP_CLEANUP_LIMIT) {
            final int toBeDeleted = backupFilesForGiven.size() - BACKUP_CLEANUP_LIMIT;
            int i = 0;
            for (String backupFile : backupFilesForGiven) {
                final boolean deleted = new File(FilenameUtils.concat(annotationFolder, backupFile)).delete();
                if (!deleted)
                    throw new IOException("Cannot delete file : " + backupFile);

                i++;
                if (i>toBeDeleted)
                    break;
            }
        }


    }

    public TreeBasedTable<String, Integer, TreeSet<Annotation>> readAllFiles() throws IOException {
        final TreeBasedTable<String, Integer, TreeSet<Annotation>> table = TreeBasedTable.create();

        final String annotationFolder = AppProperties.bookAnnotationFolder();

        final List<String> allFiles = Lists.newArrayList(new File(annotationFolder).list());

        final TreeSet<String> annotationFiles = Sets.newTreeSet(Iterables.filter(allFiles, new Predicate<String>() {
            @Override
            public boolean apply(String input) {
                final String name = FilenameUtils.getName(input);
                return !name.endsWith(".bak");
            }
        }));

        for (String annotationFile : annotationFiles) {
            readFile(table, annotationFolder, annotationFile);
        }


        return table;
    }

    public void readFile(TreeBasedTable<String, Integer, TreeSet<Annotation>> table, String annotationFolder, String annotationFile) throws IOException {
        final String fileName = FilenameUtils.getName(annotationFile);
        Validate.isTrue(fileName.endsWith("annotation.txt"));

        final String fileId = fileName.substring(0, fileName.length() - ".annotation.txt".length());


        // read at once
        final List<String> lines = FileUtils.readLines(new File(FilenameUtils.concat(annotationFolder, annotationFile)), Charsets.UTF_8);
        for (String line : lines) {
            if (line.startsWith("#")) {
                continue;
            }

            final Annotation annotation = this.annotationFormatter.parseLine(fileId, line);

            final TreeSet<Annotation> annotations = table.get(fileId, annotation.getPageNumber());
            if (annotations == null) {
                final TreeSet<Annotation> newSet = new TreeSet<>();
                newSet.add(annotation);
                table.put(fileId, annotation.getPageNumber(), newSet);
            } else {
                annotations.add(annotation);
            }
        }
    }

    public void setAnnotationFormatter(AnnotationFormatter annotationFormatter) {
        this.annotationFormatter = annotationFormatter;
    }
}
