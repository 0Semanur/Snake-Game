import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

public class main {

    public static final int ROWS = 10;
    public static final int COLS = 10;

    public static final char EMPTY = '.';
    public static final char SNAKE = 'S';
    public static final char FOOD = 'F';
    public static final char BOMB = 'B';

    public static final int UP = 0;
    public static final int DOWN = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;

    public static Random random = new Random();
    public static Scanner scanner = new Scanner(System.in);

    public static LinkedList<Node> snake = new LinkedList<>();

    public static class Node {
        int row;
        int col;

        public Node(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }

    public static char[][] board = new char[ROWS][COLS];
    public static Node food;
    public static Node bomb;
    public static int direction;
    public static int score;
    public static int bombCounter;

    public static void init() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                board[i][j] = EMPTY;
            }
        }

        int midRow = ROWS / 2;
        int midCol = COLS / 2;
        for (int i = 0; i < 5; i++) {
            Node node = new Node(midRow, midCol - i);
            snake.add(node);
            board[node.row][node.col] = SNAKE;
        }

        generateFood();
        generateBomb();

        direction = RIGHT;
        score = 0;
        bombCounter = 0;
    }

    public static void generateFood() {
        int row, col;
        do {
            row = random.nextInt(ROWS);
            col = random.nextInt(COLS);
        } while (board[row][col] != EMPTY);

        food = new Node(row, col);
        board[food.row][food.col] = FOOD;
    }

    public static void generateBomb() {
        int row, col;
        do {
            row = random.nextInt(ROWS);
            col = random.nextInt(COLS);
        } while (board[row][col] != EMPTY);

        bomb = new Node(row, col);
        board[bomb.row][bomb.col] = BOMB;
    }

    public static void draw() {
        System.out.println("Food Counter: " + score);
        System.out.println("Bomb Counter: "+bombCounter);
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void update() {
        Node head = snake.peekFirst();
        int newRow = head.row;
        int newCol = head.col;
        switch (direction) {
            case UP:
                newRow--;
                break;
            case DOWN:
                newRow++;
                break;
            case LEFT:
                newCol--;
                break;
            case RIGHT:
                newCol++;
                break;
        }

        if (newRow < 0 || newRow >= ROWS || newCol < 0 || newCol >= COLS) {
            gameOver();
            return;
        }

        if (newRow == food.row && newCol == food.col) {
            score++;
            generateFood();
        } else {
            Node tail = snake.removeLast();
            board[tail.row][tail.col] = EMPTY;
        }

        if (newRow == bomb.row && newCol == bomb.col) {
            bombCounter++;

            if (bombCounter == 3) {
                Node last = snake.removeLast();
                board[last.row][last.col] = EMPTY;
                bombCounter = 0;
            }

            generateBomb();
        }

        if (board[newRow][newCol] == SNAKE) {
            gameOver();
            return;
        }

        Node node = new Node(newRow, newCol);
        snake.addFirst(node);
        board[node.row][node.col] = SNAKE;

        if (snake.size() < 4) {
            gameOver();
            return;
        }
    }

    public static void input() {
        System.out.println("Enter W (up), S (down), A (left), D (right)");

        String input = scanner.nextLine();

        switch (input.toUpperCase()) {
            case "W":
                if (direction != DOWN) {
                    direction = UP;
                }
                break;
            case "S":
                if (direction != UP) {
                    direction = DOWN;
                }
                break;
            case "A":
                if (direction != RIGHT) {
                    direction = LEFT;
                }
                break;
            case "D":
                if (direction != LEFT) {
                    direction = RIGHT;
                }
                break;
            default:
                break;
        }
    }

    public static void gameOver() {
        System.out.println("Game Over!");
        System.exit(0);
    }

    public static void main(String[] args) {
        init();

        while (true) {
            draw();
            input();
            update();
        }
    }
}
