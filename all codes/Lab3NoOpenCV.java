import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Lab3NoOpenCV {
    
    public static void main(String[] args) {
        try {
            BufferedImage src = ImageIO.read(new File("forlab3.jpg"));
            if (src == null) {
                System.out.println("Image not found.");
                return;
            }
            
            int k = 9;
            
            // Apply different filters
            BufferedImage blur = applyHomogeneousBlur(src, k);
            BufferedImage gaussian = applyGaussianBlur(src, k);
            BufferedImage median = applyMedianBlur(src, k);
            BufferedImage bilateral = applyBilateralFilter(src, k);
            
            int thumbWidth = 200;
            
            // Resize images
            BufferedImage originalImg = resizePreserveAspect(src, thumbWidth);
            BufferedImage blurImg = resizePreserveAspect(blur, thumbWidth);
            BufferedImage gaussianImg = resizePreserveAspect(gaussian, thumbWidth);
            BufferedImage medianImg = resizePreserveAspect(median, thumbWidth);
            BufferedImage bilateralImg = resizePreserveAspect(bilateral, thumbWidth);
            
            showCombinedImages(
                new BufferedImage[]{originalImg, blurImg, gaussianImg, medianImg, bilateralImg},
                new String[]{"Original", "Homogeneous Blur", "Gaussian Blur", "Median Blur", "Bilateral Filter"}
            );
            
        } catch (IOException e) {
            System.out.println("Error reading image: " + e.getMessage());
        }
    }
    
    // Homogeneous blur (Box filter)
    public static BufferedImage applyHomogeneousBlur(BufferedImage src, int kernelSize) {
        float weight = 1.0f / (kernelSize * kernelSize);
        float[] kernelData = new float[kernelSize * kernelSize];
        Arrays.fill(kernelData, weight);
        
        Kernel kernel = new Kernel(kernelSize, kernelSize, kernelData);
        ConvolveOp convolveOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        
        BufferedImage result = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());
        return convolveOp.filter(src, result);
    }
    
    // Gaussian blur
    public static BufferedImage applyGaussianBlur(BufferedImage src, int kernelSize) {
        float sigma = kernelSize / 3.0f;
        float[] kernelData = createGaussianKernel(kernelSize, sigma);
        
        Kernel kernel = new Kernel(kernelSize, kernelSize, kernelData);
        ConvolveOp convolveOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        
        BufferedImage result = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());
        return convolveOp.filter(src, result);
    }
    
    // Create Gaussian kernel
    private static float[] createGaussianKernel(int size, float sigma) {
        float[] kernel = new float[size * size];
        float sum = 0.0f;
        int center = size / 2;
        
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int x = i - center;
                int y = j - center;
                float value = (float) Math.exp(-(x * x + y * y) / (2 * sigma * sigma));
                kernel[i * size + j] = value;
                sum += value;
            }
        }
        
        // Normalize
        for (int i = 0; i < kernel.length; i++) {
            kernel[i] /= sum;
        }
        
        return kernel;
    }
    
    // Median blur
    public static BufferedImage applyMedianBlur(BufferedImage src, int kernelSize) {
        int width = src.getWidth();
        int height = src.getHeight();
        BufferedImage result = new BufferedImage(width, height, src.getType());
        
        int radius = kernelSize / 2;
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int[] rValues = new int[kernelSize * kernelSize];
                int[] gValues = new int[kernelSize * kernelSize];
                int[] bValues = new int[kernelSize * kernelSize];
                int count = 0;
                
                for (int ky = -radius; ky <= radius; ky++) {
                    for (int kx = -radius; kx <= radius; kx++) {
                        int nx = Math.max(0, Math.min(width - 1, x + kx));
                        int ny = Math.max(0, Math.min(height - 1, y + ky));
                        
                        int rgb = src.getRGB(nx, ny);
                        rValues[count] = (rgb >> 16) & 0xFF;
                        gValues[count] = (rgb >> 8) & 0xFF;
                        bValues[count] = rgb & 0xFF;
                        count++;
                    }
                }
                
                Arrays.sort(rValues, 0, count);
                Arrays.sort(gValues, 0, count);
                Arrays.sort(bValues, 0, count);
                
                int medianIndex = count / 2;
                int r = rValues[medianIndex];
                int g = gValues[medianIndex];
                int b = bValues[medianIndex];
                
                result.setRGB(x, y, (r << 16) | (g << 8) | b);
            }
        }
        
        return result;
    }
    
    // Simplified bilateral filter
    public static BufferedImage applyBilateralFilter(BufferedImage src, int kernelSize) {
        int width = src.getWidth();
        int height = src.getHeight();
        BufferedImage result = new BufferedImage(width, height, src.getType());
        
        int radius = kernelSize / 2;
        double sigmaSpace = kernelSize / 3.0;
        double sigmaColor = 50.0;
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int centerRGB = src.getRGB(x, y);
                int centerR = (centerRGB >> 16) & 0xFF;
                int centerG = (centerRGB >> 8) & 0xFF;
                int centerB = centerRGB & 0xFF;
                
                double totalWeight = 0.0;
                double totalR = 0.0, totalG = 0.0, totalB = 0.0;
                
                for (int ky = -radius; ky <= radius; ky++) {
                    for (int kx = -radius; kx <= radius; kx++) {
                        int nx = Math.max(0, Math.min(width - 1, x + kx));
                        int ny = Math.max(0, Math.min(height - 1, y + ky));
                        
                        int neighborRGB = src.getRGB(nx, ny);
                        int neighborR = (neighborRGB >> 16) & 0xFF;
                        int neighborG = (neighborRGB >> 8) & 0xFF;
                        int neighborB = neighborRGB & 0xFF;
                        
                        // Spatial weight
                        double spatialDist = Math.sqrt(kx * kx + ky * ky);
                        double spatialWeight = Math.exp(-(spatialDist * spatialDist) / (2 * sigmaSpace * sigmaSpace));
                        
                        // Color weight
                        double colorDist = Math.sqrt(
                            (centerR - neighborR) * (centerR - neighborR) +
                            (centerG - neighborG) * (centerG - neighborG) +
                            (centerB - neighborB) * (centerB - neighborB)
                        );
                        double colorWeight = Math.exp(-(colorDist * colorDist) / (2 * sigmaColor * sigmaColor));
                        
                        double weight = spatialWeight * colorWeight;
                        totalWeight += weight;
                        totalR += neighborR * weight;
                        totalG += neighborG * weight;
                        totalB += neighborB * weight;
                    }
                }
                
                int r = (int) Math.round(totalR / totalWeight);
                int g = (int) Math.round(totalG / totalWeight);
                int b = (int) Math.round(totalB / totalWeight);
                
                r = Math.max(0, Math.min(255, r));
                g = Math.max(0, Math.min(255, g));
                b = Math.max(0, Math.min(255, b));
                
                result.setRGB(x, y, (r << 16) | (g << 8) | b);
            }
        }
        
        return result;
    }
    
    // Resize image preserving aspect ratio
    public static BufferedImage resizePreserveAspect(BufferedImage original, int newWidth) {
        int origWidth = original.getWidth();
        int origHeight = original.getHeight();
        double ratio = (double) newWidth / origWidth;
        int newHeight = (int) (origHeight * ratio);
        
        Image tmp = original.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(newWidth, newHeight, original.getType());
        Graphics2D g2d = resized.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }
    
    // Display all images in one window with labels
    public static void showCombinedImages(BufferedImage[] images, String[] titles) {
        int spacing = 20;
        int labelHeight = 20;
        int totalWidth = 0;
        int maxHeight = 0;
        
        for (BufferedImage img : images) {
            totalWidth += img.getWidth() + spacing;
            maxHeight = Math.max(maxHeight, img.getHeight());
        }
        
        BufferedImage combined = new BufferedImage(totalWidth, maxHeight + labelHeight + 10, 
                BufferedImage.TYPE_3BYTE_BGR);
        Graphics g = combined.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, combined.getWidth(), combined.getHeight());
        
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.setColor(Color.BLACK);
        
        int x = 0;
        for (int i = 0; i < images.length; i++) {
            g.drawImage(images[i], x, labelHeight, null);
            g.drawString(titles[i], x + 10, 15);
            x += images[i].getWidth() + spacing;
        }
        
        JFrame frame = new JFrame("Filtered Outputs (Scaled)");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(combined.getWidth(), combined.getHeight() + 40);
        frame.add(new JLabel(new ImageIcon(combined)));
        frame.setVisible(true);
    }
}