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

plt.tight_layout()
plt.show()

