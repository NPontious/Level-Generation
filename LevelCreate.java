import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

class LevelCreate{
    public static int[][] level;
    public static void main(String[] args) {
        // for(int i = 0; i < level[0].length; i++){
        //     for(int j = 0; j < level.length; j++){
        //         level[j][i] = 0;
        //     }
        // }
        readImage();
        cullSingle();
        cutWeirdShapes();
        cullSingle();
        // cutWeirdShapes();
        // for(int i = 0; i < level[0].length; i++){
        //     for(int j = 0; j < level.length; j++){
        //         System.out.print(level[j][i]);
        //     }
        //     System.out.println();
        // }
        saveArrayAsPNG(level, "output.png");
    }

    public static void readImage(){
        try {
            // Specify the path to the PNG file
            File file = new File("Noise.png");
            
            // Read the PNG image
            BufferedImage image = ImageIO.read(file);

            level = new int[image.getWidth()][image.getHeight()];
            
            // Print image dimensions
            //System.out.println("Width: " + image.getWidth());
            //System.out.println("Height: " + image.getHeight());
            
            // Access pixel data
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    int rgb = image.getRGB(x, y);
                    // Extract color components
                    int red = (rgb >> 16) & 0xFF;
                    int green = (rgb >> 8) & 0xFF;
                    int blue = rgb & 0xFF;
                    //System.out.println("Pixel (" + x + ", " + y + ") - R: " + red + ", G: " + green + ", B: " + blue);
                    if(red != 0 || green != 0 || blue != 0){
                        level[x][y] = rgb;
                    }
                }
            }
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    public static void saveArrayAsPNG(int[][] array, String filePath) {
        int height = array.length;
        int width = array[0].length;

        // Create a BufferedImage with the specified width and height
        BufferedImage image = new BufferedImage(height, width, BufferedImage.TYPE_INT_RGB);

        // Fill the BufferedImage with the pixel data from the array
        for (int y = 0; y < width; y++) {
            for (int x = 0; x < height; x++) {
                image.setRGB(x, y, array[x][y]);
            }
        }

        // Save the BufferedImage as a PNG file
        try {
            File file = new File(filePath);
            ImageIO.write(image, "png", file);
            //System.out.println("Image saved successfully to " + filePath);
        } catch (IOException e) {
            System.err.println("Error saving image: " + e.getMessage());
        }
    }

    public static void cullSingle(){
        for(int i = 1; i < level[0].length - 1; i++){
            for(int j = 1; j < level.length - 1; j++){
                if(level[j - 1][i] == 0 && level[j + 1][i] == 0 && level[j][i - 1] == 0 && level[j][i + 1] == 0 && level[j][i] != 0){
                    level[j][i] = 0;
                } else if(level[j][i] != 0) {
                    shapeColor(i, j);
                }
            }
        }
        for(int j = 1; j < level.length - 1; j++){
            if(level[j - 1][0] == 0 && level[j + 1][0] == 0 && level[j][0] != 0){
                level[j][0] = 0;
            } else if(level[j][0] != 0){
                shapeColor(0, j);
            }
            if(level[j - 1][level[0].length - 1] == 0 && level[j + 1][level[0].length - 1] == 0 && level[j][level[0].length - 1] != 0){
                level[j][level[0].length - 1] = 0;
            } else if(level[j][level[0].length - 1] != 0){
                shapeColor(level[0].length - 1, j);
            }
        }
        for(int i = 1; i < level[0].length - 1; i++){
            if(level[0][i - 1] == 0 && level[0][i + 1] == 0 && level[1][i] == 0 && level[0][i] != 0){
                level[0][i] = 0;
            } else if(level[0][i] != 0) {
                shapeColor(i, 0);
            }
            if(level[level.length - 1][i - 1] == 0 && level[level.length - 1][i + 1] == 0 && level[level.length - 2][i] == 0 && level[level.length - 1][i] != 0){
                level[level.length - 1][i] = 0;
            } else if(level[level.length - 1][i] != 0) {
                shapeColor(i, level.length - 1);
            }
        }
        
        if(level[0][1] == 0 && level[1][0] == 0 && level[0][0] != 0){
            level[0][0] = 0;
        } else if(level[0][0] != 0) {
            shapeColor(0, 0);
        }
        if(level[level.length - 1][1] == 0 && level[level.length - 2][0] == 0 && level[level.length - 1][0] != 0){
            level[level.length - 1][0] = 0;
        } else if(level[level.length - 1][0] != 0) {
            shapeColor(0, level.length - 1);
        }
        if(level[0][level[0].length - 2] == 0 && level[1][level[0].length - 1] == 0 && level[0][level[0].length - 1] != 0){
            level[0][level[0].length - 1] = 0;
        } else if(level[0][level[0].length - 1] != 0) {
            shapeColor(0, level.length - 1);
        }
        if(level[level.length - 2][level[0].length - 1] == 0 && level[level.length - 1][level[0].length - 2] == 0 && level[level.length - 1][level[0].length - 1] != 0){
            level[level.length - 1][level[0].length - 1] = 0;
        } else if(level[level.length - 1][level[0].length - 1] != 0) {
            shapeColor(level[0].length - 1, level.length - 1);
        }
    }

