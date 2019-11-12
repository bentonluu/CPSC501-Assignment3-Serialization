import org.jdom2.Document;
import org.jdom2.Element;
import org.junit.Assert;
import java.util.ArrayList;
import org.junit.Test;

public class SerializerTest {
    private Serializer serializer = new Serializer();

    @Test
    public void serializeClassA() {
        Document doc = serializer.serialize(new ClassA());

        Element rootElement = doc.getRootElement();
        Assert.assertEquals("serialized", rootElement.getName());

        Element objectElementA = rootElement.getChildren().get(0);
        Assert.assertEquals("ClassA", objectElementA.getAttributeValue("class"));
        Assert.assertEquals("0", objectElementA.getAttributeValue("id"));

        Assert.assertEquals("a", objectElementA.getChildren().get(0).getAttributeValue("name"));
        Assert.assertEquals("ClassA", objectElementA.getChildren().get(0).getAttributeValue("declaringclass"));
        Assert.assertEquals("b", objectElementA.getChildren().get(1).getAttributeValue("name"));
        Assert.assertEquals("ClassA", objectElementA.getChildren().get(1).getAttributeValue("declaringclass"));

        Assert.assertEquals("value", objectElementA.getChildren().get(0).getChildren().get(0).getName());
        Assert.assertEquals("0", objectElementA.getChildren().get(0).getChildren().get(0).getText());
        Assert.assertEquals("false", objectElementA.getChildren().get(1).getChildren().get(0).getText());
    }

    @Test
    public void serializeValueClassA() {
        Document doc = serializer.serialize(new ClassA(3,true));

        Element rootElement = doc.getRootElement();
        Assert.assertEquals("serialized", rootElement.getName());

        Element objectElementA = rootElement.getChildren().get(0);
        Assert.assertEquals("ClassA", objectElementA.getAttributeValue("class"));
        Assert.assertEquals("0", objectElementA.getAttributeValue("id"));

        Assert.assertEquals("a", objectElementA.getChildren().get(0).getAttributeValue("name"));
        Assert.assertEquals("ClassA", objectElementA.getChildren().get(0).getAttributeValue("declaringclass"));
        Assert.assertEquals("b", objectElementA.getChildren().get(1).getAttributeValue("name"));
        Assert.assertEquals("ClassA", objectElementA.getChildren().get(1).getAttributeValue("declaringclass"));

        Assert.assertEquals("value", objectElementA.getChildren().get(0).getChildren().get(0).getName());
        Assert.assertEquals("3", objectElementA.getChildren().get(0).getChildren().get(0).getText());
        Assert.assertEquals("true", objectElementA.getChildren().get(1).getChildren().get(0).getText());
    }

    @Test
    public void serializeClassB() {
        Document doc = serializer.serialize(new ClassB(new ClassA()));

        Element rootElement = doc.getRootElement();
        Assert.assertEquals("serialized", rootElement.getName());

        Element objectElementA = rootElement.getChildren().get(0);
        Assert.assertEquals("ClassA", objectElementA.getAttributeValue("class"));
        Assert.assertEquals("1", objectElementA.getAttributeValue("id"));

        Assert.assertEquals("a", objectElementA.getChildren().get(0).getAttributeValue("name"));
        Assert.assertEquals("ClassA", objectElementA.getChildren().get(0).getAttributeValue("declaringclass"));
        Assert.assertEquals("b", objectElementA.getChildren().get(1).getAttributeValue("name"));
        Assert.assertEquals("ClassA", objectElementA.getChildren().get(1).getAttributeValue("declaringclass"));

        Assert.assertEquals("0", objectElementA.getChildren().get(0).getChildren().get(0).getText());
        Assert.assertEquals("false", objectElementA.getChildren().get(1).getChildren().get(0).getText());

        Element objectElementB = rootElement.getChildren().get(1);
        Assert.assertEquals("ClassB", objectElementB.getAttributeValue("class"));
        Assert.assertEquals("0", objectElementB.getAttributeValue("id"));

        Assert.assertEquals("classA", objectElementB.getChildren().get(0).getAttributeValue("name"));
        Assert.assertEquals("ClassB", objectElementB.getChildren().get(0).getAttributeValue("declaringclass"));

        Assert.assertEquals("reference", objectElementB.getChildren().get(0).getChildren().get(0).getName());
        Assert.assertEquals("1", objectElementB.getChildren().get(0).getChildren().get(0).getText());
    }

