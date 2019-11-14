import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import org.jdom2.*;

public class Serializer {
    Document document = new Document();
    IdentityHashMap<Object,Integer> ihm;
    int uniqueID;

    public Document serialize(Object object) {
        Element rootXML = new Element("serialized");
        document.setRootElement(rootXML);

        ihm = new IdentityHashMap();
        uniqueID = 0;

        identityMapTracker(object);
        serializeObjectXML(object, rootXML);

        return document;
    }

    public void serializeObjectXML(Object object, Element rootXML) {
        try {
            // Object attributes
            Class classObj = object.getClass();

            Element objXML = new Element("object");
            objXML.setAttribute("class", classObj.getName());
            objXML.setAttribute("id", String.valueOf(identityMapTracker(object)));

            // Field attributes
            Field[] fieldList = classObj.getDeclaredFields();

            if (fieldList.length != 0) {
                for (Field field : fieldList) {
                    field.setAccessible(true);

                    Element fieldXML = new Element("field");
                    fieldXML.setAttribute("name", field.getName());
                    fieldXML.setAttribute("declaringclass", field.getDeclaringClass().getName());
                    objXML.addContent(fieldXML);

                    Object fieldValue = null;
                    try {
                        fieldValue = field.get(object);
                    }
                    catch (IllegalAccessException e) {
                        System.out.println("Cannot access field");
                    }

                    if (fieldValue == null) {
                        fieldXML.addContent(new Element("value").setText("null"));
                    }
                    else if (field.getType().isPrimitive()) {
                        fieldXML.addContent(new Element("value").setText(fieldValue.toString()));
                    }
                    else if (field.getType().isArray()) {
                        Element referenceXML = new Element("reference");
                        referenceXML.setText(String.valueOf(identityMapTracker(fieldValue)));
                        fieldXML.addContent(referenceXML);

                        arrayObjectXML(field.getType(),fieldValue,rootXML);
                    }
                    else {
                        Element referenceXML = new Element("reference");

                        referenceXML.setText(String.valueOf(identityMapTracker(fieldValue)));
                        fieldXML.addContent(referenceXML);

                        serializeObjectXML(fieldValue,rootXML);
                    }
                }
            }
            rootXML.addContent(objXML);
        }
        catch (Exception e) {
            System.out.println("Error" + e.toString());
        }
    }

    public void arrayObjectXML(Class arrayClass, Object arrayObj, Element rootXML) {
        int arrayLength = Array.getLength(arrayObj);

        Element objXML = new Element("object");
        objXML.setAttribute("class",arrayClass.getName());
        objXML.setAttribute("id",String.valueOf(identityMapTracker(arrayObj)));
        objXML.setAttribute("length", String.valueOf(arrayLength));

        for (int i = 0; i < arrayLength; i++) {
            Object arrayItem = Array.get(arrayObj, i);

            if (arrayItem == null) {
                objXML.addContent(new Element("value").setText("null"));
            }
            else if (arrayClass.getComponentType().isPrimitive()) {
                objXML.addContent(new Element("value").setText(arrayItem.toString()));
            }
            else if (arrayClass.getComponentType().isArray()) {
                arrayObjectXML(arrayItem.getClass(),arrayItem,rootXML);
            }
            else {
                Element referenceXML = new Element("reference");
                referenceXML.setText(String.valueOf(identityMapTracker(arrayItem)));
                objXML.addContent(referenceXML);
                serializeObjectXML(arrayItem,rootXML);
            }
        }

        rootXML.addContent(objXML);
    }

    public int identityMapTracker(Object object) {
        int id = uniqueID;

        if (ihm.containsKey(object)) {
            id = ihm.get(object);
        }
        else {
            ihm.put(object, uniqueID);
            uniqueID = uniqueID + 1;
        }

        return id;
    }
}