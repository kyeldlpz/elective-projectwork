import matplotlib.pyplot as plt
from PIL import Image
import numpy as np

# Load and display original image
img = Image.open('formalpicturekzy.jpg')

# Calculate new size (50% of original)
new_size = (img.width // 2, img.height // 2)

# Resize using different methods
resized_images = {
    'Original Image': img,
    'Bilinear Image': img.resize(new_size, Image.BILINEAR),
    'Nearest Image': img.resize(new_size, Image.NEAREST),
    'Bicubic Image': img.resize(new_size, Image.BICUBIC)
}

# Create subplot display
fig, axes = plt.subplots(2, 2, figsize=(15, 12))
axes = axes.flatten()

# Function to calculate appropriate tick intervals
def get_tick_interval(size):
    """Calculate appropriate tick interval based on image size"""
    if size <= 500:
        return 100
    elif size <= 1000:
        return 200
    elif size <= 2000:
        return 400
    else:
        return 500

# Display images with proper axis formatting
for i, (title, image) in enumerate(resized_images.items()):
    # Convert image to numpy array for display
    img_array = np.array(image)
    
    # Display image with correct extent (left, right, bottom, top)
    axes[i].imshow(img_array, extent=[0, image.width, image.height, 0])
    axes[i].set_title(title, fontsize=12, fontweight='bold')
    
    # Calculate appropriate tick intervals based on image size
    x_interval = get_tick_interval(image.width)
    y_interval = get_tick_interval(image.height)
    
    # Set ticks based on image dimensions
    x_ticks = np.arange(0, image.width + x_interval, x_interval)
    y_ticks = np.arange(0, image.height + y_interval, y_interval)
    
    # Apply ticks and ensure they don't exceed image bounds
    axes[i].set_xticks(x_ticks[x_ticks <= image.width])
    axes[i].set_yticks(y_ticks[y_ticks <= image.height])
    
    # Set axis limits to match image bounds exactly
    axes[i].set_xlim(0, image.width)
    axes[i].set_ylim(image.height, 0)  # Invert Y-axis for image coordinates
    
    # Add grid for better readability
    axes[i].grid(True, alpha=0.3, linestyle='--', linewidth=0.5)
    axes[i].tick_params(labelsize=9)

plt.tight_layout()
plt.show()

# Print image dimensions for reference
print("Image Dimensions:")
print("-" * 40)
for title, image in resized_images.items():
    print(f"{title:<25}: {image.width} × {image.height} pixels")