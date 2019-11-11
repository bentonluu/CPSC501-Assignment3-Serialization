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

        ClassA classA = new ClassA(1,true);
        //serializedDoc = serializer.serialize(classA);
        //Sender sender = new Sender("127.0.0.1", 5000, serializedDoc);

        String option = "";
        while (!option.equals("3")) {
            System.out.println("--- MAIN MENU ---");
            System.out.println("1 - Create Object(s)");
            System.out.println("2 - XML Document Options");
            System.out.println("3 - End program");

            if (input.hasNext()) {
                option = input.nextLine();
            }

            if (option.equals("1")) {
                ObjectCreator objectCreator = new ObjectCreator();
                objectSerializeList = objectCreator.objectCreatorMenu();
            }
            else if (option.equals("2")) {
                serializedDoc = serializer.serialize(objectSerializeList);

                while (!option.equals("4")) {
                    System.out.println("--- XML DOCUMENT OPTIONS MENU ---");
                    System.out.println("1 - Store XML Document");
                    System.out.println("2 - Print XML Document");
                    System.out.println("3 - Send XML Document");
                    System.out.println("4 - Exit Menu");

                    if (input.hasNext()) {
                        option = input.nextLine();
                    }

                    try {
                        if (option.equals("1")) {
                            String fileName = "fileSend.xml";
                            xmlOutputter.output(serializedDoc, new FileOutputStream(fileName));
                        }
                        else if (option.equals("2")) {
                            xmlOutputter.output(serializedDoc,System.out);
                            System.out.println("");
                        }
                        else if (option.equals("3")) {
                            System.out.println("Enter destination address:");
                            String destinationIP = input.nextLine();
                            System.out.println("Enter port:");
                            int port = input.nextInt();

                            sendFile(destinationIP, port, serializedDoc);
                        }
                        else if (!option.equals("4")) {
                            System.out.println("* Invalid option selected *\n");
                        }
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            else {
                if (!option.equals("3")) {
                    System.out.println("* Invalid option selected *\n");
                }
            }
        }
    }

    public static void sendFile(String ip, int port, Document document) {
        Socket socket = null;
        OutputStream outputStream = null;

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
            System.out.println("Sender side closed");
        }
        catch (Exception e) {
            System.out.println("Error on sender side");
        }
    }
}