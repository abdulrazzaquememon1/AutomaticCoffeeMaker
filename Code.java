import java.util.ArrayList;
import java.util.Scanner;

public class MainMethod {
    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        AutomaticCoffeeMaker making = new AutomaticCoffeeMaker();
        ArrayList<User> u1= new ArrayList<>();
        User u = new User("Abdul Razzaque");
        u1.add(u);
        ArrayList<CoffeeRecipe> ct = new ArrayList<>();
        ct.add(new CoffeeRecipe("Espresso", 100, 2, 0));
        ct.add(new CoffeeRecipe("Cappuccino", 150, 2, 100));
        ct.add(new CoffeeRecipe("Latte", 200, 2, 150));
        ct.add(new CoffeeRecipe("Black Coffee", 150, 1, 0));

//        CoffeeCup c;

        String user = "Abdul Razzaque";
        System.out.println("\t\t\t\t\t\t\t\t\tWelcome " + user);
        making.turnOn();

        boolean isRunning = true;
        do {
            int select = making.display();

            switch (select) {
                case 1:
                    making.water.pourwater();
                    break;
                case 2:
                    making.grindingSystem.pourBeans();
                    break;
                case 3:
                    making.milk.pourMilk();
                    break;
                case 4:
                    System.out.println("Choose your coffee type:");
                    for (int i = 0; i < ct.size(); i++) {
                        ct.get(i).showName(i);
                    }
                    System.out.print("Select : ");

                    int coffeeChoice = scan.nextInt();
                    coffeeChoice--;

                    CoffeeRecipe selectedRecipe = null;

                    boolean d = false;

                    if (coffeeChoice < 0) {
                        System.out.println("Invalid Input");
                    } else if (coffeeChoice < ct.size()) {
                        selectedRecipe = ct.get(coffeeChoice);
                        d = true;
                    } else {
                        System.out.println("Invalid input");
                    }
                    if (d) {
                        if (making.grindingSystem.coffeebean < selectedRecipe.beansRequired ||
                                making.water.level < selectedRecipe.waterRequired ||
                                making.milk.level < selectedRecipe.milkRequired) {
                            System.out.println("Please refill ingredients (Water, Beans, or Milk)");
                        } else {
//                            c = new CoffeeCup(selectedRecipe);
//                            if (CoffeeCup.p) {
                            making.startProcess();
                            making.grindingSystem.coffeebean -= selectedRecipe.beansRequired;
                            making.water.level -= selectedRecipe.waterRequired;
                            making.milk.level -= selectedRecipe.milkRequired;
//                            }
                        }
                    }
                    break;
                case 5:
                    making.showInfo();
                    break;
                case 6:
                    System.out.print("Coffee : Water : Beans : Milk\n");
                    for (int i = 0; i < ct.size(); i++) {
                        System.out.println(ct.get(i));
                    }
                    break;
                case 7:
                    System.out.println("To add coffee fill these details");
                    scan.nextLine();
                    System.out.print("Coffee name : ");
                    String name = scan.nextLine();

                    boolean f = true;
                    int water;
                    do {
                        System.out.print("How much water will it require : ");
                        water = scan.nextInt();
                        if (water < 0 || water > 500) {
                            System.out.println("Range 0 - 500 ");
                        } else {
                            f = false;
                        }
                    } while (f);

                    boolean b = true;
                    int beans;
                    do {
                        System.out.print("How much beans will it require : ");
                        beans = scan.nextInt();
                        if (beans < 0 || beans > 5) {
                            System.out.println("Range 0 - 5 ");

                        } else {
                            b = false;
                        }
                    } while (b);
                    boolean m = true;
                    int milk;
                    do {
                        System.out.print("How much milk will it require : ");
                        milk = scan.nextInt();
                        if (milk < 0 || milk > 300) {
                            System.out.println("Range 0 - 300ml ");

                        } else {
                            m = false;
                        }
                    } while (m);
                    ct.add(new CoffeeRecipe(name, water, beans, milk));
                    break;
                case 8:
                    making.turnOff();
                    isRunning = false;
                    break;
                case 9:
                    String names=scan.nextLine();;
                    User u22 = new User(names);
                    u1.add(u22);

                    isRunning = false;
                    break;
                case 10:
                    int j=1;
                    int se;
                    for(int i=0;i<u1.size();i++){
                        System.out.println((j)+" "+u1.get(i).name);
                    }
                    System.out.println("Select user");
                    se=scan.nextInt();
                    user=u1.get(se).name;
                    System.out.println("\t\t\t\t\t\t\t\t\tWelcome " + user);
                    break;
                default:
                    System.out.println("Invalid Option!");
            }
        } while (isRunning);
    }
}


class AutomaticCoffeeMaker extends CoffeeMaker {
    Scanner scan = new Scanner(System.in);
    public Heating heatingSystem;
    public Grinder grindingSystem;
    public WaterTank water;
    public MilkContainer milk;

    public AutomaticCoffeeMaker() {
        super("MakeItHappen");
        heatingSystem = new Heating();
        grindingSystem = new Grinder();
        water = new WaterTank(500);
        milk = new MilkContainer(300);
    }

