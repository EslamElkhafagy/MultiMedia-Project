package multi_media;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import javax.imageio.ImageIO;

public class order {

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

    public Color[][] order(BufferedImage bf) {
        int n = 2;
        int[][] ths = {{10, 20}, {30, 40}};
        Color[][] matrix_res = new Color[bf.getWidth()][bf.getHeight()];
        Color[][] matrix = initmatrix(bf);
        for (int x = 0; x < matrix.length; x++) {
            for (int y = 0; y < matrix[0].length; y++) {
                int red = matrix[x][y].getRed();
                int green = matrix[x][y].getGreen();
                int blue = matrix[x][y].getBlue();
                int j = y % n;
                int i = x % n;
                int gray = (int) ((red + green + blue) / 3);
                if (gray > ths[i][j]) {
                    matrix_res[x][y] = new Color(255, 255, 255);
                } else {
                    matrix_res[x][y] = new Color(0, 0, 0);
                }

            }
        }
        return matrix_res;
    }

    public static void main(String[] args) {
        order q = new order();
        BufferedImage img = q.get_img("/home/elkhafagy/Desktop/1.jpg");
        q.output(q.order(img), "/home/elkhafagy/Desktop/2.jpg");
    }
}
