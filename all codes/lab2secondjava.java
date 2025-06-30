import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.*;

public class lab2secondjava {

    public static void main(String[] args) {
        try {
            BufferedImage originalImage = ImageIO.read(new File("picforlab1.jpg"));

            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter Scaling Factor: ");
            double scaleFactor = scanner.nextDouble();
            scanner.close();

            BufferedImage scaledImage = manualResize(originalImage, scaleFactor);
            BufferedImage originalWithAxes = addAxesToImage(originalImage, "Original");
            BufferedImage scaledWithAxes = addAxesToImage(scaledImage, "Scaled");

            BufferedImage rotated60 = manualRotate(scaledImage, 60);
            BufferedImage rotated45 = manualRotate(scaledImage, 45);

            displayImages(originalWithAxes, scaledWithAxes, rotated60, rotated45);

            ImageIO.write(scaledImage, "jpg", new File("scaled_image.jpg"));
            ImageIO.write(rotated60, "jpg", new File("rotated_60deg.jpg"));
            ImageIO.write(rotated45, "jpg", new File("rotated_45deg.jpg"));

        } catch (IOException e) {
            System.err.println("Error loading image: " + e.getMessage());
        }
    }

    public static BufferedImage manualResize(BufferedImage img, double scaleFactor) {
        int originalWidth = img.getWidth();
        int originalHeight = img.getHeight();

        int newWidth = (int) Math.round(originalWidth * scaleFactor);
        int newHeight = (int) Math.round(originalHeight * scaleFactor);

        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < newHeight; y++) {
            for (int x = 0; x < newWidth; x++) {
                double origX = x / scaleFactor;
                double origY = y / scaleFactor;
                int color = bilinearInterpolation(img, origX, origY);
                resizedImage.setRGB(x, y, color);
            }
        }

