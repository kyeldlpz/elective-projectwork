import numpy as np
import matplotlib.pyplot as plt
from PIL import Image
import math

def manual_resize(img_array, scale_factor):
    """
    Manually resize image using bilinear interpolation
    """
    if len(img_array.shape) == 3:
        rows, cols, channels = img_array.shape
    else:
        rows, cols = img_array.shape
        channels = 1
        img_array = img_array[:, :, np.newaxis]
    
    # Calculate new dimensions
    new_rows = int(round(rows * scale_factor))
    new_cols = int(round(cols * scale_factor))
    
    # Create coordinate grids for new image
    x_new = np.arange(new_cols)
    y_new = np.arange(new_rows)
    X_new, Y_new = np.meshgrid(x_new, y_new)
    
    # Map new coordinates back to original image coordinates
    X_orig = (X_new) / scale_factor
    Y_orig = (Y_new) / scale_factor
    
    # Initialize output image
    resized_img = np.zeros((new_rows, new_cols, channels), dtype=img_array.dtype)
    
    # Perform bilinear interpolation for each channel
    for c in range(channels):
        resized_img[:, :, c] = bilinear_interpolation(img_array[:, :, c], X_orig, Y_orig)
    
    # Remove single channel dimension if original was grayscale
    if channels == 1 and len(img_array.shape) == 2:
        resized_img = resized_img[:, :, 0]
    
    return resized_img

def manual_rotate(img_array, angle_deg):
    """
    Manually rotate image
    """
    if len(img_array.shape) == 3:
        rows, cols, channels = img_array.shape
    else:
        rows, cols = img_array.shape
        channels = 1
        img_array = img_array[:, :, np.newaxis]
    
    # Convert angle to radians
    theta = math.radians(angle_deg)
    cos_theta = math.cos(theta)
    sin_theta = math.sin(theta)
    
    # Find corners of original image
    corners = np.array([[0, 0], [cols-1, 0], [cols-1, rows-1], [0, rows-1]]).T
    center_orig = np.array([cols/2, rows/2])
    
    # Translate corners to origin, rotate, then find bounding box
    corners_centered = corners - center_orig.reshape(-1, 1)
    rotation_matrix = np.array([[cos_theta, -sin_theta], 
                               [sin_theta, cos_theta]])
    rotated_corners = rotation_matrix @ corners_centered
    
    # Find bounding box of rotated image
    min_x = np.min(rotated_corners[0, :])
    max_x = np.max(rotated_corners[0, :])
    min_y = np.min(rotated_corners[1, :])
    max_y = np.max(rotated_corners[1, :])
    
    # Calculate new image dimensions
    new_cols = int(math.ceil(max_x - min_x))
    new_rows = int(math.ceil(max_y - min_y))
    
    # Create coordinate grids for new image
    x_new = np.arange(new_cols)
    y_new = np.arange(new_rows)
    X_new, Y_new = np.meshgrid(x_new, y_new)
    
    # Center of new image
    center_new = np.array([new_cols/2, new_rows/2])
    
    # Convert new image coordinates to original image coordinates
    X_centered = X_new - center_new[0]
    Y_centered = Y_new - center_new[1]
    
    # Apply inverse rotation
    X_orig = cos_theta * X_centered + sin_theta * Y_centered + center_orig[0]
    Y_orig = -sin_theta * X_centered + cos_theta * Y_centered + center_orig[1]
    
    # Initialize output image
    rotated_img = np.zeros((new_rows, new_cols, channels), dtype=img_array.dtype)
    
    # Perform interpolation for each channel
    for c in range(channels):
        rotated_img[:, :, c] = bilinear_interpolation(img_array[:, :, c], X_orig, Y_orig)
    
    # Remove single channel dimension if original was grayscale
    if channels == 1 and len(img_array.shape) == 2:
        rotated_img = rotated_img[:, :, 0]
    
    return rotated_img

def bilinear_interpolation(img_channel, X_coords, Y_coords):
    """
    Perform bilinear interpolation on a single channel
    """
    rows, cols = img_channel.shape
    output_shape = X_coords.shape
    result = np.zeros(output_shape, dtype=img_channel.dtype)
    
    # Flatten coordinates for easier processing
    X_flat = X_coords.flatten()
    Y_flat = Y_coords.flatten()
    
    for i, (x, y) in enumerate(zip(X_flat, Y_flat)):
        # Check bounds
        if x < 0 or x >= cols-1 or y < 0 or y >= rows-1:
            continue
        
        # Get integer coordinates
        x1 = int(np.floor(x))
        y1 = int(np.floor(y))
        x2 = min(x1 + 1, cols - 1)
        y2 = min(y1 + 1, rows - 1)
        
        # Calculate weights
        dx = x - x1
        dy = y - y1
        
        # Get four surrounding pixels
        c11 = img_channel[y1, x1]
        c21 = img_channel[y1, x2]
        c12 = img_channel[y2, x1]
        c22 = img_channel[y2, x2]
        
        # Bilinear interpolation
        top = c11 * (1 - dx) + c21 * dx
        bottom = c12 * (1 - dx) + c22 * dx
        interpolated_value = top * (1 - dy) + bottom * dy
        
        # Convert back to 2D index
        row_idx = i // output_shape[1]
        col_idx = i % output_shape[1]
        result[row_idx, col_idx] = interpolated_value
    
    return result

