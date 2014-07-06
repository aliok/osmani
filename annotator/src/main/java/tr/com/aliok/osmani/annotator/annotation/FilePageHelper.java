package tr.com.aliok.osmani.annotator.annotation;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.list.SetUniqueList;
import org.apache.commons.io.FilenameUtils;
import tr.com.aliok.osmani.annotator.commons.AppProperties;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

/**
 * @author Ali Ok (ali.ok@apache.org)
 */
public class FilePageHelper {
    public TreeMap<String, SetUniqueList> buildFilePagesMap() {
        final File bookSourceFolder = new File(AppProperties.bookSourceFolder());
        //noinspection ConstantConditions
        final Iterable<File> pdfs = Iterables.filter(Lists.newArrayList(bookSourceFolder.listFiles()), new Predicate<File>() {
            @Override
            public boolean apply(File input) {
                return FilenameUtils.getExtension(input.getAbsolutePath()).equalsIgnoreCase("pdf");
            }
        });

        final TreeMap<String, SetUniqueList> map = Maps.newTreeMap();

        for (File file : pdfs) {
            final String fileId = FilenameUtils.getName(file.getAbsolutePath());
            final SetUniqueList pages = getPageFiles(fileId);
            map.put(fileId, pages);
        }

        return map;
    }

    private SetUniqueList getPageFiles(String fileId) {
        final String baseFileId = FilenameUtils.getBaseName(fileId);

        final String bookPagesFolder = AppProperties.bookPagesFolder();
        final ArrayList<String> resultWithoutFolder = Lists.newArrayList(Iterables.filter(Lists.newArrayList(new File(bookPagesFolder).list()), new Predicate<String>() {
            @Override
            public boolean apply(String input) {
                final String baseName = FilenameUtils.getBaseName(input);
                return baseName.startsWith(baseFileId);
            }
        }));


        final List<String> result = Lists.newArrayList(Lists.transform(resultWithoutFolder, new Function<String, String>() {
            @Override
            public String apply(String input) {
                return FilenameUtils.concat(bookPagesFolder, input);
            }
        }));


        Collections.sort(result);
        return SetUniqueList.decorate(result);
    }
}
