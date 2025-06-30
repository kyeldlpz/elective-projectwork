# PYTHON VERSION
import cv2
import matplotlib.pyplot as plt # type: ignore
import numpy as np

# Red Blue and Green and Gray Components
i = cv2.imread('picforlab1.jpg')
i = cv2.cvtColor(i, cv2.COLOR_BGR2RGB)  # Convert BGR to RGB

plt.figure(figsize=(12, 8))

# Original Image
plt.subplot(3, 2, 1)
plt.imshow(i)
plt.title('Original Image')
plt.axis('off')

# Red Component
r = i[:, :, 0]
plt.subplot(3, 2, 2)
plt.imshow(r, cmap='gray')
plt.title('Red Component')
plt.axis('off')

# Green Component
g = i[:, :, 1]
plt.subplot(3, 2, 3)
plt.imshow(g, cmap='gray')
plt.title('Green Component')
plt.axis('off')

# Blue Component
b = i[:, :, 2]
plt.subplot(3, 2, 4)
plt.imshow(b, cmap='gray')
plt.title('Blue Component')
plt.axis('off')

# Color to Gray - note that 0.2989 for red, 0.5870 for green, 0.1140 for blue
rg = 0.2989 * i[:, :, 0] + 0.5870 * i[:, :, 1] + 0.1140 * i[:, :, 2]
rg = rg.astype(np.uint8)
plt.subplot(3, 2, 5)
plt.imshow(rg, cmap='gray')
plt.title('Gray Image')
plt.axis('off')

plt.tight_layout()
plt.show()

# ================================================================
# JAVA VERSION (using BufferedImage and Graphics2D)
"""
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class ImageProcessing extends JFrame {
    public static void main(String[] args) {
        new ImageProcessing().processImage();
    }
    
    public void processImage() {
        try {
            // Red Blue and Green and Gray Components
            BufferedImage i = ImageIO.read(new File("picforlab1.jpg"));
            
            int width = i.getWidth();
            int height = i.getHeight();
            
            // Create component images
            BufferedImage r = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
            BufferedImage g = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
            BufferedImage b = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
            BufferedImage rg = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
            
            // Extract components and create grayscale
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    Color pixel = new Color(i.getRGB(x, y));
                    int red = pixel.getRed();
                    int green = pixel.getGreen();
                    int blue = pixel.getBlue();
                    
                    // Red Component
                    r.setRGB(x, y, new Color(red, red, red).getRGB());
                    
                    // Green Component
                    g.setRGB(x, y, new Color(green, green, green).getRGB());
                    
                    // Blue Component
                    b.setRGB(x, y, new Color(blue, blue, blue).getRGB());
                    
                    // Color to Gray - note that 0.2989 for red, 0.5870 for green, 0.1140 for blue
                    int gray = (int)(0.2989 * red + 0.5870 * green + 0.1140 * blue);
                    rg.setRGB(x, y, new Color(gray, gray, gray).getRGB());
                }
            }
            
            // Display images in a JFrame
            displayImages(i, r, g, b, rg);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void displayImages(BufferedImage original, BufferedImage red, 
                              BufferedImage green, BufferedImage blue, BufferedImage gray) {
        setTitle("RGB Components and Gray Image");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(2, 3));
        
        add(createImagePanel(original, "Original Image"));
        add(createImagePanel(red, "Red Component"));
        add(createImagePanel(green, "Green Component"));
        add(createImagePanel(blue, "Blue Component"));
        add(createImagePanel(gray, "Gray Image"));
        
        pack();
        setVisible(true);
    }
    
    private JPanel createImagePanel(BufferedImage img, String title) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(new ImageIcon(img));
        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }
}
"""