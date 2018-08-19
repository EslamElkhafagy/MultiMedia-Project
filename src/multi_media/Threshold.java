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
public class Threshold {

    private BufferedImage BFimage;
    private BufferedImage AFimage;
    private int width = 282;// intial number
    private int height = 383;//// intial number
    private int arr[][];

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

            int av = 0;
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int pixel = img.getRGB(x, y);
                    int a = (pixel >> 24) & 0x0FF;
                    int r = (pixel >> 16) & 0x0FF;
                    int g = (pixel >> 8) & 0x0FF;
                    int b = (pixel) & 0x0FF;
                    matrix1[x][y] = new Color(r, g, b, a);
                    av += (int) ((r + g + b) / 3);
//                    arr[x][y]=av;
                }
            }
            System.out.println("avg " + av / (matrix1.length * matrix1[0].length));
        } catch (Exception e) {
            System.out.println("here+++++++>  " + e.getMessage());
            e.printStackTrace();
        }
        return matrix1;

    }

    public void output(Color[][] matrix, String uri) {
//       ..........................");

        this.arr = new int[width][height / 2];

        try {
            File f = new File(uri);
            FileInputStream fs = new FileInputStream(f);
            AFimage = new BufferedImage(width, height / 2, BufferedImage.TYPE_INT_RGB);

            for (int x = 0; x < matrix.length; x++) {
                int d = 0;
                for (int y = 0; y < matrix[x].length; y++) {
                    if (y >= matrix[x].length / 2 && d<height/2) {
                        AFimage.setRGB(x, d, matrix[x][y].getRGB());
                        int red = matrix[x][y].getRed();
                        int green = matrix[x][y].getGreen();
                        int blue = matrix[x][y].getBlue();

                        //Color color = new Color(red, green, blue);
                        int lum = (int) ((red + green + blue) / 3);
                        arr[x][d] = lum;
                        d++;

                    }
                }
            }
            ImageIO.write(AFimage, "png", f);

        } catch (Exception e) {
            System.out.println("here+++++++>  " + e.getMessage());
            e.printStackTrace();
        }
        int max = 0, min = 0,avg=0;
        System.out.println("tol   =   "+arr.length);
        System.out.println("3ard    =   "+arr[0].length);
        for (int x = 0; x < arr.length; x++) {
            System.out.println("x =  " + x + " ");
            for (int y = 0; y < arr[x].length / 2; y++) {
//                System.out.print("  " + arr[x][y] + " ");
                if (arr[x][y] == 0) {
                    min++;
                } else {
                    max++;
                }
                
                if(x==y){
                    avg+=arr[x][y];
                }
            }
            System.out.println("");
        }
        System.out.println("...........................................");
//        System.out.printl
        System.out.println("max =  " + max);
        System.out.println("min =  " + min);
        System.out.println("avg  =  "+avg);
        
        
       if(avg>7000){
           System.out.println("Smile");
       }else{
           System.out.println("Angry");
       }
    }

    public Color[][] threshold(BufferedImage b) {

        {
            
            this.width = b.getWidth();
            this.height = b.getHeight();
            // Random rn = new Random();
            Color[][] matrix_res = new Color[b.getWidth()][b.getHeight()];
            Color[][] matrix = initmatrix(b);

            for (int x = 0; x < matrix.length; x++) {
                for (int y = 0; y < matrix[0].length; y++) {
//                    System.out.println("matrix[x][y].getRed()  =  "+matrix[x][y].getRed());
                    int red = matrix[x][y].getRed();
                    int green = matrix[x][y].getGreen();
                    int blue = matrix[x][y].getBlue();

                    //Color color = new Color(red, green, blue);
                    int lum = (int) ((red + green + blue) / 3);

                    if (lum < 128) {
                        matrix_res[x][y] = new Color(0, 0, 0);
//                        arr[x][y]=0;

                    } else {
                        matrix_res[x][y] = new Color(255, 255, 255);
//arr[x][y]=255;
                    }

                }
            }
            return matrix_res;
        }
    }

    public static void main(String[] args) {
        
        int threshold =12000;
        
        Threshold q = new Threshold();
        BufferedImage img = q.get_img("/home/elkhafagy/Desktop/mostafasad1.png");
        q.output(q.threshold(img), "/home/elkhafagy/Desktop/mostafasad10.png");

        
//        BufferedImage img = q.get_img("/home/elkhafagy/Desktop/5555.png");
//        q.output(q.threshold(img), "/home/elkhafagy/Desktop/55550.png");

        
    }
}
