package tr.com.aliok.osmani.annotator.annotation;

import org.apache.commons.collections.list.SetUniqueList;
import org.junit.Test;

import java.util.TreeMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class FilePageHelperTest {
    FilePageHelper helper = new FilePageHelper();

    @Test
    public void shouldBuildPageMap() {
        final TreeMap<String, SetUniqueList> map = helper.buildFilePagesMap();
        assertThat(map.get("00_test.pdf").size(), equalTo(10));
    }
}