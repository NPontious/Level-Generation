import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

public class LevelCreate2 {
    public static int[][] zig = {
        {0,1,1},
        {1,1,0}
    };
    public static int[][] bigZig = {
        {1,1,0,0},
        {0,1,1,1}
    };
    public static int[][] lShape = {
        {1,0},
        {1,0},
        {1,1}
    };
    public static int[][] bigLShape = {
        {1,0},
        {1,0},
        {1,0},
        {1,1}
    };
    public static int[][] quadSeg = {
        {1},
        {1},
        {1},
        {1}
    };
    public static int[][] triSeg = {
        {1},
        {1},
        {1}
    };
    public static int[][] duoSeg = {
        {1},
        {1}
    };
    public static int[][] corner = {
        {1,0},
        {1,1}
    };
    public static int[][] bigCorner = {
        {1,0,0},
        {1,0,0},
        {1,1,1}
    };
    public static int[][] bShape = {
        {1,0},
        {1,1},
        {1,1}
    };
    public static ArrayList<int[][]> shapes = new ArrayList<>();
    public static int[][] level = new int[100][100];
    public static void main(String[] args) {
        shapes.add(zig);
        shapes.add(bigZig);
        shapes.add(lShape);
        shapes.add(bigLShape);
        shapes.add(quadSeg);
        shapes.add(triSeg);
        shapes.add(duoSeg);
        shapes.add(corner);
        shapes.add(bigCorner);
        shapes.add(bShape);
        Random rand = new Random();
        int numOfShapes = level.length *  level[0].length / 20;

        for (int i = 0; i < numOfShapes; i++) {
            int shapeIndex = rand.nextInt(shapes.size());
            int[][] shape = shapes.get(shapeIndex);

            // Randomize rotation
            int rotations = rand.nextInt(4);
            for (int r = 0; r < rotations; r++) {
                shape = rotateShape(shape);
            }

            int[] position = new int[2];
            boolean collision = true;
            while (collision) {
                position[0] = rand.nextInt(level[0].length - shape[0].length);
                position[1] = rand.nextInt(level.length - shape.length);
                collision = checkCollision(shape, position);
            }

            placeShape(shape, position, i + 1);
        }

        printLevel();
        try {
            drawLevelAsImage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to rotate a shape matrix 90 degrees clockwise
    public static int[][] rotateShape(int[][] shape) {
        int rows = shape.length;
        int cols = shape[0].length;
        int[][] rotatedShape = new int[cols][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                rotatedShape[j][rows - 1 - i] = shape[i][j];
            }
        }
        return rotatedShape;
    }

    // Method to check for collisions
    public static boolean checkCollision(int[][] shape, int[] position) {
        for (int k = 0; k < shape[0].length; k++) {
            for (int l = 0; l < shape.length; l++) {
                if (shape[l][k] != 0 && level[position[1] + l][position[0] + k] != 0) {
                    return true;
                }
            }
        }
        return false;
    }

    // Method to place a shape in the level
    public static void placeShape(int[][] shape, int[] position, int value) {
        for (int k = 0; k < shape[0].length; k++) {
            for (int l = 0; l < shape.length; l++) {
                if (shape[l][k] == 1) {
                    level[l + position[1]][k + position[0]] = value;
                }
            }
        }
    }

    // Method to print the level to the console
    public static void printLevel() {
        for (int i = 0; i < level[0].length; i++) {
            for (int j = 0; j < level.length; j++) {
                System.out.print(level[j][i]);
            }
            System.out.println();
        }
    }

    //Prints out the array as an image
    public static void drawLevelAsImage() throws IOException {
        BufferedImage image = new BufferedImage(level.length, level[0].length, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();

        for (int y = 0; y < level.length; y++) {
            for (int x = 0; x < level[0].length; x++) {
                Color color = getColorFromValue(level[y][x]);
                g.setColor(color);
                g.fillRect(y, x, 1, 1);
            }
        }

        File outputFile = new File("level_image.png");
        ImageIO.write(image, "png", outputFile);
    }

    //Maps number to a color
    private static Color getColorFromValue(int value) {
        if(value == 0){
            return Color.BLACK;
        }
        switch ((value % 10) + 1) {
            case 1:
                return Color.RED;
            case 2:
                return Color.BLUE;
            case 3:
                return Color.GREEN;
            case 4:
                return Color.YELLOW;
            case 5:
                return Color.CYAN;
            case 6:
                return Color.MAGENTA;
            case 7:
                return Color.ORANGE;
            case 8:
                return Color.PINK;
            case 9:
                return Color.LIGHT_GRAY;
            case 10:
                return Color.DARK_GRAY;
            default:
                return Color.BLACK;
        }
    }
}