        return resizedImage;
    }

    public static BufferedImage manualRotate(BufferedImage img, double angleDeg) {
        int width = img.getWidth();
        int height = img.getHeight();

        double theta = Math.toRadians(angleDeg);
        double cosTheta = Math.cos(theta);
        double sinTheta = Math.sin(theta);

        double centerX = width / 2.0;
        double centerY = height / 2.0;

        double minX = Double.MAX_VALUE, maxX = Double.MIN_VALUE;
        double minY = Double.MAX_VALUE, maxY = Double.MIN_VALUE;

        for (int[] corner : new int[][]{{0, 0}, {width, 0}, {width, height}, {0, height}}) {
            double x = corner[0] - centerX;
            double y = corner[1] - centerY;

            double rotatedX = x * cosTheta - y * sinTheta;
            double rotatedY = x * sinTheta + y * cosTheta;

            minX = Math.min(minX, rotatedX);
            maxX = Math.max(maxX, rotatedX);
            minY = Math.min(minY, rotatedY);
            maxY = Math.max(maxY, rotatedY);
        }

        int newWidth = (int) Math.ceil(maxX - minX);
        int newHeight = (int) Math.ceil(maxY - minY);
        double newCenterX = newWidth / 2.0;
        double newCenterY = newHeight / 2.0;

        BufferedImage rotatedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < newHeight; y++) {
            for (int x = 0; x < newWidth; x++) {
                double xCentered = x - newCenterX;
                double yCentered = y - newCenterY;

                double origX = cosTheta * xCentered + sinTheta * yCentered + centerX;
                double origY = -sinTheta * xCentered + cosTheta * yCentered + centerY;

                if (origX >= 0 && origX < width && origY >= 0 && origY < height) {
                    int color = bilinearInterpolation(img, origX, origY);
                    rotatedImage.setRGB(x, y, color);
                } else {
                    rotatedImage.setRGB(x, y, 0); // black background
                }
            }
        }

        return rotatedImage;
    }

    private static int bilinearInterpolation(BufferedImage img, double x, double y) {
        int x1 = (int) Math.floor(x);
        int y1 = (int) Math.floor(y);
        int x2 = Math.min(x1 + 1, img.getWidth() - 1);
        int y2 = Math.min(y1 + 1, img.getHeight() - 1);

        x1 = Math.max(0, x1);
        y1 = Math.max(0, y1);

        double dx = x - x1;
        double dy = y - y1;

        int rgb11 = img.getRGB(x1, y1);
        int rgb21 = img.getRGB(x2, y1);
        int rgb12 = img.getRGB(x1, y2);
        int rgb22 = img.getRGB(x2, y2);

        int[] result = new int[3];
        for (int i = 0; i < 3; i++) {
            int c11 = (rgb11 >> (16 - 8 * i)) & 0xFF;
            int c21 = (rgb21 >> (16 - 8 * i)) & 0xFF;
            int c12 = (rgb12 >> (16 - 8 * i)) & 0xFF;
            int c22 = (rgb22 >> (16 - 8 * i)) & 0xFF;

            double top = c11 * (1 - dx) + c21 * dx;
            double bottom = c12 * (1 - dx) + c22 * dx;
            result[i] = (int) Math.round(top * (1 - dy) + bottom * dy);
        }

        return (result[0] << 16) | (result[1] << 8) | result[2];
    }

    private static void displayImages(BufferedImage original, BufferedImage scaled,
                                      BufferedImage rotated60, BufferedImage rotated45) {
        JFrame frame = new JFrame("Image Processing Results");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(2, 2));

        int maxDisplaySize = 300;

        frame.add(createImagePanel(scaleForDisplay(original, maxDisplaySize), "Original Image"));
        frame.add(createImagePanel(scaleForDisplay(scaled, maxDisplaySize), "Scaled Image"));
        frame.add(createImagePanel(scaleForDisplay(rotated60, maxDisplaySize), "Rotated 60°"));
        frame.add(createImagePanel(scaleForDisplay(rotated45, maxDisplaySize), "Rotated 45°"));

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static JPanel createImagePanel(BufferedImage img, String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(title, JLabel.CENTER), BorderLayout.NORTH);
        panel.add(new JLabel(new ImageIcon(img)), BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createEtchedBorder());
        return panel;
    }

    private static BufferedImage scaleForDisplay(BufferedImage img, int maxSize) {
        int width = img.getWidth();
        int height = img.getHeight();

        if (width <= maxSize && height <= maxSize) {
            return img;
        }

        double scale = Math.min((double) maxSize / width, (double) maxSize / height);
        int newWidth = (int) (width * scale);
        int newHeight = (int) (height * scale);

        BufferedImage scaled = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = scaled.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(img, 0, 0, newWidth, newHeight, null);
        g2d.dispose();

        return scaled;
    }

    private static BufferedImage addAxesToImage(BufferedImage img, String label) {
        final int width = img.getWidth();
        final int height = img.getHeight();

        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = result.createGraphics();
        g.drawImage(img, 0, 0, null);

        final int tickSpacing = 50;
        final int tickSize = 5;
        final Font labelFont = new Font("Arial", Font.BOLD, 14);
        g.setFont(labelFont);
        FontMetrics fm = g.getFontMetrics();

        // Draw axes lines
        g.setStroke(new BasicStroke(2));
        g.setColor(Color.RED);
        g.drawLine(0, 0, 0, height);         // Y-axis
        g.drawLine(0, height - 1, width, height - 1); // X-axis

        // Draw origin marker
        g.setColor(Color.GREEN);
        g.fillOval(-3, height - 4, 7, 7); // At (0, height-1)

        // Tick marks and labels
        g.setColor(Color.YELLOW);

        for (int x = 0; x < width; x += tickSpacing) {
            g.drawLine(x, height - tickSize, x, height);
            String xLabel = String.valueOf(x);
            int labelWidth = fm.stringWidth(xLabel);
            g.drawString(xLabel, x - labelWidth / 2, height - tickSize - 2);
        }

        for (int y = 0; y < height; y += tickSpacing) {
            g.drawLine(0, y, tickSize, y);
            String yLabel = String.valueOf(y);
            g.drawString(yLabel, tickSize + 2, y + fm.getAscent() / 2);
        }

        // Draw X and Y axis labels
        g.setColor(Color.CYAN);
        g.drawString("X", width - 20, height - 10);
        g.drawString("Y", 10, 20);

        g.dispose();
        return result;
    }
}
