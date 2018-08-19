/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multi_media;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.Random;
import javax.imageio.ImageIO;

/**
 *
 * @author elkhafagy
 */
public class floyed {

    private BufferedImage BFimage;
    private BufferedImage AFimage;
    private int width = 960;
    private int height = 960;

    private double[] threshold = {0.25, 0.26, 0.27, 0.28, 0.29, 0.3, 0.31,
        0.32, 0.33, 0.34, 0.35, 0.36, 0.37, 0.38, 0.39, 0.4, 0.41, 0.42,
        0.43, 0.44, 0.45, 0.46, 0.47, 0.48, 0.49, 0.5, 0.51, 0.52, 0.53,
        0.54, 0.55, 0.56, 0.57, 0.58, 0.59, 0.6, 0.61, 0.62, 0.63, 0.64,
        0.65, 0.66, 0.67, 0.68, 0.69};

    public BufferedImage get_img(String uri) {
        try {
            File f = new File(uri);
            FileInputStream fs = new FileInputStream(f);
            BFimage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            BFimage = ImageIO.read(f);
        } catch (Exception e) {
            System.out.println("here+++++++>  " + e.getMessage());
            e.printStackTrace();
        }
        return BFimage;
    }

    public Color[][] initmatrix(BufferedImage img) {
        Color[][] matrix1 = new Color[img.getWidth()][img.getHeight()];
        try {

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int pixel = img.getRGB(x, y);
                    int a = (pixel >> 24) & 0x0FF;
                    int r = (pixel >> 16) & 0x0FF;
                    int g = (pixel >> 8) & 0x0FF;
                    int b = (pixel) & 0x0FF;
                    matrix1[x][y] = new Color(r, g, b, a);

                }
            }

        } catch (Exception e) {
            System.out.println("here +++++++>  " + e.getMessage());
            e.printStackTrace();
        }
        return matrix1;

    }

    public void output(Color[][] matrix, String uri) {
        try {
            File f = new File(uri);
            FileInputStream fs = new FileInputStream(f);
            AFimage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            for (int x = 0; x < matrix.length; x++) {
                for (int y = 0; y < matrix[0].length; y++) {
                    AFimage.setRGB(x, y, matrix[x][y].getRGB());
                }
            }
            ImageIO.write(AFimage, "PNG", f);

        } catch (Exception e) {
            System.out.println("here+++++++>  " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Color[][] floyed(BufferedImage b) {

        {
            Color[][] matrix_res = new Color[b.getWidth()][b.getHeight()];
            Color[][] matrix = initmatrix(b);
            System.out.println("size" + width + height);
            Random rn = new Random();
            for (int x = 0; x < matrix.length; x++) {
                for (int y = 0; y < matrix[0].length; y++) {
                    int red = matrix[x][y].getRed();
                    int green = matrix[x][y].getGreen();
                    int blue = matrix[x][y].getBlue();
                    int error = 0;
                    Color color = new Color(red, green, blue);
                    double lum = (red * 0.21f + green * 0.71f + blue * 0.07f) / 255;

                    if (lum <= threshold[rn.nextInt(threshold.length)]) {
                        matrix_res[x][y] = new Color(0, 0, 0);

                        error = color.getRGB() - matrix_res[x][y].getRGB();
//                        System.out.println(y+1);
                        if ((y + 1) < height) {

                            matrix[x][y + 1] = new Color((7 / 16) * error);
                        }
                        if ((y + 1 < height) && (x + 1 < width)) {
                            matrix[x + 1][y + 1] = new Color((1 / 16) * error);
                        }
                        if (x + 1 < width) {
                            matrix[x + 1][y] = new Color((5 / 16) * error);
                        }
                        if (0 < y - 1 && y - 1 < height) {
                            matrix[x][y - 1] = new Color((3 / 16) * error);
                        }
                    } else {
                        matrix_res[x][y] = new Color(255, 255, 255);

                        error = color.getRGB() - matrix_res[x][y].getRGB();
                        if ((y + 1) < height) {

                            matrix[x][y + 1] = new Color((7 / 16) * error);
                        }
                        if ((y + 1 < height) && (x + 1 < width)) {
                            matrix[x + 1][y + 1] = new Color((1 / 16) * error);
                        }
                        if (x + 1 < width) {
                            matrix[x + 1][y] = new Color((5 / 16) * error);
                        }
                        if (0 < y - 1 && y - 1 < height) {
                            matrix[x][y - 1] = new Color((3 / 16) * error);
                        }
                    }

                }
            }
            return matrix_res;
        }
    }

    public static void main(String[] args) {
        floyed q = new floyed();
        BufferedImage img = q.get_img("/home/elkhafagy/Desktop/1.png");
        q.output(q.floyed(img), "/home/elkhafagy/Desktop/out.png");
    }
}
