import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.Scanner;

public class lab2secondjava {
    
    public static void main(String[] args) {
        try {
            // Load image
            BufferedImage originalImage = ImageIO.read(new File("picforlab1.jpg"));
            
            // Get scaling factor from user
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter Scaling Factor: ");
            double scaleFactor = scanner.nextDouble();
            
            // Manual scaling
            BufferedImage scaledImage = manualResize(originalImage, scaleFactor);
            
            // Manual rotation by 60 degrees
            BufferedImage rotated60 = manualRotate(scaledImage, 60);
            
            // Manual rotation by 45 degrees
            BufferedImage rotated45 = manualRotate(scaledImage, 45);
            
            // Display images in a window
            displayImages(originalImage, scaledImage, rotated60, rotated45);
            
            // Save processed images
            ImageIO.write(scaledImage, "jpg", new File("scaled_image.jpg"));
            ImageIO.write(rotated60, "jpg", new File("rotated_60deg.jpg"));
            ImageIO.write(rotated45, "jpg", new File("rotated_45deg.jpg"));
            
            scanner.close();
            
        } catch (IOException e) {
            System.err.println("Error loading image: " + e.getMessage());
        }
    }
    
    // Manual resize function using bilinear interpolation
    public static BufferedImage manualResize(BufferedImage img, double scaleFactor) {
        int originalWidth = img.getWidth();
        int originalHeight = img.getHeight();
        
        // Calculate new dimensions
        int newWidth = (int) Math.round(originalWidth * scaleFactor);
        int newHeight = (int) Math.round(originalHeight * scaleFactor);
        
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, img.getType());
        
        // Perform bilinear interpolation
        for (int y = 0; y < newHeight; y++) {
            for (int x = 0; x < newWidth; x++) {
                // Map new coordinates back to original image coordinates
                double origX = (x / scaleFactor);
                double origY = (y / scaleFactor);
                
                // Get the interpolated color
                int interpolatedColor = bilinearInterpolation(img, origX, origY);
                resizedImage.setRGB(x, y, interpolatedColor);
            }
        }
        
