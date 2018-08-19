/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multi_media;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.*;
import java.io.IOException;
import java.net.URISyntaxException;
/**
 *
 * @author elkhafagy
 */

import java.awt.Color;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;

public class Quantization {

    private BufferedImage BFimage;
    private BufferedImage AFimage;
    private int width = 960;
    private int height = 960;

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
                    System.out.println("  "+r+"  "+"  "+g+"  "+b);
                    matrix1[x][y] = new Color(r, g, b, a);

                }

            }

        } catch (Exception e) {
            System.out.println("here+++++++>  " + e.getMessage());
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
            ImageIO.write(AFimage, "jpg", f);

        } catch (Exception e) {
            System.out.println("here+++++++>  " + e.getMessage());
            e.printStackTrace();
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////

    private int quantizeValue(int numberOfBits, int value) {
        int skip = 256 / (int) (Math.pow(2, numberOfBits));
        value = value - (value % skip);
        return value;
    }

    public Color[][] quantize(double numOfBits, BufferedImage bf) {
        Color[][] matrix_res = new Color[bf.getWidth()][bf.getHeight()];
        Color[][] matrix = initmatrix(bf);
        for (int x = 0; x < matrix.length; x++) {
            for (int y = 0; y < matrix[0].length; y++) {
                int red = matrix[x][y].getRed();
                int green = matrix[x][y].getGreen();
                int blue = matrix[x][y].getBlue();
                red = quantizeValue((int) numOfBits, red);
                green = quantizeValue((int) numOfBits, green);
                blue = quantizeValue((int) numOfBits, blue);
                Color color = new Color(red, green, blue);
//             System.out.println("color    ====>"+color.toString() );
                matrix_res[x][y] = color;

            }
        }
        return matrix_res;
    }

    public static void main(String[] args) {
        Quantization q = new Quantization();
        BufferedImage img = q.get_img("/home/elkhafagy/Desktop/1.jpg");
        q.output(q.quantize(2, img), "/home/elkhafagy/Desktop/2.jpg");
    }
}
