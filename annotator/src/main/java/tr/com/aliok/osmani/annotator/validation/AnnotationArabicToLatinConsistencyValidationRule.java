package tr.com.aliok.osmani.annotator.validation;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.tuple.Pair;
import tr.com.aliok.osmani.annotator.model.Annotation;

import java.util.Collection;
import java.util.Set;

/**
 * @author Ali Ok (ali.ok@apache.org)
 */
public class AnnotationArabicToLatinConsistencyValidationRule implements AnnotationValidationRule {
    private static ImmutableSet<String> ALLOWED_AMBIGUOUS_WORDS = ImmutableSet.of(
            "\u0627\u0648\u0646",           // un - on
            "\u062f\u06d5",                 // da - de
            "\u0642\u0648\u0634",           // kus -kos
            "\u0628\u0648\u0632",           // buz - boz
            "\u0645\u064a\u0633\u06ad",     // misin - musun
            "\u0645\u062d\u0645\u062f"      // muhammed - mehmed

    );

    @Override
    public void validate(Annotation annotation, AnnotationContext annotationContext) {
        final String tr_arabic = annotation.getTr_arabic();

        // skip single letters!
        if (tr_arabic.length() == 1)
            return;

        if(ALLOWED_AMBIGUOUS_WORDS.contains(tr_arabic))
            return;


        final Multimap<String, Annotation> arabicToAnnotationMap = annotationContext.getArabicToAnnotationMap();

        final Collection<Annotation> annotations = arabicToAnnotationMap.get(tr_arabic);
        final Set<Pair<String, String>> foundLatinEquivalents = Sets.newHashSet(Iterables.transform(annotations, new Function<Annotation, Pair<String, String>>() {
            @Override
            public Pair<String, String> apply(Annotation input) {
                return Pair.of(input.getTr_latin(), input.getTr_latin2());
            }
        }));

        if (foundLatinEquivalents.size() == 0) {
            throw new IllegalStateException("How is this possible?");
        } else if (foundLatinEquivalents.size() > 1) {
            throw new RuntimeException("Found multiple latin equivalents for word in Arabic script : \n\t '" + annotation + "' \n\t" + foundLatinEquivalents + "\n\t in following annotations : \n\t\t" +
                    Joiner.on("\n\t\t").join(annotations));
        }
    }
}
