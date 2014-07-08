package tr.com.aliok.osmani.annotator.annotation;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import tr.com.aliok.osmani.annotator.model.Annotation;

import java.io.Serializable;
import java.util.TreeSet;

/**
 * @author Ali Ok (ali.ok@apache.org)
 */
public class AnnotationJSONFormatter implements Serializable {
    public String getJSON(TreeSet<Annotation> annotations, Annotation current) {
        /*
        expected result:

        [
            {
                id: 'asdasdasd',
                'x': 10,
                'y': 20,
                'w': 100,
                'h': 200,
                'textData': {
                    'tr_latin': 'Kolay',
                    'tr_arabic': ' الف'
                    'tr_latin2': 'Kulay',
                    'description': 'Aciklama',
                },
                'selected' : true
            },
            {
                id: 'asdasdasd2',
                'x': 20,
                'y': 30,
                'w': 400,
                'h': 400,
                'textData': {
                    'tr_latin': 'Kolay2',
                    'tr_arabic': '2 الف',
                    'tr_latin2': 'Kulay2',
                    'description': 'Aciklama2',
                }
            }
        ]
         */

        final Gson gson = new Gson();

        final JsonArray jsonArray = new JsonArray();

        for (Annotation annotation : annotations) {
            final JsonObject textData = new JsonObject();
            textData.add("tr_arabic", new JsonPrimitive(Strings.nullToEmpty(annotation.getTr_arabic())));
            textData.add("tr_latin", new JsonPrimitive(Strings.nullToEmpty(annotation.getTr_latin())));
            textData.add("tr_latin2", new JsonPrimitive(Strings.nullToEmpty(annotation.getTr_latin2())));
            textData.add("description", new JsonPrimitive(Strings.nullToEmpty(annotation.getDescription())));

            final JsonObject jsonObject = new JsonObject();
            jsonObject.add("id", new JsonPrimitive(annotation.getAnnotationId()));
            jsonObject.add("x", new JsonPrimitive(annotation.getX()));
            jsonObject.add("y", new JsonPrimitive(annotation.getY()));
            jsonObject.add("w", new JsonPrimitive(annotation.getW()));
            jsonObject.add("h", new JsonPrimitive(annotation.getH()));
            jsonObject.add("textData", textData);
            if (current != null)
                jsonObject.add("selected", new JsonPrimitive(annotation.getAnnotationId().equals(current.getAnnotationId())));

            jsonArray.add(jsonObject);
        }


        return gson.toJson(jsonArray);
    }

}
