import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Julia extends Thread {
    int me;
    static int[][] set = new int[4096][4096];

    public Julia(int me) {
        this.me = me;
    }

    public void run() {
        int begin = 0;
        int end = 0;
        if (this.me == 0) {
            begin = 0;
            end = 1024;
        } else if (this.me == 1) {
            begin = 1024;
            end = 2048;
        } else if (this.me == 2) {
            begin = 2048;
            end = 3072;
        } else if (this.me == 3) {
            begin = 3072;
            end = 4096;
        }

        for(int i = begin; i < end; ++i) {
            for(int j = 0; j < 4096; ++j) {
                double cr = 1.5D * (double)(i - 2048) / 2048.0D;
                double ci = (double)(j - 2048) / 2048.0D;
                double zr = cr,zi = ci;

                int k;
                for(k = 0; k < 1000 && zr * zr + zi * zi < 4.0D; ++k) {
                    double newr = zr * zr - zi * zi - 0.7D;
                    double newi = 2.0D * zr * zi + 0.27015D;
                    zr = newr;
                    zi = newi;
                }

                set[i][j] = k;
            }
        }

    }

    public static void main(String[] args) {
        Julia thread0 = new Julia(0);
        Julia thread1 = new Julia(1);
        Julia thread2 = new Julia(2);
        Julia thread3 = new Julia(3);
        thread0.start();
        thread1.start();
        thread2.start();
        thread3.start();

        try {
            thread0.join();
            thread1.join();
            thread2.join();
            thread3.join();
        } catch (InterruptedException var12) {
        }
//Wyswietlenie
        BufferedImage img = new BufferedImage(4096, 4096, 2);
//Rysowanie pix
        for(int i = 0; i < 4096; ++i) {
            for(int j = 0; j < 4096; ++j) {
                int k = set[i][j];
                float level;
                if (k < 100) {
                    level = (float)k / 100.0F;
                } else {
                    level = 0.0F;
                }

                Color c = new Color(0.0F, level, 0.0F);
                img.setRGB(i, j, c.getRGB());
            }
        }
//Zapis pliku
        try {
            ImageIO.write(img, "PNG", new File("Julia.png"));
        } catch (IOException var11) {
            var11.printStackTrace();
        }

    }
}