import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SimpleBlur {

    public static void main(String[] args) throws IOException {
        BufferedImage original = ImageIO.read(new File("forlab3.jpg"));
        BufferedImage blurred = applySimpleBlur(original, 3); // 3x3 kernel

        JFrame frame = new JFrame("Image Blur Demo");
        frame.setLayout(new GridLayout(1, 2));
        frame.add(new JLabel(new ImageIcon(original)));
        frame.add(new JLabel(new ImageIcon(blurred)));

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public static BufferedImage applySimpleBlur(BufferedImage img, int kernelSize) {
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage output = new BufferedImage(w, h, img.getType());

        int radius = kernelSize / 2;
        for (int y = radius; y < h - radius; y++) {
            for (int x = radius; x < w - radius; x++) {
                int r = 0, g = 0, b = 0, count = 0;

                for (int ky = -radius; ky <= radius; ky++) {
                    for (int kx = -radius; kx <= radius; kx++) {
                        int rgb = img.getRGB(x + kx, y + ky);
                        r += (rgb >> 16) & 0xFF;
                        g += (rgb >> 8) & 0xFF;
                        b += rgb & 0xFF;
                        count++;
                    }
                }

                int avgR = r / count;
                int avgG = g / count;
                int avgB = b / count;
                int newRgb = (avgR << 16) | (avgG << 8) | avgB;
                output.setRGB(x, y, newRgb);
            }
        }

        return output;
    }
}
