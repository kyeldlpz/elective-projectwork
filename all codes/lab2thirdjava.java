import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;

public class lab2thirdjava extends JPanel {
    private final BufferedImage[] images;
    private final String[] titles = {
        "Original Image", "Bilinear Image", "Nearest Image", "Bicubic Image"
    };

    public lab2thirdjava(BufferedImage original) {
        int newWidth = original.getWidth() / 2;
        int newHeight = original.getHeight() / 2;

        images = new BufferedImage[4];
        images[0] = original;
        images[1] = resizeImage(original, newWidth, newHeight, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        images[2] = resizeImage(original, newWidth, newHeight, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        images[3] = resizeImage(original, newWidth, newHeight, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    }

    private BufferedImage resizeImage(BufferedImage src, int width, int height, Object interpolationHint) {
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, interpolationHint);
        g2d.drawImage(src, 0, 0, width, height, null);
        g2d.dispose();
        return resized;
    }

    private int getGridSpacing(int size) {
        if (size <= 500) return 100;
        else if (size <= 1000) return 200;
        else if (size <= 2000) return 400;
        else return 500;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int pad = 40; // Padding between cells
        int labelHeight = 20;
        int tickSize = 5;
        int tickFontSize = 10;
        int rows = 2;
        int cols = 2;

        int panelW = getWidth();
        int panelH = getHeight();
        int cellW = (panelW - (cols + 1) * pad) / cols;
        int cellH = (panelH - (rows + 1) * pad) / rows;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setFont(new Font("SansSerif", Font.BOLD, 14));
        g2d.setColor(Color.BLACK);

        for (int i = 0; i < images.length; i++) {
            int row = i / cols;
            int col = i % cols;

            int x = pad + col * (cellW + pad);
            int y = pad + row * (cellH + pad);

            BufferedImage img = images[i];

            // --- Centered title above the image ---
            FontMetrics fm = g2d.getFontMetrics();
            int titleWidth = fm.stringWidth(titles[i]);
            int titleX = x + (cellW - titleWidth) / 2;
            g2d.drawString(titles[i], titleX, y);

            int imgX = x;
            int imgY = y + labelHeight;

            int imgW = cellW;
            int imgH = cellH - labelHeight;

            // --- Draw image ---
            g2d.drawImage(img, imgX, imgY, imgW, imgH, null);

            // --- Grid overlay ---
            int gridSpacing = getGridSpacing(img.getWidth());
            g2d.setColor(new Color(255, 255, 255, 80));
            for (int gx = 0; gx <= imgW; gx += gridSpacing * imgW / img.getWidth()) {
                g2d.drawLine(imgX + gx, imgY, imgX + gx, imgY + imgH);
            }
            for (int gy = 0; gy <= imgH; gy += gridSpacing * imgH / img.getHeight()) {
                g2d.drawLine(imgX, imgY + gy, imgX + imgW, imgY + gy);
            }

            // --- Axis ticks and labels ---
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("SansSerif", Font.PLAIN, tickFontSize));

            int xTickSpacing = getGridSpacing(img.getWidth());
            int yTickSpacing = getGridSpacing(img.getHeight());

            for (int gx = 0; gx <= img.getWidth(); gx += xTickSpacing) {
                int screenX = imgX + gx * imgW / img.getWidth();
                g2d.drawLine(screenX, imgY + imgH, screenX, imgY + imgH + tickSize);
                String label = Integer.toString(gx);
                int strWidth = g2d.getFontMetrics().stringWidth(label);
                g2d.drawString(label, screenX - strWidth / 2, imgY + imgH + tickSize + 12);
            }

            for (int gy = 0; gy <= img.getHeight(); gy += yTickSpacing) {
                int screenY = imgY + gy * imgH / img.getHeight();
                g2d.drawLine(imgX - tickSize, screenY, imgX, screenY);
                g2d.drawString(Integer.toString(gy), imgX - tickSize - 25, screenY + 5);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedImage img = ImageIO.read(new File("formalpicturekzy.jpg"));

        JFrame frame = new JFrame("Image Resizing with Grid and Axes");
        lab2thirdjava panel = new lab2thirdjava(img);
        frame.add(panel);
        frame.setSize(1200, 900);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