    @Test
    public void serializeClassC() {
        int[] intArray = {1,2,3};
        Document doc = serializer.serialize(new ClassC(intArray));

        Element rootElement = doc.getRootElement();
        Assert.assertEquals("serialized", rootElement.getName());

        Element objectArray = rootElement.getChildren().get(0);
        Assert.assertEquals("[I", objectArray.getAttributeValue("class"));
        Assert.assertEquals("1", objectArray.getAttributeValue("id"));
        Assert.assertEquals("3", objectArray.getAttributeValue("length"));

        Assert.assertEquals("value", objectArray.getChildren().get(0).getName());
        Assert.assertEquals("1", objectArray.getChildren().get(0).getText());
        Assert.assertEquals("2", objectArray.getChildren().get(1).getText());
        Assert.assertEquals("3", objectArray.getChildren().get(2).getText());


        Element objectElementC = rootElement.getChildren().get(1);
        Assert.assertEquals("ClassC", objectElementC.getAttributeValue("class"));
        Assert.assertEquals("0", objectElementC.getAttributeValue("id"));

        Assert.assertEquals("intArray", objectElementC.getChildren().get(0).getAttributeValue("name"));
        Assert.assertEquals("ClassC", objectElementC.getChildren().get(0).getAttributeValue("declaringclass"));

        Assert.assertEquals("reference", objectElementC.getChildren().get(0).getChildren().get(0).getName());
        Assert.assertEquals("1", objectElementC.getChildren().get(0).getChildren().get(0).getText());
    }

    @Test
    public void serializeClassD() {
        Object[] objArray = {new ClassA(), new ClassA(2,true)};
        Document doc = serializer.serialize(new ClassD(objArray));

        Element rootElement = doc.getRootElement();
        Assert.assertEquals("serialized", rootElement.getName());

        Element objectElementA1 = rootElement.getChildren().get(0);
        Assert.assertEquals("ClassA", objectElementA1.getAttributeValue("class"));
        Assert.assertEquals("2", objectElementA1.getAttributeValue("id"));

        Assert.assertEquals("a", objectElementA1.getChildren().get(0).getAttributeValue("name"));
        Assert.assertEquals("ClassA", objectElementA1.getChildren().get(0).getAttributeValue("declaringclass"));
        Assert.assertEquals("b", objectElementA1.getChildren().get(1).getAttributeValue("name"));
        Assert.assertEquals("ClassA", objectElementA1.getChildren().get(1).getAttributeValue("declaringclass"));

        Assert.assertEquals("0", objectElementA1.getChildren().get(0).getChildren().get(0).getText());
        Assert.assertEquals("false", objectElementA1.getChildren().get(1).getChildren().get(0).getText());

        Element objectElementA2 = rootElement.getChildren().get(1);
        Assert.assertEquals("ClassA", objectElementA2.getAttributeValue("class"));
        Assert.assertEquals("3", objectElementA2.getAttributeValue("id"));

        Assert.assertEquals("a", objectElementA2.getChildren().get(0).getAttributeValue("name"));
        Assert.assertEquals("ClassA", objectElementA2.getChildren().get(0).getAttributeValue("declaringclass"));
        Assert.assertEquals("b", objectElementA2.getChildren().get(1).getAttributeValue("name"));
        Assert.assertEquals("ClassA", objectElementA2.getChildren().get(1).getAttributeValue("declaringclass"));

        Assert.assertEquals("2", objectElementA2.getChildren().get(0).getChildren().get(0).getText());
        Assert.assertEquals("true", objectElementA2.getChildren().get(1).getChildren().get(0).getText());

        Element objectArray = rootElement.getChildren().get(2);
        Assert.assertEquals("[Ljava.lang.Object;", objectArray.getAttributeValue("class"));
        Assert.assertEquals("1", objectArray.getAttributeValue("id"));
        Assert.assertEquals("2", objectArray.getAttributeValue("length"));

        Assert.assertEquals("reference", objectArray.getChildren().get(0).getName());
        Assert.assertEquals("2", objectArray.getChildren().get(0).getText());
        Assert.assertEquals("3", objectArray.getChildren().get(1).getText());

        Element objectElementC = rootElement.getChildren().get(3);
        Assert.assertEquals("ClassD", objectElementC.getAttributeValue("class"));
        Assert.assertEquals("0", objectElementC.getAttributeValue("id"));

        Assert.assertEquals("objectRefArray", objectElementC.getChildren().get(0).getAttributeValue("name"));
        Assert.assertEquals("ClassD", objectElementC.getChildren().get(0).getAttributeValue("declaringclass"));

        Assert.assertEquals("reference", objectElementC.getChildren().get(0).getChildren().get(0).getName());
        Assert.assertEquals("1", objectElementC.getChildren().get(0).getChildren().get(0).getText());
    }

