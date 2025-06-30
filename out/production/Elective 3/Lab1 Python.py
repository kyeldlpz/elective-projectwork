from PIL import Image # type: ignore
import numpy as np # type: ignore
import matplotlib.pyplot as plt # type: ignore
import os

def process_image():
    try:
        # Check if image file exists
        image_path = 'picforlab1.jpg'
        if not os.path.exists(image_path):
            print(f"Error: {image_path} not found in current directory")
            print(f"Current directory: {os.getcwd()}")
            print("Available files:", os.listdir('.'))
            return
        
        # Red Blue and Green and Gray Components
        i = np.array(Image.open(image_path))
        print(f"Image loaded successfully! Shape: {i.shape}")
        
        # Create figure with subplots
        plt.figure(figsize=(15, 10))
        
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
        
        # Adjust layout and show
        plt.tight_layout()
        plt.show()
        
        print("Image processing completed successfully!")
        
    except FileNotFoundError:
        print("Error: Image file 'picforlab1.jpg' not found!")
        print("Make sure the image is in the same folder as this Python file.")
    except Exception as e:
        print(f"An error occurred: {e}")

# Run the function
if __name__ == "__main__":
    process_image()