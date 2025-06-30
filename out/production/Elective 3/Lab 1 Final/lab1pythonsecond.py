import cv2
import numpy as np
import matplotlib.pyplot as plt

def image_operations():
    # Read the image
    I = cv2.imread('picforlab1.jpg')
    if I is None:
        print("Error: Could not read 'picforlab1.jpg'")
        return
    
    # Convert BGR to RGB for display
    I = cv2.cvtColor(I, cv2.COLOR_BGR2RGB)
    
    # Create complement of color image
    c = 255 - I
    
    # Convert to grayscale
    r = np.uint8(0.299 * I[:,:,0] + 0.587 * I[:,:,1] + 0.114 * I[:,:,2])
    
    # Complement of grayscale image
    b = 255 - r
    
    # Display first set of images
    plt.figure(figsize=(12, 8))
    
    plt.subplot(2, 2, 1)
    plt.imshow(I)
    plt.title('Color Image')
    plt.axis('off')
    
    plt.subplot(2, 2, 2)
    plt.imshow(c)
    plt.title('Complement of Color Image')
    plt.axis('off')
    
    plt.subplot(2, 2, 3)
    plt.imshow(r, cmap='gray')
    plt.title('Grayscale of Color Image')
    plt.axis('off')
    
    plt.subplot(2, 2, 4)
    plt.imshow(b, cmap='gray')
    plt.title('Complement of Grayscale Image')
    plt.axis('off')
    
    plt.tight_layout()
    plt.show()

def create_custom_colored_matrix(matrix, color_mode):
    """Create custom colored version of matrix based on color_mode"""
    rows, cols = matrix.shape
    
    if color_mode == "white":
        # For A = 10*(c+d): change greys to white, keep zeros black
        colored_matrix = np.where(matrix > 0, 1.0, 0.0)
        return colored_matrix
    
    elif color_mode == "blackLowerLeft":
        # For S = c-d: lower left should be black
        colored_matrix = matrix.copy()
        half_rows = rows // 2
        half_cols = cols // 2
        
        # Make lower left quadrant black (0)
        colored_matrix[half_rows:, :half_cols] = 0
        
        # Normalize other areas for better visibility
        colored_matrix = np.where(colored_matrix > 0, 1.0, 
                                np.where(colored_matrix < 0, 0.5, 0.0))
        return colored_matrix
    
    elif color_mode == "diagonalGrey":
        # For D = c/4: create 2x2 block pattern
        # Upper-left: grey, Upper-right: black, Lower-left: black, Lower-right: grey
        colored_matrix = np.zeros((rows, cols))
        half_rows = rows // 2
        half_cols = cols // 2
        
        # Upper-left quadrant: grey (0.5)
        colored_matrix[:half_rows, :half_cols] = 0.5
        # Upper-right quadrant: black (0.0)
        colored_matrix[:half_rows, half_cols:] = 0.0
        # Lower-left quadrant: black (0.0)
        colored_matrix[half_rows:, :half_cols] = 0.0
        # Lower-right quadrant: grey (0.5)
        colored_matrix[half_rows:, half_cols:] = 0.5
        
        return colored_matrix
    
    else:
        # Normal grayscale - normalize to 0-1 range
        if np.max(np.abs(matrix)) > 0:
            return np.abs(matrix) / np.max(np.abs(matrix))
        else:
            return matrix

def simulation():
    # Create basic matrices
    a = np.ones((40, 40))
    b = np.zeros((40, 40))
    
    # Create checkerboard patterns
    c = np.block([[a, b], [b, a]])
    d = np.block([[b, b], [a, a]])
    
    # Arithmetic operations
    A = 10 * (c + d)
    M = c * d
    S = c - d
    D = c / 4
    
    # Create custom colored versions
    c_display = create_custom_colored_matrix(c, "normal")
    d_display = create_custom_colored_matrix(d, "normal")
    A_display = create_custom_colored_matrix(A, "white")
    M_display = create_custom_colored_matrix(M, "normal")
    S_display = create_custom_colored_matrix(S, "blackLowerLeft")
    D_display = create_custom_colored_matrix(D, "diagonalGrey")
    
    # Display simulation results
    plt.figure(figsize=(12, 8))
    
    plt.subplot(3, 2, 1)
    plt.imshow(c_display, cmap='gray', vmin=0, vmax=1)
    plt.title('Matrix c')
    plt.axis('off')
    
    plt.subplot(3, 2, 2)
    plt.imshow(d_display, cmap='gray', vmin=0, vmax=1)
    plt.title('Matrix d')
    plt.axis('off')
    
    plt.subplot(3, 2, 3)
    plt.imshow(A_display, cmap='gray', vmin=0, vmax=1)
    plt.title('A = 10*(c+d)')
    plt.axis('off')
    
    plt.subplot(3, 2, 4)
    plt.imshow(M_display, cmap='gray', vmin=0, vmax=1)
    plt.title('M = c.*d')
    plt.axis('off')
    
    plt.subplot(3, 2, 5)
    plt.imshow(S_display, cmap='gray', vmin=0, vmax=1)
    plt.title('S = c-d')
    plt.axis('off')
    
    plt.subplot(3, 2, 6)
    plt.imshow(D_display, cmap='gray', vmin=0, vmax=1)
    plt.title('D = c/4')
    plt.axis('off')
    
    plt.tight_layout()
    plt.show()

if __name__ == "__main__":
    image_operations()
    simulation()