    @Test
    public void serializeClassE() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new ClassA());
        arrayList.add(new ClassA(2,true));
        Document doc = serializer.serialize(new ClassE(arrayList));

        Element rootElement = doc.getRootElement();
        Assert.assertEquals("serialized", rootElement.getName());

        Element objectElement1 = rootElement.getChildren().get(0);
        Assert.assertEquals("[Ljava.lang.Object;", objectElement1.getAttributeValue("class"));
        Assert.assertEquals("2", objectElement1.getAttributeValue("id"));
        Assert.assertEquals("0", objectElement1.getAttributeValue("length"));

        Element objectElement2 = rootElement.getChildren().get(1);
        Assert.assertEquals("[Ljava.lang.Object;", objectElement2.getAttributeValue("class"));
        Assert.assertEquals("3", objectElement2.getAttributeValue("id"));
        Assert.assertEquals("0", objectElement2.getAttributeValue("length"));

        Element objectElement3 = rootElement.getChildren().get(2);
        Assert.assertEquals("ClassA", objectElement3.getAttributeValue("class"));
        Assert.assertEquals("5", objectElement3.getAttributeValue("id"));

        Assert.assertEquals("a", objectElement3.getChildren().get(0).getAttributeValue("name"));
        Assert.assertEquals("ClassA", objectElement3.getChildren().get(0).getAttributeValue("declaringclass"));
        Assert.assertEquals("b", objectElement3.getChildren().get(1).getAttributeValue("name"));
        Assert.assertEquals("ClassA", objectElement3.getChildren().get(1).getAttributeValue("declaringclass"));

        Assert.assertEquals("0", objectElement3.getChildren().get(0).getChildren().get(0).getText());
        Assert.assertEquals("false", objectElement3.getChildren().get(1).getChildren().get(0).getText());

        Element objectElement4 = rootElement.getChildren().get(3);
        Assert.assertEquals("ClassA", objectElement4.getAttributeValue("class"));
        Assert.assertEquals("6", objectElement4.getAttributeValue("id"));

        Assert.assertEquals("a", objectElement4.getChildren().get(0).getAttributeValue("name"));
        Assert.assertEquals("ClassA", objectElement4.getChildren().get(0).getAttributeValue("declaringclass"));
        Assert.assertEquals("b", objectElement4.getChildren().get(1).getAttributeValue("name"));
        Assert.assertEquals("ClassA", objectElement4.getChildren().get(1).getAttributeValue("declaringclass"));

        Assert.assertEquals("2", objectElement4.getChildren().get(0).getChildren().get(0).getText());
        Assert.assertEquals("true", objectElement4.getChildren().get(1).getChildren().get(0).getText());

        Element objectElement5 = rootElement.getChildren().get(4);
        Assert.assertEquals("[Ljava.lang.Object;", objectElement5.getAttributeValue("class"));
        Assert.assertEquals("4", objectElement5.getAttributeValue("id"));
        Assert.assertEquals("10", objectElement5.getAttributeValue("length"));

        Element objectElement6 = rootElement.getChildren().get(5);
        Assert.assertEquals("java.util.ArrayList", objectElement6.getAttributeValue("class"));
        Assert.assertEquals("1", objectElement6.getAttributeValue("id"));

        Element objectElement7 = rootElement.getChildren().get(6);
        Assert.assertEquals("ClassE", objectElement7.getAttributeValue("class"));
        Assert.assertEquals("0", objectElement7.getAttributeValue("id"));

        Assert.assertEquals("reference", objectElement7.getChildren().get(0).getChildren().get(0).getName());
        Assert.assertEquals("1", objectElement7.getChildren().get(0).getChildren().get(0).getText());
    }
}