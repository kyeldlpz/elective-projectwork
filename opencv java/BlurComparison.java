import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class BlurComparison {
    public static void main(String[] args) throws IOException {
        String path = (args.length > 0) ? args[0] : "forlab3.jpg";
        BufferedImage src = ImageIO.read(new File(path));

        BufferedImage original = addLabel(src, "Original");
        BufferedImage homogenous = addLabel(applyBoxBlur(src), "Homogeneous Blur");
        BufferedImage gaussian = addLabel(applyGaussianBlur(src), "Gaussian Blur");
        BufferedImage median = addLabel(applyMedianBlur(src), "Median Blur");
        BufferedImage bilateral = addLabel(applyBilateralApprox(src), "Bilateral Filter");

        int width = original.getWidth();
        int height = original.getHeight();

        BufferedImage combined = new BufferedImage(width * 5, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = combined.createGraphics();
        g2.drawImage(original, 0, 0, null);
        g2.drawImage(homogenous, width, 0, null);
        g2.drawImage(gaussian, width * 2, 0, null);
        g2.drawImage(median, width * 3, 0, null);
        g2.drawImage(bilateral, width * 4, 0, null);
        g2.dispose();

        // Show in window
        JFrame frame = new JFrame("Blur Comparison");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new JLabel(new ImageIcon(combined)));
        frame.pack();
        frame.setVisible(true);
    }

    // Adds text label to image
    private static BufferedImage addLabel(BufferedImage img, String text) {
        BufferedImage labeled = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = labeled.createGraphics();
        g2.drawImage(img, 0, 0, null);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.drawString(text, 10, 25);
        g2.dispose();
        return labeled;
    }

    // Basic Box Blur (average)
    private static BufferedImage applyBoxBlur(BufferedImage img) {
        return convolve(img, makeKernel(3, 1.0 / 9));
    }

    // Approximate Gaussian Blur
    private static BufferedImage applyGaussianBlur(BufferedImage img) {
        float[] kernel = {
            1f / 16, 2f / 16, 1f / 16,
            2f / 16, 4f / 16, 2f / 16,
            1f / 16, 2f / 16, 1f / 16
        };
        return convolve(img, new Kernel(3, 3, kernel));
    }

    // Median blur
    private static BufferedImage applyMedianBlur(BufferedImage img) {
        int w = img.getWidth(), h = img.getHeight();
        BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        for (int y = 1; y < h - 1; y++) {
            for (int x = 1; x < w - 1; x++) {
                int[] r = new int[9], g = new int[9], b = new int[9];
                int idx = 0;
                for (int dy = -1; dy <= 1; dy++)
                    for (int dx = -1; dx <= 1; dx++) {
                        Color c = new Color(img.getRGB(x + dx, y + dy));
                        r[idx] = c.getRed();
                        g[idx] = c.getGreen();
                        b[idx++] = c.getBlue();
                    }
                Arrays.sort(r);
                Arrays.sort(g);
                Arrays.sort(b);
                output.setRGB(x, y, new Color(r[4], g[4], b[4]).getRGB());
            }
        }
        return output;
    }

    // Simple bilateral approximation (blur while preserving edges)
    private static BufferedImage applyBilateralApprox(BufferedImage img) {
        BufferedImage blur = applyBoxBlur(img);
        int w = img.getWidth(), h = img.getHeight();
        BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++) {
                Color orig = new Color(img.getRGB(x, y));
                Color blurred = new Color(blur.getRGB(x, y));
                int r = (orig.getRed() + blurred.getRed()) / 2;
                int g = (orig.getGreen() + blurred.getGreen()) / 2;
                int b = (orig.getBlue() + blurred.getBlue()) / 2;
                result.setRGB(x, y, new Color(r, g, b).getRGB());
            }
        return result;
    }

    // Utility to convolve an image with a kernel
    private static BufferedImage convolve(BufferedImage img, Kernel kernel) {
        BufferedImage dest = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        op.filter(img, dest);
        return dest;
    }

    private static Kernel makeKernel(int size, double value) {
        float[] data = new float[size * size];
        Arrays.fill(data, (float) value);
        return new Kernel(size, size, data);
    }
}
