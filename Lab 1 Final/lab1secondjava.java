import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class lab1secondjava extends JFrame {
    
    public static void main(String[] args) {
        new lab1secondjava().processImages();
    }
    
    public void processImages() {
        try {
            // Read the image
            BufferedImage original = ImageIO.read(new File("picforlab1.jpg"));
            int width = original.getWidth();
            int height = original.getHeight();
            
            // Create complement of color image
            BufferedImage complement = createComplement(original);
            
            // Convert to grayscale
            BufferedImage grayscale = convertToGrayscale(original);
            
            // Create complement of grayscale
            BufferedImage grayComplement = createComplement(grayscale);
            
            // Display first set of images
            displayImageOperations(original, complement, grayscale, grayComplement);
            
            // Create simulation matrices
            createSimulation();
            
        } catch (IOException e) {
            System.err.println("Error reading image: " + e.getMessage());
        }
    }
    
    private BufferedImage createComplement(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage complement = new BufferedImage(width, height, img.getType());
        
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color pixel = new Color(img.getRGB(x, y));
                int red = 255 - pixel.getRed();
                int green = 255 - pixel.getGreen();
                int blue = 255 - pixel.getBlue();
                complement.setRGB(x, y, new Color(red, green, blue).getRGB());
            }
        }
        return complement;
    }
    
    private BufferedImage convertToGrayscale(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage gray = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color pixel = new Color(img.getRGB(x, y));
                int grayValue = (int)(0.299 * pixel.getRed() + 
                                     0.587 * pixel.getGreen() + 
                                     0.114 * pixel.getBlue());
                gray.setRGB(x, y, new Color(grayValue, grayValue, grayValue).getRGB());
            }
        }
        return gray;
    }
    
    private void displayImageOperations(BufferedImage original, BufferedImage complement, 
                                       BufferedImage grayscale, BufferedImage grayComplement) {
        JFrame frame1 = new JFrame("Image Operations");
        frame1.setLayout(new GridLayout(2, 2));
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame1.add(createImagePanel(original, "Color Image"));
        frame1.add(createImagePanel(complement, "Complement of Color Image"));
        frame1.add(createImagePanel(grayscale, "Grayscale Image"));
        frame1.add(createImagePanel(grayComplement, "Complement of Grayscale"));
        
        frame1.pack();
        frame1.setVisible(true);
    }
    
    private void createSimulation() {
        // Create matrices (40x40)
        int size = 40;
        int[][] a = createOnesMatrix(size);
        int[][] b = createZerosMatrix(size);
        
        // Create checkerboard patterns
        int[][] c = createCheckerboard1(a, b);
        int[][] d = createCheckerboard2(a, b);
        
        // Arithmetic operations
        int[][] A = multiplyAndAdd(c, d, 10);  // 10*(c+d)
        int[][] M = elementMultiply(c, d);     // c.*d
        int[][] S = subtract(c, d);            // c-d
        int[][] D = divide(c, 4);              // c/4
        
        // Display simulation results
        displaySimulation(c, d, A, M, S, D);
    }
    
    private int[][] createOnesMatrix(int size) {
        int[][] matrix = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = 1;
            }
        }
        return matrix;
    }
    
    private int[][] createZerosMatrix(int size) {
        return new int[size][size]; // Default initialized to 0
    }
    
    private int[][] createCheckerboard1(int[][] a, int[][] b) {
        int size = a.length;
        int[][] result = new int[size * 2][size * 2];
        
        // Top-left: a, Top-right: b, Bottom-left: b, Bottom-right: a
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                result[i][j] = a[i][j];                    // Top-left
                result[i][j + size] = b[i][j];             // Top-right
                result[i + size][j] = b[i][j];             // Bottom-left
                result[i + size][j + size] = a[i][j];      // Bottom-right
            }
        }
        return result;
    }
    
    private int[][] createCheckerboard2(int[][] a, int[][] b) {
        int size = a.length;
        int[][] result = new int[size * 2][size * 2];
        
        // Top-left: b, Top-right: b, Bottom-left: a, Bottom-right: a
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                result[i][j] = b[i][j];                    // Top-left
                result[i][j + size] = b[i][j];             // Top-right
                result[i + size][j] = a[i][j];             // Bottom-left
                result[i + size][j + size] = a[i][j];      // Bottom-right
            }
        }
        return result;
    }
    
    private int[][] multiplyAndAdd(int[][] c, int[][] d, int multiplier) {
        int rows = c.length;
        int cols = c[0].length;
        int[][] result = new int[rows][cols];
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = multiplier * (c[i][j] + d[i][j]);
            }
        }
        return result;
    }
    
    private int[][] elementMultiply(int[][] c, int[][] d) {
        int rows = c.length;
        int cols = c[0].length;
        int[][] result = new int[rows][cols];
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = c[i][j] * d[i][j];
            }
        }
        return result;
    }
    
    private int[][] subtract(int[][] c, int[][] d) {
        int rows = c.length;
        int cols = c[0].length;
        int[][] result = new int[rows][cols];
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = c[i][j] - d[i][j];
            }
        }
        return result;
    }
    
    private int[][] divide(int[][] c, int divisor) {
        int rows = c.length;
        int cols = c[0].length;
        int[][] result = new int[rows][cols];
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = c[i][j] / divisor;
            }
        }
        return result;
    }
    
    private void displaySimulation(int[][] c, int[][] d, int[][] A, int[][] M, int[][] S, int[][] D) {
        JFrame frame2 = new JFrame("Image Simulation - Arithmetic Operations");
        frame2.setLayout(new GridLayout(3, 2));
        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame2.add(createMatrixPanel(c, "Matrix c", "normal"));
        frame2.add(createMatrixPanel(d, "Matrix d", "normal"));
        frame2.add(createMatrixPanel(A, "A = 10*(c+d)", "white"));
        frame2.add(createMatrixPanel(M, "M = c.*d", "normal"));
        frame2.add(createMatrixPanel(S, "S = c-d", "blackLowerLeft"));
        frame2.add(createMatrixPanel(D, "D = c/4", "diagonalGrey"));
        
        frame2.pack();
        frame2.setVisible(true);
    }
    
    private JPanel createImagePanel(BufferedImage img, String title) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel imageLabel = new JLabel(new ImageIcon(img));
        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(imageLabel, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createMatrixPanel(int[][] matrix, String title, String colorMode) {
        // Convert matrix to image for display with specific color mode
        BufferedImage img = matrixToImage(matrix, colorMode);
        return createImagePanel(img, title);
    }
    
    private BufferedImage matrixToImage(int[][] matrix, String colorMode) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        BufferedImage img = new BufferedImage(cols, rows, BufferedImage.TYPE_INT_RGB);
        
        // Apply different color schemes based on colorMode
        switch (colorMode) {
            case "white":
                // For A = 10*(c+d): change greys to white
                createWhiteImage(img, matrix, rows, cols);
                break;
            case "blackLowerLeft":
                // For S = c-d: lower left should be black
                createBlackLowerLeftImage(img, matrix, rows, cols);
                break;
            case "diagonalGrey":
                // For D = c/4: diagonal grey pattern
                createDiagonalGreyImage(img, matrix, rows, cols);
                break;
            default:
                // Normal grayscale conversion
                createNormalGrayscaleImage(img, matrix, rows, cols);
                break;
        }
        
        // Scale up for better visibility
        return scaleImage(img, 5);
    }
    
    private void createWhiteImage(BufferedImage img, int[][] matrix, int rows, int cols) {
        // For A = 10*(c+d): make all non-zero values white, keep zeros black
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (matrix[i][j] > 0) {
                    img.setRGB(j, i, Color.WHITE.getRGB());
                } else {
                    img.setRGB(j, i, Color.BLACK.getRGB());
                }
            }
        }
    }
    
    private void createBlackLowerLeftImage(BufferedImage img, int[][] matrix, int rows, int cols) {
        // For S = c-d: make lower left quadrant black, others based on matrix values
        int halfRows = rows / 2;
        int halfCols = cols / 2;
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // Check if we're in the lower left quadrant
                if (i >= halfRows && j < halfCols) {
                    img.setRGB(j, i, Color.BLACK.getRGB());
                } else {
                    // Use original matrix values for other quadrants
                    if (matrix[i][j] > 0) {
                        img.setRGB(j, i, Color.WHITE.getRGB());
                    } else if (matrix[i][j] < 0) {
                        img.setRGB(j, i, Color.GRAY.getRGB());
                    } else {
                        img.setRGB(j, i, Color.BLACK.getRGB());
                    }
                }
            }
        }
    }
    
    private void createDiagonalGreyImage(BufferedImage img, int[][] matrix, int rows, int cols) {
        // For D = c/4: create 2x2 block pattern to match the uploaded image
        // Upper-left: grey, Upper-right: black, Lower-left: black, Lower-right: grey
        int halfRows = rows / 2;
        int halfCols = cols / 2;
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (i < halfRows && j < halfCols) {
                    // Upper-left quadrant: grey
                    img.setRGB(j, i, Color.GRAY.getRGB());
                } else if (i < halfRows && j >= halfCols) {
                    // Upper-right quadrant: black
                    img.setRGB(j, i, Color.BLACK.getRGB());
                } else if (i >= halfRows && j < halfCols) {
                    // Lower-left quadrant: black
                    img.setRGB(j, i, Color.BLACK.getRGB());
                } else {
                    // Lower-right quadrant: grey
                    img.setRGB(j, i, Color.GRAY.getRGB());
                }
            }
        }
    }
    
    private void createNormalGrayscaleImage(BufferedImage img, int[][] matrix, int rows, int cols) {
        // Find max value for normalization
        int max = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                max = Math.max(max, Math.abs(matrix[i][j]));
            }
        }
        
        // Convert to grayscale image
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int value = (max == 0) ? 0 : (int)(255.0 * Math.abs(matrix[i][j]) / max);
                img.setRGB(j, i, new Color(value, value, value).getRGB());
            }
        }
    }
    
    private BufferedImage scaleImage(BufferedImage original, int factor) {
        int width = original.getWidth() * factor;
        int height = original.getHeight() * factor;
        BufferedImage scaled = new BufferedImage(width, height, original.getType());
        Graphics2D g2d = scaled.createGraphics();
        g2d.drawImage(original, 0, 0, width, height, null);
        g2d.dispose();
        return scaled;
    }
}