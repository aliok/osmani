package tr.com.aliok.osmani.annotator.commons.model;

import com.google.common.base.Charsets;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.io.Files;
import org.apache.commons.io.FilenameUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import tr.com.aliok.osmani.annotator.annotation.AnnotationFormatter;
import tr.com.aliok.osmani.annotator.annotation.AnnotationFormatterImpl;
import tr.com.aliok.osmani.annotator.commons.AppProperties;
import tr.com.aliok.osmani.annotator.model.Annotation;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(MockitoJUnitRunner.class)
public class TurkishArabicAlphabetTest {

    @Test
    public void allLettersSetShouldReallyHaveAllDefined() throws IllegalAccessException {
        final Field[] declaredFields = Letters.class.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            if (declaredField.getType().isAssignableFrom(Letter.class)) {
                final Letter fieldValue = (Letter) declaredField.get(Letters.class);
                assertThat(TurkishArabicAlphabet.getLetter(fieldValue.value), notNullValue());
                assertThat(TurkishArabicAlphabet.getLetter(fieldValue.value), equalTo(fieldValue));
            }
        }
    }

    @Test
    public void allLettersShouldHaveAllNonAsciiCharsInArabicTextOfAnnotationFiles() throws IOException {
        final File[] allFiles = new File(AppProperties.bookAnnotationFolder()).listFiles();
        final Iterable<File> annotationFiles = Iterables.filter(Arrays.asList(allFiles), new Predicate<File>() {
            @Override
            public boolean apply(File input) {
                return FilenameUtils.getExtension(input.getName()).equalsIgnoreCase("txt");
            }
        });

        final AnnotationFormatter annotationFormatter = new AnnotationFormatterImpl();

        for (File annotationFile : annotationFiles) {
            final List<String> lines = Files.readLines(annotationFile, Charsets.UTF_8);
            for (String line : lines) {
                if (line.startsWith("#"))
                    continue;
                final Annotation annotation = annotationFormatter.parseLine(annotationFile.getName(), line);
                final String tr_arabic = annotation.getTr_arabic();
                for (char c : tr_arabic.toCharArray()) {
                    if (c < 256)        // skip ascii chars
                        continue;
                    ;
                    assertThat("Character (\\u" + (Integer.toHexString(c)) + ") was not found in TurkishArabicAlphabet! : \n\t'" + c + "'", TurkishArabicAlphabet.getLetter(c), notNullValue());
                }

            }
        }

    }

}