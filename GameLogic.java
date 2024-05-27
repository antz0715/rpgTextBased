import java.util.Scanner;

public class GameLogic {
    static Scanner scanner = new Scanner(System.in);

    static Player player;

    public static boolean isRunning;


    //random encounters
    public static String[] encounters = {"Battle", "Battle", "Battle", "Rest", "Rest"};

    // enemy names 
    public static String[] enemies = {"Ogre", "Ogre", "Goblin", "Goblin", "Stone Elemental"};



    // story elements
    public static int place = 0, act = 1;
    public static String[] places = {"Everlasting Mountains", "Haunted Landlines", "Castle of the Evil Emperor", "Throne Room"};

    public static int readInt(String prompt, int userChoices){
        int input;

        do{
            System.out.println(prompt);
            try{
                input = Integer.parseInt(scanner.next());

            }catch(Exception e){
                input = -1;
                System.out.println("Please enter an integer!");
            }
        }while(input < 1 || input > userChoices);
        return input;
    }
    public static void clearConsole(){
        for(int i = 0; i < 100; i++){
            System.out.println();

        }
        

        
    }
    public static void printSeperator(int n){
        for(int i = 0; i < n; i++){
            System.out.print("-");
        }
        System.out.println();
        
    }
    public static void printHeading(String title){
        printSeperator(30);
        System.out.println(title);
        printSeperator(30);
    }

    public static void anythingToContinue(){
        System.out.println("\nEnter anything to continue");
        scanner.next();
    }
    public static void startGame(){
        boolean nameSet = false;
        String name; 
        clearConsole();
        printSeperator(40);
        printSeperator(30);
        System.out.println("Age of the Evil Emperor");
        printSeperator(30);
        printSeperator(40);

        //player name
        do{
            clearConsole();
            printHeading("What is your name?");
            name = scanner.next();

            clearConsole();
            printHeading("Your name is " + name + "\nIs that correct?");
            System.out.println("1. YES");
            System.out.println("2. NO I want to change my name");
            int input = readInt("-> ", 2); 
            if(input == 1){
                nameSet = true;
            }


        }while(!nameSet);

        Story.printIntro();

        // player obj
        player = new Player(name);

        Story.printFirstActIntro();

        isRunning = true;



        // sta
        gameLoop();


    }
    public static void checkAct(){
        if(player.xp >= 10 && act == 1){

            act = 2;
            place = 1;

            Story.printFirstActOutro();

            player.chooseTrait();

            Story.printSecondActIntro();

            //assign new values to enemies
            enemies[0] = "Evil Mercenary";
            enemies[1] = "Goblin";
            enemies[2] = "Wolve Pack"; 
            enemies[3] = "Henchman of the Evil Emperor";
            enemies[4] = "Scary Stranger";

            encounters[0] = "Battle";
            encounters[1] = "Battle";
            encounters[2] = "Battle";
            encounters[3] = "Rest";
            encounters[4] = "Battle";

        }else if(player.xp >= 50 && act == 2){
            act = 3;
            place = 2;

            Story.printSecondActOutro();

            player.chooseTrait();

            Story.printThirdActIntro();

            enemies[0] = "Evil Mercenary";
            enemies[1] = "Evil Mercenary";
            enemies[2] = "Henchman of the Evil Emperor";
            enemies[3] = "Henchman of the Evil Emperor";
            enemies[4] = "Henchman of the Evil Emperor";

            encounters[0] = "Battle";
            encounters[1] = "Battle";
            encounters[2] = "Battle";
            encounters[3] = "Battle";
            encounters[4] = "Shop";

            player.hp  = player.maxHp;

        }else if (player.xp >= 100 && act == 3){
            act = 4;
            place = 3;

            Story.printThirdActOutro();

            player.chooseTrait();

            Story.printFourthActIntro();
            player.hp  = player.maxHp;

            //final battle
            finalBattle();



        }
    }

    public static void randomEncounter(){
        int encounter = (int) (Math.random()* encounters.length);

        if (encounters[encounter].equals("Battle")){
            randomBattle();
        } else if (encounters[encounter].equals("Rest")){
            takeRest();
        }else{
            shop();
        }

    } 



    public static void continueJourney(){
        checkAct();

        if(act != 4){
            randomEncounter();
        }


    }
    public static void characterInfo(){
        clearConsole();
        printHeading("CHARACTER INFO");
        System.out.println(player.name + "\tHP: " + player.hp + "/" + player.maxHp);
        printSeperator(20);
        System.out.println("XP:" + player.xp + "\tGold: " + player.gold) ;
        System.out.println("#of pots " + player.pots);


        if (player.numAtkUpgrades > 0){
            System.out.println("Offensive trait: " + player.atkUpgrades[player.numAtkUpgrades -1 ] );
            printSeperator(20);
        }
        if (player.numDefUpgrades > 0){
            System.out.println("Defensive trait: " + player.defUpgrades[player.numDefUpgrades -1 ]);
        }
        anythingToContinue();
    }

