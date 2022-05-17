
package snakepackage;

import java.util.ArrayList;
import java.util.Random;

class Snake {

    static String direction = "RIGHT"; // UP, RIGHT, DOWN, LEFT
    private int tableRows = 15;
    private int tableColumns = 20;

    private String tableData [][] = new String[tableRows][tableColumns];
    private int fruit [] = new int[2];

    private static ArrayList<int[]> playerData = new ArrayList<int[]>();

    private Double tableUpdateTime = (double)(1000 / 30);
    private int gameSpeed = 8;

    
    private Thread updateGui = null;
    private Thread speedGame = null;

    private boolean isUpdateGui = true;
    private boolean isSpeedGame = true;

    static {
        System.loadLibrary("lib/controls");

        Thread runControlsListener = new Thread(new Runnable() { 
            @Override
            public void run() {
                controls();
            } 
        });

        runControlsListener.start();

    }
    

    public static void main(String [] args) {

        Snake snake = new Snake();

        snake.start();
        
    }
    
    private static String getBodyDirection() {

        int [] head = playerData.get(0);
        int [] nextHead = playerData.get(1);

        if (head[0] - 1 == nextHead[0] && head[1] == nextHead[1]){
            return "DOWN"; 
        }else if (head[0] + 1 == nextHead[0] && head[1] == nextHead[1]){
            return "UP"; 
        }else if (head[0] == nextHead[0] && head[1] - 1 == nextHead[1]){
            return "RIGHT"; 
        }else if (head[0] == nextHead[0] && head[1] + 1 == nextHead[1]){
            return "LEFT"; 
        }
        
        return "";
    }

    //controls native method to call C++ function
    static native void controls();

    public static void controlUp(){
        if ( getBodyDirection() != "DOWN" ){ 
            direction = "UP";
        }
    } 

    public static void controlRight(){
        if ( getBodyDirection() != "LEFT" ){ 
            direction = "RIGHT";
        } 
    }

    public static void controlDown(){
        if ( getBodyDirection() != "UP" ){ 
            direction = "DOWN";
        }  
    }

    public static void controlLeft(){
        if ( getBodyDirection() != "RIGHT" ){ 
            direction = "LEFT";
        }   
    }

    public static void controlExit(){
        System.exit(0);
    }

    private void start() {
        resetGame();

        printTable ();
         
        updateGui = new Thread(new Runnable(){ 
            @Override
            public void run() { 
                try {
                    while (isUpdateGui){
                        //clean cmd console
                        new ProcessBuilder("cmd", "/c", "cls" ).inheritIO().start().waitFor();
                    
                        printTable();
                        Thread.sleep(tableUpdateTime.longValue());
                    }
                }catch(Exception e){
                    e.printStackTrace();
                    stopAllbyError(e); 
                }
            } 
        });

        speedGame = new Thread(new Runnable() { 
            @Override
            public void run() {
                long speed = (1000 - (gameSpeed * 100));

                try{
                    while (isSpeedGame){
                        for (int [] body : playerData) {
                            tableData[body[0]][body[1]] = " ";
                        }

                        updatePlayer();
                        
                        insertPlayer();
                        insertFruit();

                        Thread.sleep(speed > 50 ? speed : 50);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                    stopAllbyError(e); 
                }
            }

        });

        updateGui.start();
        speedGame.start();
    }
    
    private void updatePlayer() {

        int [] head = playerData.get(0);

        switch (direction) { 
            case "UP": 
                playerData.add(0, new int[] {head[0] - 1, head[1]});
            break;
            case "RIGHT":             
                playerData.add(0, new int[] {head[0], head[1] + 1});
            break;
            case "DOWN": 
                playerData.add(0, new int[] {head[0] + 1, head[1]});
            break;
            case "LEFT": 
                playerData.add(0, new int[] {head[0], head[1] - 1});            
            break;
            default: break;
        }

        if (head[0] == fruit[0] && head[1] == fruit[1]){
            createRandomFriut();
        }else{
            playerData.remove(playerData.size() - 1);
        }

        int [] newHead = playerData.get(0);

        //Head Collition with any wall
        if (newHead[0] <= 0 || newHead[0] >= tableRows -1 || newHead[1] <= 0 || newHead[1] >= tableColumns -1){
            resetGame();
        }

        //Head Collition with body 
        for (int [] body : playerData.subList(1, playerData.size())){
            if(body[0] == newHead[0] && body[1] == newHead[1]){
                resetGame();
                break;
            }
        }

    } 

    private void stopAllbyError(Exception e) {

        isUpdateGui = false;
        isSpeedGame = false;

        System.exit(1);
    } 

    private void printTable() {
        for (int i = 0; i < tableRows; i++){
            for (int j = 0; j < tableColumns; j++){
                System.out.print(tableData[i][j]);
            }
            System.out.println("");
        }
    }

    private void resetGame() {
        direction = "RIGHT";
        playerData = new ArrayList<int[]>();

        //player position
        int middleColumns = (int) (tableColumns / 2);
        int middleRows = (int) (tableRows / 2);

        playerData.add( new int[]{middleRows, middleColumns});
        playerData.add( new int[]{middleRows, middleColumns - 1});
        playerData.add( new int[]{middleRows, middleColumns - 2});
        playerData.add( new int[]{middleRows, middleColumns - 3});

        createRandomFriut();

        //fill table with a space
        for (int i = 1; i < tableRows - 1; i++){
            for (int j = 1; j < tableColumns - 1; j++){
                tableData[i][j] = " ";
            }
        }

        //fill right and left walls
        for (int i = 0; i < tableColumns; i++){
            tableData[0][i] = "|";
            tableData[tableRows - 1][i] = "|";
        }

        //Fill up and down walls 
        for (int i = 0; i < tableRows; i++){
            tableData[i][0] = "||";
            tableData[i][tableColumns - 1] = "||";
        }

        insertPlayer();
        insertFruit();

    }

    private void insertFruit() {
        tableData[fruit[0]][fruit[1]] = "Q";
    }

    private void insertPlayer() {
        try {
            for (int [] pos : playerData){
                tableData[pos[0]][pos[1]] = "#";
            }

            int [] head = playerData.get(0);
            tableData[head[0]][head[1]] = "@";
        }catch (ArrayIndexOutOfBoundsException aiobe){
            aiobe.printStackTrace();
            resetGame();
        }
    }

    private void createRandomFriut() {
        Random random = new Random();

        int x = random.nextInt(tableColumns - 2) + 1;
        int y = random.nextInt(tableRows - 2) + 1;

        for (int [] body : playerData){
            if(body[0] == y && body[1] == x){
                createRandomFriut();
                return;
            }
        }

        fruit[0] = y;
        fruit[1] = x;
    }

}