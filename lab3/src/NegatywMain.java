import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.*;


class Negatyw extends Thread{
    BufferedImage picture;
    int xStart, yStart, xStop, yStop;

    public Negatyw(BufferedImage picture, int xStart, int yStart, int xStop, int yStop){
        this.xStart = xStart;
        this.yStart = yStart;
        this.xStop = xStop;
        this.yStop = yStop;
        this.picture = picture;
    }

    @Override
    public void run() {
        for(int i = xStart; i < xStop; i++){
            for(int j = yStart; j < yStop; j++) {
                Color c = new Color(picture.getRGB(i, j));
                int r = c.getRed();
                int g= c.getGreen();
                int b = c.getBlue();

                int R, G, B;

                R = 255 - r;
                G = 255 - g;
                B = 255 - b;

                Color newColor = new Color(R, G, B);
                picture.setRGB(i, j, newColor.getRGB());
            }
        }
    }

}

public class NegatywMain {

    public static void main(String[] args) throws IOException {
        BufferedImage obraz;
        File inputJPG = new File("zwykłyobraz.jpg");
        obraz = ImageIO.read(inputJPG);
        int width = obraz.getWidth();
        int height = obraz.getHeight();
        int halfWidth = width / 2;
        int halfHeight = height / 2;

        Negatyw n1 = new Negatyw(obraz, 0, 0, halfWidth, halfHeight);
        Negatyw n2 = new Negatyw(obraz, halfWidth, 0, width, halfHeight);
        Negatyw n3 = new Negatyw(obraz, 0, halfHeight, halfWidth, height);
        Negatyw n4 = new Negatyw(obraz, halfWidth, halfHeight, width, height);

        n1.start();
        n2.start();
        n3.start();
        n4.start();

        try {
            n1.join();
            n2.join();
            n3.join();
            n4.join();
        } catch (Exception e) { }

        File ouptutJPG = new File("obrazNegatyw.jpg");
        ImageIO.write(obraz, "jpg", ouptutJPG);
    }

}