import java.net.*;
import org.jdom2.input.SAXBuilder;
import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class Receiver {
    private Socket socket;
    private ServerSocket server;

    public Receiver(int port) {
        try {
            InetAddress address = InetAddress.getLocalHost();
            server = new ServerSocket(port);

            System.out.println("Server opened at: " + address.getHostAddress());
            System.out.println("Server started");

            socket = server.accept();
            System.out.println("Client accepted");

            SAXBuilder saxBuilder = new SAXBuilder();
            Document receivedDoc = saxBuilder.build(socket.getInputStream());

            Deserializer deserializer = new Deserializer();
            deserializer.deserialize(receivedDoc);

            XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
            //System.out.println(xmlOutputter.outputString(receivedDoc));

            System.out.println("Received from client: "+ receivedDoc.getRootElement().toString());
            server.close();
            socket.close();

            System.out.println("Receiver side closed");
        }
        catch (Exception e) {
            System.out.println("Error on receiver side" + e.toString());
        }
    }

    public static void main(String args[]) {
        //Receiver receiver = new Receiver(5000);


    }
}