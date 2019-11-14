import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;
import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class Sender {
    public static void main(String args[]) {
        Scanner input = new Scanner(System.in);
        ArrayList<Object> objectSerializeList = new ArrayList<>();
        Serializer serializer = new Serializer();
        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
        Document serializedDoc;

        String option = "";
        while (!option.equals("6")) {
            System.out.println("--- MAIN MENU ---");
            System.out.println("1 - Create Object(s)");
            System.out.println("2 - List Object(s)");
            System.out.println("3 - Send XML Document");
            System.out.println("4 - Print XML Document");
            System.out.println("5 - Store XML Document");
            System.out.println("6 - End program");

            if (input.hasNext()) {
                option = input.nextLine();
            }

            if (option.equals("1")) {
                ObjectCreator objectCreator = new ObjectCreator();
                objectSerializeList = objectCreator.objectCreatorMenu();
            }
            else if (option.equals("2")) {
                for (Object obj : objectSerializeList) {
                    System.out.println(obj);
                }
            }
            else if (option.equals("3")) {
                try {
                    boolean sentFlag = true;
                    while (sentFlag) {
                        try {
                            System.out.println("Enter destination address:");
                            String destinationIP = input.nextLine();
                            System.out.println("Enter port:");
                            int port = input.nextInt();

                            for (Object obj : objectSerializeList) {
                                serializedDoc = serializer.serialize(obj);
                                sendFile(destinationIP, port, serializedDoc);
                            }
                            sentFlag = false;
                        }
                        catch (Exception e) {
                            System.out.println("* Invalid input *\n");
                            input.nextLine();
                        }
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (option.equals("4")) {
                for (Object obj : objectSerializeList) {
                    try {
                        serializedDoc = serializer.serialize(obj);
                        xmlOutputter.output(serializedDoc,System.out);
                        System.out.println("");
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            else if (option.equals("5")) {
                for (Object obj : objectSerializeList) {
                    try {
                        serializedDoc = serializer.serialize(obj);
                        String fileName = "";
                        if (obj.getClass().getName().equals("ClassA")) {
                            fileName = "sentFileClassA.xml";
                        }
                        else if (obj.getClass().getName().equals("ClassB")) {
                            fileName = "sentFileClassB.xml";
                        }
                        else if (obj.getClass().getName().equals("ClassC")) {
                            fileName = "sentFileClassC.xml";
                        }
                        else if (obj.getClass().getName().equals("ClassD")) {
                            fileName = "sentFileClassD.xml";
                        }
                        else if (obj.getClass().getName().equals("ClassE")) {
                            fileName = "sentFileClassE.xml";
                        }

                        xmlOutputter.output(serializedDoc, new FileOutputStream(fileName));
                        System.out.println(fileName + " has been stored");
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            else {
                if (!option.equals("6")) {
                    System.out.println("* Invalid option selected *\n");
                }
            }
        }
    }

    public static void sendFile(String ip, int port, Document document) {
        Socket socket;
        OutputStream outputStream;

        try {
            socket = new Socket(ip, port);

            if (socket.isConnected()) {
                System.out.println("Connected");
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
            xmlOutputter.output(document,baos);

            outputStream = new DataOutputStream(socket.getOutputStream());
            outputStream.write(baos.toByteArray());
            outputStream.flush();
            System.out.println("Message Sent");

            socket.close();
            outputStream.close();
        }
        catch (Exception e) {
            System.out.println("Error on sender side" + e.toString());
        }
    }
}