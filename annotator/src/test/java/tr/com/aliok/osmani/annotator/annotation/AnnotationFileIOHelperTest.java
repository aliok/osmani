package tr.com.aliok.osmani.annotator.annotation;

import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.Test;
import tr.com.aliok.osmani.annotator.commons.AppProperties;
import tr.com.aliok.osmani.annotator.model.Annotation;
import tr.com.aliok.osmani.annotator.model.AnnotationBuilder;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.TreeSet;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

public class AnnotationFileIOHelperTest {

    final AnnotationFileIOHelper helper = new AnnotationFileIOHelper();

    @Test
    public void shouldWriteToFile() throws IOException {
        final Annotation annotation = new AnnotationBuilder()
                .setAnnotationId("12345678-1234-1234-1234-1234567890ab")
                .setPageNumber(10)
                .setX(10)
                .setY(200)
                .setW(250)
                .setH(300)
                .setTr_arabic("test_arabic")
                .setTr_latin("test_latin")
                .setFileId("test.pdf")
                .createAnnotation();


        Table<String, Integer, TreeSet<Annotation>> table = TreeBasedTable.create();
        table.put(annotation.getFileId(), annotation.getPageNumber(), new TreeSet<>(Arrays.asList(annotation)));

        helper.writeTableToFile("test.pdf", table);

        final File expectedFile = new File(FilenameUtils.concat(AppProperties.bookAnnotationFolder(), "00_test.pdf.annotation.txt"));
        final File retrievedFile = new File(FilenameUtils.concat(AppProperties.bookAnnotationFolder(), "test.pdf.annotation.txt"));

        final String expected = FileUtils.readFileToString(expectedFile);
        final String retrieved = FileUtils.readFileToString(retrievedFile);

        assertThat(expected, equalTo(retrieved));
    }

    @Test
    public void shouldRead() throws IOException {
        final TreeBasedTable<String, Integer, TreeSet<Annotation>> table = helper.readAllFiles();

        final Annotation annotation = new AnnotationBuilder()
                .setAnnotationId("12345678-1234-1234-1234-1234567890ab")
                .setPageNumber(10)
                .setX(10)
                .setY(200)
                .setW(250)
                .setH(300)
                .setTr_arabic("test_arabic")
                .setTr_latin("test_latin")
                .setFileId("test.pdf")
                .createAnnotation();

        final TreeSet<Annotation> allForTestFile = table.get("test.pdf", 10);

        assertThat(allForTestFile, hasSize(1));

        final Annotation retrieved = allForTestFile.iterator().next();

        assertThat(annotation.getFileId(), equalTo(retrieved.getFileId()));
        assertThat(annotation.getAnnotationId(), equalTo(retrieved.getAnnotationId()));
        assertThat(annotation.getPageNumber(), equalTo(retrieved.getPageNumber()));
        assertThat(annotation.getX(), equalTo(retrieved.getX()));
        assertThat(annotation.getY(), equalTo(retrieved.getY()));
        assertThat(annotation.getW(), equalTo(retrieved.getW()));
        assertThat(annotation.getH(), equalTo(retrieved.getH()));
        assertThat(annotation.getTr_arabic(), equalTo(retrieved.getTr_arabic()));
        assertThat(annotation.getTr_latin(), equalTo(retrieved.getTr_latin()));

    }

}