import org.jdom2.Document;
import org.junit.Assert;
import org.junit.Test;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class DeserializerTest {
    private Serializer serializer = new Serializer();
    private Deserializer deserializer = new Deserializer();

    @Test
    public void deserializeClassA() throws Exception {
        Document doc = serializer.serialize(new ClassA());
        Object obj = deserializer.deserialize(doc);

        Assert.assertEquals("ClassA", obj.getClass().getName());

        Field field = obj.getClass().getDeclaredFields()[0];
        field.setAccessible(true);
        Assert.assertEquals(0, field.get(obj));

        field = obj.getClass().getDeclaredFields()[1];
        field.setAccessible(true);
        Assert.assertEquals(false, field.get(obj));
    }

    @Test
    public void deserializeValueClassA() throws Exception {
        Document doc = serializer.serialize(new ClassA(2,true));
        Object obj = deserializer.deserialize(doc);

        Assert.assertEquals("ClassA", obj.getClass().getName());

        Field field = obj.getClass().getDeclaredFields()[0];
        field.setAccessible(true);
        Assert.assertEquals(2, field.get(obj));

        field = obj.getClass().getDeclaredFields()[1];
        field.setAccessible(true);
        Assert.assertEquals(true, field.get(obj));
    }

    @Test
    public void deserializeClassB() throws Exception {
        Document doc = serializer.serialize(new ClassB(new ClassA()));
        Object obj = deserializer.deserialize(doc);

        Assert.assertEquals("ClassB", obj.getClass().getName());

        Field field = obj.getClass().getDeclaredFields()[0];
        field.setAccessible(true);
        Object fieldObject = field.get(obj);
        Assert.assertEquals("ClassA", fieldObject.getClass().getName());

        Field classAFields = fieldObject.getClass().getDeclaredFields()[0];
        classAFields.setAccessible(true);
        Assert.assertEquals(0, classAFields.get(fieldObject));

        classAFields = fieldObject.getClass().getDeclaredFields()[1];
        classAFields.setAccessible(true);
        Assert.assertEquals(false, classAFields.get(fieldObject));
    }

    @Test
    public void deserializeClassC() throws Exception {
        int[] intArray = {1,2,3};
        Document doc = serializer.serialize(new ClassC(intArray));
        Object obj = deserializer.deserialize(doc);

        Assert.assertEquals("ClassC", obj.getClass().getName());

        Field field = obj.getClass().getDeclaredFields()[0];
        field.setAccessible(true);
        Object fieldObject = field.get(obj);
        Assert.assertEquals(1, Array.get(fieldObject,0));
        Assert.assertEquals(2, Array.get(fieldObject,1));
        Assert.assertEquals(3, Array.get(fieldObject,2));
    }

    @Test
    public void deserializeClassD() throws Exception {
        Object[] objArray = {new ClassA(), new ClassA(2,true)};
        Document doc = serializer.serialize(new ClassD(objArray));
        Object obj = deserializer.deserialize(doc);

        Assert.assertEquals("ClassD", obj.getClass().getName());

        Field field = obj.getClass().getDeclaredFields()[0];
        field.setAccessible(true);
        Object fieldObject = field.get(obj);

        Object arrayObject = Array.get(fieldObject,0);
        Assert.assertEquals("ClassA", arrayObject.getClass().getName());
        Field classAFields = arrayObject.getClass().getDeclaredFields()[0];
        classAFields.setAccessible(true);
        Assert.assertEquals(0, classAFields.get(arrayObject));

        classAFields = arrayObject.getClass().getDeclaredFields()[1];
        classAFields.setAccessible(true);
        Assert.assertEquals(false, classAFields.get(arrayObject));

        arrayObject = Array.get(fieldObject,1);
        Assert.assertEquals("ClassA", arrayObject.getClass().getName());
        classAFields = arrayObject.getClass().getDeclaredFields()[0];
        classAFields.setAccessible(true);
        Assert.assertEquals(2, classAFields.get(arrayObject));

        classAFields = arrayObject.getClass().getDeclaredFields()[1];
        classAFields.setAccessible(true);
        Assert.assertEquals(true, classAFields.get(arrayObject));
    }

    @Test
    public void deserializeClassE() throws Exception {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new ClassA());
        arrayList.add(new ClassA(2,true));
        Document doc = serializer.serialize(new ClassE(arrayList));
        Object obj = deserializer.deserialize(doc);

        Assert.assertEquals("ClassE", obj.getClass().getName());
    }
}