import java.util.Scanner;
import java.util.ArrayList;

public class ObjectCreator {
    Scanner input = new Scanner(System.in);

    public ArrayList<Object>  objectCreatorMenu() {
        ArrayList<Object> serializeList = new ArrayList();
        String objOption = "";

        while (!objOption.equals("6")) {
            System.out.println("--- CREATE OBJECT(S) MENU ---");
            System.out.println("1 - Create Simple Object");
            System.out.println("2 - Create Reference Object");
            System.out.println("3 - Create Array Object with Primitives");
            System.out.println("4 - Create Array Object with Object References");
            System.out.println("5 - Create Java Collection Object");
            System.out.println("6 - Exit Menu");

            if(input.hasNext()) {
                objOption = input.nextLine();
            }

            if (objOption.equals("1")) {
                serializeList.add(createSimpleObject());
            }
            else if (objOption.equals("2")) {
                serializeList.add(createRefObject());
            }
            else if (objOption.equals("3")) {
                serializeList.add(createArrayObject());
            }
            else if (objOption.equals("4")) {
                serializeList.add(createRefArrayObject());
            }
            else if (objOption.equals("5")) {
                serializeList.add(createCollectionObject());
            }
            else {
                if (!objOption.equals("6")) {
                    System.out.println("* Invalid option selected *\n");
                }
            }
        }

        return serializeList;
    }

    public Object createSimpleObject() {
        System.out.println("... creating Simple Object");
        boolean createFlag = false;

        while (!createFlag)
        {
            try {
                System.out.println("Enter an integer:");
                int a = input.nextInt();
                System.out.println("Enter a boolean:");
                boolean b = input.nextBoolean();

                createFlag = true;
                return new ClassA(a,b);
            }
            catch (Exception e){
                System.out.println("Invalid input");
                input.nextLine();
            }
        }

        return null;
    }

    public Object createRefObject() {
        System.out.println("... creating Reference Object\n");
        return new ClassB((ClassA) createSimpleObject());
    }

    public Object createArrayObject() {
        System.out.println("... creating Array Object\n");
        boolean createFlag = false;

        while (!createFlag) {
            try {
                System.out.println("Enter array values (integer only): (eg., 1,2,3)");
                String arrayInput = input.nextLine();
                String[] arrayValues = arrayInput.split(",");
                int[] intArray = new int[arrayValues.length];

                for (int i = 0; i < intArray.length; i++) {
                    intArray[i] = Integer.parseInt(arrayValues[i]);
                }

                createFlag = true;
                return new ClassC(intArray);
            }
            catch (Exception e) {
                System.out.println("Invalid input; ensure values are integers and separated by ','");
                input.nextLine();
            }
        }

        return null;
    }

    public Object createRefArrayObject() {
        System.out.println("... creating Reference Array Object\n");
        boolean createFlag = false;

        while (!createFlag) {
            try {
                System.out.println("Enter the length of the array:");
                int length = input.nextInt();
                Object[] arrayClassA = new Object[length];

                for (int i = 0; i < length; i++) {
                    arrayClassA[i] = createSimpleObject();
                }

                createFlag = true;
                return new ClassD(arrayClassA);
            }
            catch (Exception e) {
                System.out.println("Invalid input; length of the array must be an integer");
                input.nextLine();
            }
        }

        return null;
    }

    public Object createCollectionObject() {
        System.out.println("... creating Collection Object\n");
        boolean createFlag = false;

        while (!createFlag) {
            try {
                ArrayList arrayList = new ArrayList();

                System.out.print("Add an object?\n");
                System.out.print("1 - YES\n");
                System.out.print("2 - NO\n");

                String option = input.nextLine();
                if (option.equals("1")) {
                    arrayList.add(createSimpleObject());
                    input.nextLine();
                }
                else if (option.equals("2")) {
                    createFlag = true;
                    return new ClassE(arrayList);
                }
                else {
                    System.out.println("Invalid selection");
                    input.nextLine();
                }
            }
            catch (Exception e) {
                System.out.println("Invalid input");
                input.nextLine();
            }
        }

        return null;
    }
}