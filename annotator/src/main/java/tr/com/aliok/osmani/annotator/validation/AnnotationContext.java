package tr.com.aliok.osmani.annotator.validation;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Multimap;
import com.google.common.collect.Ordering;
import com.google.common.io.Files;
import tr.com.aliok.osmani.annotator.annotation.AnnotationFormatterImpl;
import tr.com.aliok.osmani.annotator.model.Annotation;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Ali Ok (ali.ok@apache.org)
 */
public class AnnotationContext {
    private final ImmutableMultimap<String, Annotation> arabicToAnnotationMap;
    private final ImmutableSortedSet<Annotation> allAnnotations;

    protected AnnotationContext(ImmutableMultimap<String, Annotation> arabicToAnnotationMap, ImmutableSortedSet<Annotation> allAnnotations) {
        this.arabicToAnnotationMap = arabicToAnnotationMap;
        this.allAnnotations = allAnnotations;
    }

    public static AnnotationContext buildFromFiles(Iterable<File> annotationFiles) throws IOException {
        final ImmutableSortedSet.Builder<Annotation> allAnnotationsBuilder = new ImmutableSortedSet.Builder<>(Ordering.natural());
        final ImmutableMultimap.Builder<String, Annotation> arabicToAnnotationMapBuilder = new ImmutableMultimap.Builder<>();


        final AnnotationFormatterImpl annotationFormatter = new AnnotationFormatterImpl();

        for (File annotationFile : annotationFiles) {
            final List<String> lines = Files.readLines(annotationFile, Charsets.UTF_8);
            for (String line : lines) {
                if (line.startsWith("#")) {
                    continue;
                }

                final Annotation annotation = annotationFormatter.parseLine(annotationFile.getName(), line);
                allAnnotationsBuilder.add(annotation);
                arabicToAnnotationMapBuilder.put(annotation.getTr_arabic(), annotation);
            }

        }

        return new AnnotationContext(arabicToAnnotationMapBuilder.build(), allAnnotationsBuilder.build());
    }

    public Multimap<String, Annotation> getArabicToAnnotationMap() {
        return arabicToAnnotationMap;
    }

    public Iterable<Annotation> getAllAnnotations() {
        return allAnnotations;
    }
}
