import cv2
import matplotlib.pyplot as plt

# Load and convert image
img = cv2.imread('forlab3.jpg')
img_rgb = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)

# Set kernel size
k = 9

# Applying the filters in the pictures
homogeneous = cv2.blur(img_rgb, (k, k))
gaussian = cv2.GaussianBlur(img_rgb, (k, k), 0)
median = cv2.medianBlur(img_rgb, k)
bilateral = cv2.bilateralFilter(img_rgb, k, k * 2, k // 2)

# List of images and titles
images = [img_rgb, homogeneous, gaussian, median, bilateral]
titles = ['Original', 'Homogeneous Blur', 'Gaussian Blur', 'Median Blur', 'Bilateral Filter']

# Plot all in a grid with titles
plt.figure(figsize=(15, 5))
for i in range(5):
    plt.subplot(1, 5, i + 1)
    plt.imshow(images[i])
    plt.title(titles[i])
    plt.axis('off')

plt.tight_layout()
plt.show()