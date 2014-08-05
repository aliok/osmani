package tr.com.aliok.osmani.annotator.validation;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import org.apache.commons.io.FilenameUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import tr.com.aliok.osmani.annotator.commons.App;
import tr.com.aliok.osmani.annotator.commons.AppProperties;
import tr.com.aliok.osmani.annotator.commons.AppRunner;

import java.io.File;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasItem;

@RunWith(AppRunner.class)
public class AnnotationValidationApp {
    AnnotationValidationManager validationManager;
    AnnotationContext annotationContext;


    @Before
    public void before() throws Exception {
        validationManager = new AnnotationValidationManager();
        annotationContext = AnnotationContext.buildFromFiles(Iterables.filter(Arrays.asList(new File(AppProperties.bookAnnotationFolder()).listFiles()), new Predicate<File>() {
            @Override
            public boolean apply(File input) {
                return FilenameUtils.getName(input.getName()).toLowerCase().endsWith(".annotation.txt");
            }
        }));
    }

    @App("Validates all annotations against all rules")
    public void validateAllAnnotationsAgainstAllRules() {
        validationManager.validateAll(annotationContext);
    }
}