    public static void shapeColor(int i, int j){
        if(j - 1 >= 0 && level[j - 1][i] != level[j][i]){
            if(level[j - 1][i] != 0){
                level[j - 1][i] = level[j][i];
                shapeColor(i, j - 1);
            }
        }
        if(j + 1 < level.length && level[j + 1][i] != level[j + 1][i]){
            if(level[j + 1][i] != 0){
                level[j + 1][i] = level[j][i];
                shapeColor(i, j + 1);
            }
        }
        if(i - 1 >= 0 && level[j][i - 1] != level[j][i]){
            if(level[j][i - 1] != 0){
                level[j][i - 1] = level[j][i];
                shapeColor(i - 1, j);
            }
        }
        if(i + 1 < level[0].length && level[j][i + 1] != level[j][i]){
            if(level[j][i + 1] != 0){
                level[j][i + 1] = level[j][i];
                shapeColor(i + 1, j);
            }
        }
    }

    public static void cutWeirdShapes(){
        int offences = 0;
        for(int i = 1; i < level[0].length - 1; i++){
            for(int j = 1; j < level.length - 1; j++){
                if(level[j - 1][i] != 0 && level[j][i] == 0){
                    offences++;
                }
                if(level[j + 1][i] != 0 && level[j][i] == 0){
                    offences++;
                }
                if(level[j][i - 1] != 0 && level[j][i] == 0){
                    offences++;
                }
                if(level[j][i + 1] != 0 && level[j][i] == 0){
                    offences++;
                }
                if(offences >= 3){
                    // if(System.currentTimeMillis() % 2 == 0){
                    //     level[j - 1][i] = 0;
                    //     level[j + 1][i] = 0;
                    // } else {
                    //     level[j][i - 1] = 0;
                    //     level[j][i + 1] = 0;
                    // }
                    level[j][i - 1] = 0;
                    level[j][i + 1] = 0;
                    level[j - 1][i] = 0;
                    level[j + 1][i] = 0;
                }
                offences = 0;
                // else if(level[j][i] != 0) {
                //     shapeColor(i, j);
                // }
            }
        }
    }

    public static void shatterWeirdShapes(){
        for(int i = 1; i < level.length - 1; i++){
            for(int j = 1; j < level[0].length - 1; j++){
                if(j - 1 >= 0 && level[j - 1][i] != level[j][i]){
                    if(level[j - 1][i] != 0){
                        level[j - 1][i] = level[j][i];
                        shapeColor(i, j - 1);
                    }
                }
                if(j + 1 < level.length && level[j + 1][i] != level[j + 1][i]){
                    if(level[j + 1][i] != 0){
                        level[j + 1][i] = level[j][i];
                        shapeColor(i, j + 1);
                    }
                }
                if(i - 1 >= 0 && level[j][i - 1] != level[j][i]){
                    if(level[j][i - 1] != 0){
                        level[j][i - 1] = level[j][i];
                        shapeColor(i - 1, j);
                    }
                }
                if(i + 1 < level[0].length && level[j][i + 1] != level[j][i]){
                    if(level[j][i + 1] != 0){
                        level[j][i + 1] = level[j][i];
                        shapeColor(i + 1, j);
                    }
                }
            }
        }
    }

    public static void printColor(int x, int y){
        int red;
        int green;
        int blue;
        red = (level[x][y] >> 16) & 0xFF;
        green = (level[x][y] >> 8) & 0xFF;
        blue = level[x][y] & 0xFF;
        System.out.println();
        System.out.println("Pixel (" + x + ", " + y + ") - R: " + red + ", G: " + green + ", B: " + blue);
    }
}