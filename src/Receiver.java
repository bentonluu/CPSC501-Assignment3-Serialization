import java.io.FileOutputStream;
import java.net.*;
import java.util.Scanner;
import org.jdom2.input.SAXBuilder;
import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class Receiver {
    private Socket socket;
    private ServerSocket server;

    public static void main(String args[]) {
        Scanner input = new Scanner(System.in);
        int port;

        while (true) {
            try {
                System.out.println("Enter port:");
                port = input.nextInt();
                System.out.println("...starting Receiver");
                Receiver receiver = new Receiver(port);
            }
            catch (Exception e) {
                System.out.println("* Invalid input; enter a valid port number *\n");
                input.nextLine();
            }
        }
    }

    public Receiver(int port) {
        try {
            InetAddress address = InetAddress.getLocalHost();
            server = new ServerSocket(port);

            System.out.println("Server opened at: " + address.getHostAddress());
            System.out.println("Server started");

            while (true) {
                socket = server.accept();

                SAXBuilder saxBuilder = new SAXBuilder();
                Document receivedDoc = saxBuilder.build(socket.getInputStream());

                XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
                xmlOutputter.output(receivedDoc, new FileOutputStream("receivedFile.xml"));
                System.out.println(xmlOutputter.outputString(receivedDoc));

                Deserializer deserializer = new Deserializer();
                Object deserializeObj = deserializer.deserialize(receivedDoc);

                new Inspector().inspect(deserializeObj, true);
            }
        }
        catch (Exception e) {
            System.out.println("Error on receiver side" + e.toString());
        }
    }
}