    public int display() {
        System.out.println("\n---------------------- MENU ----------------------");
        System.out.println("(1) Pour Water\n(2) Pour Beans\n(3) Pour Milk\n(4) Make Coffee");
        System.out.println("(5) Show Info\n(6) Show Coffee recipe\n(7) Add Coffee\n(8) Turn Off\n(9) Add User\n(10) Show User");
        System.out.print("Select: ");
        return scan.nextInt();
    }

    public void startProcess() {


        Thread grindingThread = new Thread(() -> grindingSystem.grindBeans());
        Thread heatingThread = new Thread(() -> heatingSystem.heatWater());

        grindingThread.start();
        heatingThread.start();

        try {
            grindingThread.join();
            heatingThread.join();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Coffee is ready!");
        System.out.println("Cup is filled with coffee ☕");
    }

    public void showInfo() {
        System.out.println("\n--------- Machine Info ---------");
        System.out.println("Brand: " + getBrand());
        System.out.println("Water Capacity: " + water.getCapacity() + "ml");
        System.out.println("Water Available: " + water.getLevel() + "ml");
        System.out.println("Bean Capacity: " + grindingSystem.getCapacity() + " packets");
        System.out.println("Beans Available: " + grindingSystem.getCoffeebean() + " packets");
        System.out.println("Milk Capacity: " + milk.capacity + "ml");
        System.out.println("Milk Available: " + milk.getLevel() + "ml");
    }




}

class CoffeeRecipe {
    public String name;
    public int waterRequired;
    public int beansRequired;
    public int milkRequired;

    public CoffeeRecipe(String name, int water, int beans, int milk) {
        this.name = name;
        this.waterRequired = water;
        this.beansRequired = beans;
        this.milkRequired = milk;
    }

    public void showName(int i) {
        i++;
        System.out.println("(" + i + ") " + name);
    }

    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" : ").append(waterRequired).append(" : ").append(beansRequired).append(" : ").append(milkRequired);
        return sb.toString();
    }


}



class WaterTank {
    Scanner scan = new Scanner(System.in);
    public int capacity;
    public int level;

    public WaterTank(int capacity) {
        this.capacity = capacity;
        this.level = 0;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getLevel() {
        return level;
    }

    public void pourwater() {
        int test;
        do {
            System.out.println("\nPlease pour water (min 100ml, max 500ml)");
            System.out.print("Pour Water: ");
            test = scan.nextInt();
            test += level;
            if (test > 500) {
                System.out.println("Water container capacity exceeded (max 500ml)");
            } else if (test >= 100) {
                System.out.println("Water poured successfully");
                level = test;
            } else {
                System.out.println("Invalid Input");
            }
        } while (level < 0);
    }
}


class MilkContainer {
    Scanner scan = new Scanner(System.in);
    public int capacity;
    public int level;

    public MilkContainer(int capacity) {
        this.capacity = capacity;
        this.level = 0;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getLevel() {
        return level;
    }

    public void pourMilk() {
        int test;
        do {
            System.out.println("\nPlease pour milk (min 50ml, max " + capacity + "ml)");
            System.out.print("Pour Milk: ");
            test = scan.nextInt();
            test += level;
            if (test > capacity) {
                System.out.println("Milk container capacity exceeded");
            } else if (test >= 50) {
                System.out.println("Milk poured successfully");
                level = test;
            } else {
                System.out.println("Invalid Input");
            }
        } while (test < 0);
    }
}

class Grinder {
    Scanner scan = new Scanner(System.in);
    public byte capacity=5;
    public byte coffeebean;

    public Grinder() {
    }

    public int getCapacity() {
        return capacity;
    }

    public int getCoffeebean() {
        return coffeebean;
    }

    public void grindBeans() {
        System.out.println("Grinding Beans...");
        try {
            Thread.sleep(5000);
            System.out.println("Beans Grinded");
        } catch (InterruptedException e) {
            System.out.println("Do not press any button");
        }
    }

    public void pourBeans() {
        byte test;
        do {
            System.out.println("\nAdd coffee beans (min 1 packet, max 5 packets)");
            System.out.print("Enter coffee beans packet: ");
            test = scan.nextByte();
            test += coffeebean;
            if (test > 5) {
                System.out.println("Maximum capacity is 5");
            } else if (test > 0) {
                System.out.println("Poured successfully");
                coffeebean = test;
            } else {
                System.out.println("Invalid Input");
            }
        } while (test < 0);
    }
}
class Heating {
    int temperature = 0;

    public void heatWater() {
        System.out.println("Heating the coffee...");
        try {
            Thread.sleep(3000);
            temperature = 100;
        } catch (InterruptedException e) {
            System.out.println("Do not press any button");
        }
    }
}
class CoffeeMaker {
    private String brand;

    public CoffeeMaker(String brand) {
        this.brand = brand;
    }

    public String getBrand() {
        return brand;
    }

    public void turnOn() {
        System.out.println("Machine is turning ON...");
    }

    public void turnOff() {
        System.out.println("Machine is turning OFF...");
    }
}
class User {

    public String name;

    User(String name){
        this.name=name;
    }

}
