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
fig, axes = plt.subplots(2, 2, figsize=(12, 10))
axes = axes.flatten()

# Display images with proper axis formatting
for i, (title, image) in enumerate(resized_images.items()):
    axes[i].imshow(np.array(image), extent=[0, image.width, image.height, 0])
    axes[i].set_title(title)
    
    if title == 'Original Image':
        # Original image - ticks every 100 pixels
        axes[i].set_xticks(np.arange(100, image.width + 100, 100))
        axes[i].set_yticks(np.arange(100, image.height + 100, 100))
    else:
        # Resized images - ticks every 500 pixels
        max_x = max(2500, image.width * 2)  # Ensure we show up to 2500
        max_y = max(2000, image.height * 2)  # Ensure we show up to 2000
        axes[i].set_xticks(np.arange(500, max_x + 500, 500))
        axes[i].set_yticks(np.arange(500, max_y + 500, 500))
    
    # Set axis limits to match image bounds
    axes[i].set_xlim(0, image.width)
    axes[i].set_ylim(image.height, 0)  # Invert Y-axis to match image coordinates
    
    # Style the axes
    axes[i].grid(False)
    axes[i].tick_params(labelsize=8)

plt.tight_layout()
plt.show()