import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;

public class matlab1b extends JFrame {

    BufferedImage original, complement, gray, grayComplement;
    BufferedImage c2, d, A, M, S, D;

    public matlab1b() {
        try {
            original = ImageIO.read(new File("picforlab1.jpg"));
            int width = original.getWidth();
            int height = original.getHeight();

            complement = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            gray = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
            grayComplement = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    Color color = new Color(original.getRGB(x, y));

                    Color invColor = new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue());
                    complement.setRGB(x, y, invColor.getRGB());

                    int grayVal = (int)(0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue());
                    Color grayColor = new Color(grayVal, grayVal, grayVal);
                    gray.setRGB(x, y, grayColor.getRGB());

                    int grayComp = 255 - grayVal;
                    Color grayInvColor = new Color(grayComp, grayComp, grayComp);
                    grayComplement.setRGB(x, y, grayInvColor.getRGB());
                }
            }

            generatePatterns();

            // Main images with axis and titles
            JPanel imagePanel = new JPanel(new GridLayout(2, 2, 10, 10));
            imagePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            imagePanel.add(wrapWithLabel("Color Image", original, true));
            imagePanel.add(wrapWithLabel("Complement of Color Image", complement, true));
            imagePanel.add(wrapWithLabel("Grayscale of Color Image", gray, true));
            imagePanel.add(wrapWithLabel("Complement of Grayscale", grayComplement, true));

            JFrame imageFrame = new JFrame("Image Processing");
            imageFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
            imageFrame.setSize(900, 800);
            imageFrame.add(imagePanel);
            imageFrame.setVisible(true);

            // Simulation images (no titles, no axes)
            JPanel simulationPanel = new JPanel(new GridLayout(3, 2, 10, 10));
            simulationPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            simulationPanel.add(new ImagePanel(c2, false));
            simulationPanel.add(new ImagePanel(d, false));
            simulationPanel.add(new ImagePanel(A, false));
            simulationPanel.add(new ImagePanel(M, false));
            simulationPanel.add(new ImagePanel(S, false));
            simulationPanel.add(new ImagePanel(D, false));

            JFrame simFrame = new JFrame("Simulations");
            simFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
            simFrame.setSize(600, 700);
            simFrame.add(simulationPanel);
            simFrame.setVisible(true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generatePatterns() {
        int size = 40;
        c2 = new BufferedImage(size * 2, size * 2, BufferedImage.TYPE_BYTE_GRAY);
        d  = new BufferedImage(size * 2, size * 2, BufferedImage.TYPE_BYTE_GRAY);
        A  = new BufferedImage(size * 2, size * 2, BufferedImage.TYPE_BYTE_GRAY);
        M  = new BufferedImage(size * 2, size * 2, BufferedImage.TYPE_BYTE_GRAY);
        S  = new BufferedImage(size * 2, size * 2, BufferedImage.TYPE_BYTE_GRAY);
        D  = new BufferedImage(size * 2, size * 2, BufferedImage.TYPE_BYTE_GRAY);

        for (int y = 0; y < size * 2; y++) {
            for (int x = 0; x < size * 2; x++) {
                int valC2 = ((x < size && y < size) || (x >= size && y >= size)) ? 255 : 0;
                int valD = (y >= size) ? 255 : 0;

                int valA = Math.min(valC2 + valD, 255);
                int valM = (valC2 > 0 && valD > 0) ? 255 : 0;
                int valS = Math.max(valC2 - valD, 0);  // Fix: ensure lower left of S is black
                int valDiv = valC2 / 4;

                c2.getRaster().setSample(x, y, 0, valC2);
                d.getRaster().setSample(x, y, 0, valD);
                A.getRaster().setSample(x, y, 0, valA);
                M.getRaster().setSample(x, y, 0, valM);
                S.getRaster().setSample(x, y, 0, valS);
                D.getRaster().setSample(x, y, 0, valDiv);
            }
        }
    }

    private JPanel wrapWithLabel(String labelText, BufferedImage img, boolean showAxes) {
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BorderLayout());
        JLabel label = new JLabel(labelText, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        wrapper.add(label, BorderLayout.NORTH);
        wrapper.add(new ImagePanel(img, showAxes), BorderLayout.CENTER);
        return wrapper;
    }

    public static void main(String[] args) {
        new matlab1b();
    }

    class ImagePanel extends JPanel {
        BufferedImage image;
        boolean drawAxes;

        public ImagePanel(BufferedImage img, boolean drawAxes) {
            this.image = img;
            this.drawAxes = drawAxes;
            setPreferredSize(new Dimension(350, 350));
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            int w = getWidth();
            int h = getHeight();

            int marginLeft = drawAxes ? 40 : 0;
            int marginBottom = drawAxes ? 30 : 0;

            int imgW = w - marginLeft - 10;
            int imgH = h - marginBottom - 10;

            g.drawImage(image, marginLeft, 10, imgW, imgH, null);

            if (drawAxes) {
                g.setColor(Color.BLACK);

                // X-axis
                g.drawLine(marginLeft, h - marginBottom, w - 10, h - marginBottom);

                // Y-axis
                g.drawLine(marginLeft, 10, marginLeft, h - marginBottom);

                // Ticks and labels
                g.setFont(new Font("Arial", Font.PLAIN, 10));
                for (int i = 0; i <= 5; i++) {
                    int xTick = marginLeft + i * (imgW) / 5;
                    int yTick = h - marginBottom - i * (imgH) / 5;

                    // X tick
                    g.drawLine(xTick, h - marginBottom - 3, xTick, h - marginBottom + 3);
                    g.drawString(Integer.toString(i * 50), xTick - 10, h - 10);

                    // Y tick
                    g.drawLine(marginLeft - 3, yTick, marginLeft + 3, yTick);
                    g.drawString(Integer.toString(i * 50), 5, yTick + 5);
                }
            }
        }
    }
}