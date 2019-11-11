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

    public Object deserialize(Document document) throws Exception {
        ihm = new HashMap();
        Object objInstance = null;

        Element rootElement = document.getRootElement();
        List<Element> objectList = rootElement.getChildren();
        for (Element object : objectList) {
            System.out.println(object.getAttributeValue("class"));
            Class classObj = Class.forName(object.getAttributeValue("class"));

            if (!classObj.isArray()) {
                Constructor constructor = classObj.getDeclaredConstructor(null);
                objInstance = constructor.newInstance(null);
            }
            else {
                objInstance = Array.newInstance(classObj.getComponentType(),Integer.parseInt(object.getAttributeValue("length")));
            }

            ihm.put(object.getAttributeValue("id"), objInstance);

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

        return objInstance;
    }

    public Object parseFieldValue(Class fieldClass, Element field) {
        if (fieldClass.equals(int.class)) {
            return Integer.parseInt(field.getText());
        }
        else if (fieldClass.equals(boolean.class)) {
            return Boolean.parseBoolean(field.getText());
        }
        return null;
    }

    public static void main(String args[]) throws Exception {
        Serializer serializer = new Serializer();
        ArrayList<Object> objectArrayList = new ArrayList<>();

        ClassA classA = new ClassA();
        //ClassC classC = new ClassC(3);
        //objectArrayList.add(classC);
        //objectArrayList.add(new ClassA());
        //objectArrayList.add(new ClassB(new ClassA(1,true)));
        ArrayList arrayList = new ArrayList();
        arrayList.add(new ClassA());
        ClassE classE = new ClassE(arrayList);

        objectArrayList.add(classE);
        //System.out.println(arrayList.size());
        Document doc = serializer.serialize(objectArrayList);
        Deserializer deserializer = new Deserializer();
        Object obj = deserializer.deserialize(doc);
        new Inspector().inspect(obj, true);
    }
}