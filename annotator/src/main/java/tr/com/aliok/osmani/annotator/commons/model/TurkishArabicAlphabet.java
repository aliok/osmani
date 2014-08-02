package tr.com.aliok.osmani.annotator.commons.model;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import java.util.Map;

import static tr.com.aliok.osmani.annotator.commons.model.Letters.*;

/**
 * @author Ali Ok (ali.ok@apache.org)
 */
public class TurkishArabicAlphabet {


    public static final ImmutableSet<Letter> ALL_LETTERS = ImmutableSet.of(
            HAMZA,
            ALEF_MEDDA_ABOVE,
            ALEF_HAMZA_ABOVE,
            WAW_HAMZA_ABOVE,
            ALEF_HAMZA_BELOW,
            YEH_HAMZA_ABOVE,

            ALEF,
            BEH,
            TEH,
            TEH_MARBUTA,
            THEH,
            JEEM,
            HAH,
            KHAH,
            DAL,
            THAL,
            REH,
            ZAIN,
            SEEN,
            SHEEN,
            SAD,
            DAD,
            TAH,
            ZAH,
            AIN,
            GAIN,
            FEH,
            QAF,
            KAF,
            LAM,
            MEEM,
            NOON,
            HEH,
            WAW,
            ALEF_MAKSURA,
            YEH,

            FATHATAN,
            DAMMATAN,
            KASRATAN,
            FATHA,
            DAMMA,
            KASRA,
            SHADDA,
            SUKUN,
            MADDAH_ABOVE,
            HAMZA_ABOVE,
            HAMZA_BELOW,

            ZERO,
            ONE,
            TWO,
            THREE,
            FOUR,
            FIVE,
            SIX,
            SEVEN,
            EIGHT,
            NINE,

            PEH,
            TCHEH,
            JEH,
            NG,
            GAF,
            AE,

            LAMALEF,

            QUESTION_MARK,

            YEH_WITH_SMART_DOTS
    );

    public static final Map<Character, Letter> ALL_LETTERS_LOOKUP;

    static {
        final ImmutableMap.Builder<Character, Letter> builder = ImmutableMap.builder();
        for (Letter letter : ALL_LETTERS) {
            builder.put(letter.value, letter);
        }
        ALL_LETTERS_LOOKUP = builder.build();
    }

    public static Letter getLetter(char c) {
        return ALL_LETTERS_LOOKUP.get(c);
    }
}
