package multi_media;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.*;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;
import multi_media.Quantization;

public class AddTransparency {

    private BufferedImage BFimage;
    private BufferedImage AFimage;
    private int width = 960;
    private int height = 960;

    public BufferedImage get_img(String uri) {
        try {
            File f = new File(uri);
            FileInputStream fs = new FileInputStream(f);
            BFimage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);// or gray or binary
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

    public Color[][] alpha(double alpha, BufferedImage firstMatriximg, BufferedImage secondMatriximg) {
        Color[][] matrix_res = new Color[firstMatriximg.getWidth()][firstMatriximg.getHeight()];
        Color[][] secondMatrix = initmatrix(secondMatriximg);
        Color[][] firstMatrix = initmatrix(firstMatriximg);
        for (int x = 0; x < firstMatrix.length; x++) {
            for (int y = 0; y < firstMatrix[0].length; y++) {

                int red = (int) ((firstMatrix[x][y].getRed() * alpha) + (secondMatrix[x][y].getRed() * (1 - alpha)));
                int green = (int) ((firstMatrix[x][y].getGreen() * alpha) + (secondMatrix[x][y].getGreen() * (1 - alpha)));
                int blue = (int) ((firstMatrix[x][y].getBlue() * alpha) + (secondMatrix[x][y].getBlue() * (1 - alpha)));

                Color color = new Color(red, green, blue);
                matrix_res[x][y] = color;

            }
        }
        return matrix_res;
    }

    public static void main(String[] args) {
        AddTransparency q = new AddTransparency();
        BufferedImage img1 = q.get_img("/home/elkhafagy/Desktop/1.jpg");
        BufferedImage img2 = q.get_img("/home/elkhafagy/Desktop/2.jpg");
        q.output(q.alpha(0.2, img1, img2), "/home/elkhafagy/Desktop/out.png");
    }

}