def main():
    """
    Main function to demonstrate image processing
    """
    try:
        # Load image
        img = Image.open('picforlab1.jpg')
        img_array = np.array(img)
        
        # Get scaling factor from user
        scale_factor = float(input("Enter Scaling Factor: "))
        
        # Manual scaling
        img_scaled = manual_resize(img_array, scale_factor)
        
        # Manual rotation by 60 degrees
        img_rot60 = manual_rotate(img_scaled, 60)
        
        # Manual rotation by 45 degrees
        img_rot45 = manual_rotate(img_scaled, 45)
        
        # Display images
        plt.figure(figsize=(12, 8))
        
        plt.subplot(2, 2, 1)
        plt.imshow(img_array, cmap='gray' if len(img_array.shape) == 2 else None)
        plt.title('Original Image')
        plt.axis('on')
        
        plt.subplot(2, 2, 2)
        plt.imshow(img_scaled, cmap='gray' if len(img_scaled.shape) == 2 else None)
        plt.title('Scaled Image')
        plt.axis('on')
        
        plt.subplot(2, 2, 3)
        plt.imshow(img_rot60, cmap='gray' if len(img_rot60.shape) == 2 else None)
        plt.title('Rotated Image 60deg')
        plt.axis('on')
        
        plt.subplot(2, 2, 4)
        plt.imshow(img_rot45, cmap='gray' if len(img_rot45.shape) == 2 else None)
        plt.title('Rotated Image 45deg')
        plt.axis('on')
        
        plt.tight_layout()
        plt.show()
        
        # Save processed images
        Image.fromarray(img_scaled.astype(np.uint8)).save('scaled_image.jpg')
        Image.fromarray(img_rot60.astype(np.uint8)).save('rotated_60deg.jpg')
        Image.fromarray(img_rot45.astype(np.uint8)).save('rotated_45deg.jpg')
        
        print("Processed images saved!")
        
    except FileNotFoundError:
        print("Error: Could not find 'circuit.jpg'. Please make sure the image file exists.")
    except Exception as e:
        print(f"Error: {e}")

# Alternative using OpenCV (if available)
def opencv_version():
    """
    Alternative implementation using OpenCV for comparison
    """
    try:
        import cv2
        
        # Load image
        img = cv2.imread('circuit.jpg')
        if img is None:
            print("Error: Could not load image")
            return
            
        # Get scaling factor
        scale_factor = float(input("Enter Scaling Factor: "))
        
        # Resize using OpenCV
        height, width = img.shape[:2]
        new_height = int(height * scale_factor)
        new_width = int(width * scale_factor)
        img_scaled = cv2.resize(img, (new_width, new_height), interpolation=cv2.INTER_LINEAR)
        
        # Rotate using OpenCV
        center = (new_width // 2, new_height // 2)
        rotation_matrix_60 = cv2.getRotationMatrix2D(center, 60, 1.0)
        rotation_matrix_45 = cv2.getRotationMatrix2D(center, 45, 1.0)
        
        img_rot60 = cv2.warpAffine(img_scaled, rotation_matrix_60, (new_width, new_height))
        img_rot45 = cv2.warpAffine(img_scaled, rotation_matrix_45, (new_width, new_height))
        
        # Display using matplotlib (convert BGR to RGB)
        plt.figure(figsize=(12, 8))
        
        plt.subplot(2, 2, 1)
        plt.imshow(cv2.cvtColor(img, cv2.COLOR_BGR2RGB))
        plt.title('Original Image')
        plt.axis('on')
        
        plt.subplot(2, 2, 2)
        plt.imshow(cv2.cvtColor(img_scaled, cv2.COLOR_BGR2RGB))
        plt.title('Scaled Image')
        plt.axis('on')
        
        plt.subplot(2, 2, 3)
        plt.imshow(cv2.cvtColor(img_rot60, cv2.COLOR_BGR2RGB))
        plt.title('Rotated Image 60deg')
        plt.axis('off')
        
        plt.subplot(2, 2, 4)
        plt.imshow(cv2.cvtColor(img_rot45, cv2.COLOR_BGR2RGB))
        plt.title('Rotated Image 45deg')
        plt.axis('off')
        
        plt.tight_layout()
        plt.show()
        
    except ImportError:
        print("OpenCV not installed. Use the manual implementation instead.")

if __name__ == "__main__":
    main()
    
   