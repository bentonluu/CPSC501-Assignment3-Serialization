import org.jdom2.Document;
import org.jdom2.Element;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Deserializer {
    HashMap<String,Object> ihm;

    // Deserializes the document received and creates either an object or array instance with it's fields filled in
    public Object deserialize(Document document) throws Exception {
        ihm = new HashMap();

        Element rootElement = document.getRootElement();
        List<Element> objectList = rootElement.getChildren();
        setObjFieldValue(objectList);

        return ihm.get("0");
    }

    // Sets the fields of the created object or array with the proper value or reference
    public void setObjFieldValue(List<Element> objectList) throws Exception {
        for (Element object : objectList) {
            Object objInstance = initializeObject(object);

            List<Element> objFieldList = object.getChildren();
            for (int i = 0; i < objFieldList.size(); i++) {
                if (objInstance.getClass().isArray()) {
                    if (objFieldList.get(i).getName().equals("value")) {
                        Array.set(objInstance, i, parseFieldValue(objInstance.getClass().getComponentType(), objFieldList.get(i)));
                    }
                    else if (objFieldList.get(i).getName().equals("reference")) {
                        Array.set(objInstance, i, ihm.get(objFieldList.get(i).getText()));
                    }
                    else if (objFieldList.get(i).getText().equals("null")) {
                        Array.set(objInstance, i, null);
                    }
                }
                else {
                    List<Element> fieldElementList = objFieldList.get(i).getChildren();

                    for (Element fieldElement : fieldElementList) {
                        Class classObj = Class.forName(object.getAttributeValue("class"));
                        Field objField = classObj.getDeclaredField(objFieldList.get(i).getAttributeValue("name"));
                        objField.setAccessible(true);

                        if (Modifier.isFinal(objField.getModifiers())) {
                            continue;
                        }
                        else if (fieldElement.getName().equals("value")) {
                            objField.set(objInstance, parseFieldValue(objField.getType(), fieldElement));
                        }
                        else if (fieldElement.getName().equals("reference")) {
                            objField.set(objInstance, ihm.get(fieldElement.getText()));
                        }
                        else if (fieldElement.getText().equals("null")) {
                            objField.set(objInstance, null);
                        }
                    }
                }
            }
        }
    }

    // Creates either an object or array instance depending on the object Element
    public Object initializeObject(Element object) throws Exception {
        Object objInstance;

        Class classObj = Class.forName(object.getAttributeValue("class"));

        if (!classObj.isArray()) {
            Constructor constructor = classObj.getDeclaredConstructor(null);
            objInstance = constructor.newInstance(null);
        }
        else {
            objInstance = Array.newInstance(classObj.getComponentType(),Integer.parseInt(object.getAttributeValue("length")));
        }

        ihm.put(object.getAttributeValue("id"), objInstance);

        return objInstance;
    }

    // Converts the element value into the specific class of the field
    public Object parseFieldValue(Class fieldClass, Element field) {
        if (fieldClass.equals(int.class)) {
            return Integer.parseInt(field.getText());
        }
        else if (fieldClass.equals(boolean.class)) {
            return Boolean.parseBoolean(field.getText());
        }
        else if (fieldClass.equals(byte.class)) {
            return Byte.parseByte(field.getText());
        }
        else if (fieldClass.equals(short.class)) {
            return Short.parseShort(field.getText());
        }
        else if (fieldClass.equals(double.class)) {
            return Double.parseDouble(field.getText());
        }
        else if (fieldClass.equals(long.class)) {
            return Long.parseLong(field.getText());
        }
        else if (fieldClass.equals(float.class)) {
            return Float.parseFloat(field.getText());
        }
        return null;
    }
}