    public static void shop(){
        clearConsole();
        printHeading("Wandering trader ");
        int price = (int) (Math.random()* (10 + player.pots*3)+ 10 + player.pots);
        System.out.println("Potion: " + price + " gold");
        printSeperator(20);
        System.out.println("Do you want to buy one 1) yes 2) no");
        int input = readInt("->", 2);
        if (input == 1){
            clearConsole();
            if (player.gold >= price){
                printHeading("YOu bought a potion");
                player.pots++;
                player.gold -= price;
            }else{
                printHeading("Come back next time");

            }
            anythingToContinue();
        }
    }

    public static void takeRest(){
        clearConsole();
        if(player.restsLeft >= 1){
            printHeading("Do you want to rest " + player.restsLeft + "  rests left");
            System.out.println("1)yes 2)no");
            int input = readInt("->", 2);
            if(input == 1){
                clearConsole();
                if (player.hp < player.maxHp){
                    int hpRestored = (int) (Math.random()* (player.xp/4 + 1) + 10);
                    player.hp += hpRestored;
                    if (player.hp > player.maxHp){
                        player.hp = player.maxHp;

                    }
                    System.out.println("you restored " + hpRestored + " health");
                    player.restsLeft--;
                }
            }else{
                System.out.println("Your already rested");
            }
            anythingToContinue();
        }
    }

    public static void randomBattle(){
        clearConsole();
        printHeading("FIGHT");
        anythingToContinue();
        battle(new Enemy(enemies[(int)(Math.random()*enemies.length)], player.xp));
    }

    //battle method
    public static void battle(Enemy enemy){
        while(true){
            clearConsole();
            printHeading(enemy.name + "\nHP: " + enemy.hp + "/" + enemy.maxHp);
            printHeading(player.name + "\nHP: " + player.hp + "/" + player.maxHp);
            System.out.println("Choose an action");
            printSeperator(20);
            System.out.println("1)Fight 2) use potion 3) run");
            int input = readInt("-> ", 3 );

            if (input == 1){
                //fight
                //calc dmg 
                int dmg = player.attack() - enemy.defend();
                int dmgTook = enemy.attack() - player.defend();
                if (dmgTook < 0 ){
                    dmg -= dmgTook/2;
                    dmgTook = 0;
                }
                if(dmg < 0 ){
                    dmg = 0;

                }
                player.hp -= dmgTook;
                enemy.hp -= dmg;

                clearConsole();
                printHeading("battle");
                System.out.println("dealt" + dmg + "to the" + enemy.name + ".");
                printSeperator(15);
                System.out.println(enemy.name + "dealt" + dmgTook );
                anythingToContinue();
                if(player.hp <= 0 ){
                    playerDied();
                    break;
                }else  if (enemy.hp <= 0 ){
                    clearConsole();
                    printHeading("You slaind " + enemy.name);

                    player.xp += enemy.xp;
                    System.out.println("You earned " + enemy.xp + "XP");

                    boolean addRest = (Math.random()*5 + 1 <= 2.25);
                    int goldEarned = (int) (Math.random()*enemy.xp);
                    if(addRest){
                        player.restsLeft++;
                        System.out.println("add rest");
                    }
                    if(goldEarned > 0 ){
                        player.gold += goldEarned;
                        System.out.println(goldEarned + " gold earned");
                    }
                    anythingToContinue();
                    break;
                }

            }else if (input == 2){
                // use potion
                clearConsole();
                if(player.pots > 0 && player.hp < player.maxHp){
                    printHeading("Do you want to drink a potion " + player.pots + " left");
                    System.out.println("1) yes 2) no");
                    input = readInt("->", 2);
                    if (input == 1){
                        player.hp = player.maxHp;
                        clearConsole();
                        printHeading("Drank pot");
                        anythingToContinue();
                    }



                }else{
                    printHeading("YOu dont have any pots or ur at full health");
                    anythingToContinue();
                }

            }else{
                //run
                clearConsole();
                if(act != 4){
                    if(Math.random()*10 + 1 <= 3.5){
                        printHeading("YOu ran away");
                        anythingToContinue();
                        break;
                    }else{
                        printHeading("You didnt mangage to escape");

                        int dmgTook = enemy.attack();
                        System.out.println("YOu took" + dmgTook + "Damage");
                        anythingToContinue();

                        if(player.hp <= 0){
                            playerDied();
                        }
                    }
                }else{
                    printHeading("YOu cannot escape the evil emporor");
                    anythingToContinue();
                }


            }

        }
    }

    public static void printMenu(){
        clearConsole();
        printHeading(places[place]);
        System.out.println("Choose an action: ");
        printSeperator(20);
        System.out.println("1. Continue on journey");
        System.out.println("2. character info");
        System.out.println("3. exit game");
    }


    public static void finalBattle(){
        battle(new Enemy("The Evil Emperor", 300));

        Story.printEnd(player);
        isRunning = false;

    }

    public static void playerDied(){
        clearConsole();
        printHeading("You died");
    }



    public static void gameLoop(){
        while(isRunning){
            printMenu();
            int input = readInt("->" , 3);
            if (input == 1 ){
                continueJourney();
            } else if (input == 2){
                characterInfo();
            }else{
                isRunning = false;
            }
        }
    }
        
    



    
}