        return resizedImage;
    }
    
    // Manual rotate function
    public static BufferedImage manualRotate(BufferedImage img, double angleDeg) {
        int width = img.getWidth();
        int height = img.getHeight();
        
        // Convert angle to radians
        double theta = Math.toRadians(angleDeg);
        double cosTheta = Math.cos(theta);
        double sinTheta = Math.sin(theta);
        
        // Find corners of original image
        double[][] corners = {{0, 0}, {width-1, 0}, {width-1, height-1}, {0, height-1}};
        double centerX = width / 2.0;
        double centerY = height / 2.0;
        
        // Find bounding box of rotated image
        double minX = Double.MAX_VALUE, maxX = Double.MIN_VALUE;
        double minY = Double.MAX_VALUE, maxY = Double.MIN_VALUE;
        
        for (double[] corner : corners) {
            double x = corner[0] - centerX;
            double y = corner[1] - centerY;
            
            double rotatedX = x * cosTheta - y * sinTheta;
            double rotatedY = x * sinTheta + y * cosTheta;
            
            minX = Math.min(minX, rotatedX);
            maxX = Math.max(maxX, rotatedX);
            minY = Math.min(minY, rotatedY);
            maxY = Math.max(maxY, rotatedY);
        }
        
        // Calculate new image dimensions
        int newWidth = (int) Math.ceil(maxX - minX);
        int newHeight = (int) Math.ceil(maxY - minY);
        
        BufferedImage rotatedImage = new BufferedImage(newWidth, newHeight, img.getType());
        
        // Center of new image
        double newCenterX = newWidth / 2.0;
        double newCenterY = newHeight / 2.0;
        
        // Fill the rotated image
        for (int y = 0; y < newHeight; y++) {
            for (int x = 0; x < newWidth; x++) {
                // Convert new image coordinates to original image coordinates
                double xCentered = x - newCenterX;
                double yCentered = y - newCenterY;
                
                // Apply inverse rotation
                double origX = cosTheta * xCentered + sinTheta * yCentered + centerX;
                double origY = -sinTheta * xCentered + cosTheta * yCentered + centerY;
                
                // Get interpolated color if within bounds
                if (origX >= 0 && origX < width && origY >= 0 && origY < height) {
                    int interpolatedColor = bilinearInterpolation(img, origX, origY);
                    rotatedImage.setRGB(x, y, interpolatedColor);
                } else {
                    rotatedImage.setRGB(x, y, 0); // Black background
                }
            }
        }
        
        return rotatedImage;
    }
    
    // Bilinear interpolation helper function
    private static int bilinearInterpolation(BufferedImage img, double x, double y) {
        int x1 = (int) Math.floor(x);
        int y1 = (int) Math.floor(y);
        int x2 = Math.min(x1 + 1, img.getWidth() - 1);
        int y2 = Math.min(y1 + 1, img.getHeight() - 1);
        
        // Ensure coordinates are within bounds
        x1 = Math.max(0, Math.min(x1, img.getWidth() - 1));
        y1 = Math.max(0, Math.min(y1, img.getHeight() - 1));
        
        double dx = x - x1;
        double dy = y - y1;
        
        // Get the four surrounding pixels
        int rgb11 = img.getRGB(x1, y1);
        int rgb21 = img.getRGB(x2, y1);
        int rgb12 = img.getRGB(x1, y2);
        int rgb22 = img.getRGB(x2, y2);
        
        // Extract RGB components
        int[] c11 = {(rgb11 >> 16) & 0xFF, (rgb11 >> 8) & 0xFF, rgb11 & 0xFF};
        int[] c21 = {(rgb21 >> 16) & 0xFF, (rgb21 >> 8) & 0xFF, rgb21 & 0xFF};
        int[] c12 = {(rgb12 >> 16) & 0xFF, (rgb12 >> 8) & 0xFF, rgb12 & 0xFF};
        int[] c22 = {(rgb22 >> 16) & 0xFF, (rgb22 >> 8) & 0xFF, rgb22 & 0xFF};
        
        // Interpolate each channel
        int[] result = new int[3];
        for (int i = 0; i < 3; i++) {
            double top = c11[i] * (1 - dx) + c21[i] * dx;
            double bottom = c12[i] * (1 - dx) + c22[i] * dx;
            result[i] = (int) Math.round(top * (1 - dy) + bottom * dy);
            result[i] = Math.max(0, Math.min(255, result[i])); // Clamp to [0, 255]
        }
        
        return (result[0] << 16) | (result[1] << 8) | result[2];
    }
    
    // Display images in a JFrame
    private static void displayImages(BufferedImage original, BufferedImage scaled, 
                                    BufferedImage rotated60, BufferedImage rotated45) {
        JFrame frame = new JFrame("Image Processing Results");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(2, 2));
        
        // Scale images for display if they're too large
        int maxDisplaySize = 300;
        
        frame.add(createImagePanel(scaleForDisplay(original, maxDisplaySize), "Original Image"));
        frame.add(createImagePanel(scaleForDisplay(scaled, maxDisplaySize), "Scaled Image"));
        frame.add(createImagePanel(scaleForDisplay(rotated60, maxDisplaySize), "Rotated 60deg"));
        frame.add(createImagePanel(scaleForDisplay(rotated45, maxDisplaySize), "Rotated 45deg"));
        
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    private static JPanel createImagePanel(BufferedImage img, String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(title), BorderLayout.NORTH);
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
        
        BufferedImage scaled = new BufferedImage(newWidth, newHeight, img.getType());
        Graphics2D g2d = scaled.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(img, 0, 0, newWidth, newHeight, null);
        g2d.dispose();
        
        return scaled;
    }
}