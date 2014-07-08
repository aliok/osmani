package tr.com.aliok.osmani.annotator.annotation;

import com.google.common.collect.TreeBasedTable;
import org.junit.Ignore;
import org.junit.Test;
import tr.com.aliok.osmani.annotator.commons.AppProperties;
import tr.com.aliok.osmani.annotator.model.Annotation;

import java.io.IOException;
import java.util.TreeSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasItem;

public class AnnotationFormatterImplOLD190Test {

    @Test
    @Ignore
    public void convertFromOldFormatToNewFormat_01_kolayelifba() throws IOException {
        AnnotationFileIOHelper helper = new AnnotationFileIOHelper();
        final AnnotationFormatterImpl newFormatter = new AnnotationFormatterImpl();
        final AnnotationFormatterImplOLD190 oldFormatter = new AnnotationFormatterImplOLD190();

        final TreeBasedTable<String, Integer, TreeSet<Annotation>> table = TreeBasedTable.create();
        final String annotationFolder = AppProperties.bookAnnotationFolder();

        helper.setAnnotationFormatter(oldFormatter);
        helper.readFile(table, annotationFolder, "01_kolayelifba.pdf.annotation.txt");

        helper.setAnnotationFormatter(newFormatter);
        helper.writeTableToFile("01_kolayelifba.pdf", table);
    }
}