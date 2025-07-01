import numpy as np
import matplotlib.pyplot as plt
from scipy.ndimage import rotate
from scipy.signal import convolve2d
from scipy.fft import fft2, ifft2, fftshift

# Clear any existing plots, closing all of the pyplot
plt.close('all')

#Create a simple test image:a white square
a = np.zeros((256, 256))
a[110:141, 110:141] = 1  #Python uses exclusive end indexing

plt.figure(figsize=(12, 15))

# With this code, it display the Original image
plt.subplot(3, 1, 1)
plt.imshow(a, cmap='gray')
plt.title('Input Image')
plt.axis('off')

# Compute and display the Fourier transform
# fft2() computes 2D Fast Fourier Transform
# fftshift() moves the zero-frequency component to the center for better visualization
# log(1 + abs()) is used for display purposes to compress dynamic range
a1 = np.log(1 + np.abs(fftshift(fft2(a))))
plt.subplot(3, 1, 2)

# Normalize to [0,1] range like mat2gray
a1_normalized = (a1 - np.min(a1)) / (np.max(a1) - np.min(a1))
plt.imshow(a1_normalized, cmap='gray')
plt.title('Fourier Transform of the input image')
plt.axis('off')

# Inverse Fourier transform
a1_fft = fft2(a)
b = ifft2(a1_fft)
plt.subplot(3, 1, 3)
plt.imshow(np.real(b), cmap='gray')  # Take real part to handle numerical precision
plt.title('Input image obtained by Inverse fourier transform')
plt.axis('off')

plt.tight_layout()
plt.show()

# Part 2: To verify the rotation property of the Fourier Transform
a = np.zeros((256, 256))
a[110:141, 110:141] = 1

plt.figure(figsize=(12, 10))

# Original image
plt.subplot(2, 2, 1)
plt.imshow(a, cmap='gray')
plt.title('Input Image')
plt.axis('off')

# Fourier transform of original
a1 = np.log(1 + np.abs(fftshift(fft2(a))))
plt.subplot(2, 2, 2)
a1_normalized = (a1 - np.min(a1)) / (np.max(a1) - np.min(a1))
plt.imshow(a1_normalized, cmap='gray')
plt.title('Fourier Transform of the input image')
plt.axis('off')

# Rotated image
c = rotate(a, 45, reshape=False, order=1)  # order=1 for bilinear interpolation
plt.subplot(2, 2, 3)
plt.imshow(c, cmap='gray')
plt.title('Input image rotated by 45 degrees')
plt.axis('off')

# Fourier transform of rotated image
c1 = np.log(1 + np.abs(fftshift(fft2(c))))
plt.subplot(2, 2, 4)
c1_normalized = (c1 - np.min(c1)) / (np.max(c1) - np.min(c1))
plt.imshow(c1_normalized, cmap='gray')
plt.title('Fourier transform of the rotated image')
plt.axis('off')

plt.tight_layout()
plt.show()

#TO VERIFY THE CONVOLUTION PROPERTY
plt.figure(figsize=(12, 10))

a = np.zeros((256, 256))
a[110:141, 110:141] = 1
plt.subplot(2, 2, 1)
plt.imshow(a, cmap='gray')
plt.title('First input image')
plt.axis('off')

b = np.zeros((256, 256))
b[170:201, 170:201] = 1  
plt.subplot(2, 2, 2)
plt.imshow(b, cmap='gray')
plt.title('Second input image')
plt.axis('off')

# Convolution in spatial domain
d = convolve2d(a, b, mode='same')
plt.subplot(2, 2, 3)
plt.imshow(d, cmap='gray')
plt.title('Convolution of input images in time domain')
plt.axis('off')

# Multiplication in frequency domain
a1 = fft2(a)
b1 = fft2(b)
e = a1 * b1
f = fftshift(ifft2(e))
plt.subplot(2, 2, 4)
plt.imshow(np.real(f), cmap='gray')  # Take real part
plt.title('Result of Multiplication of FFTs\nof input images and IFFT') #inverse fast fourier transform
plt.axis('off')

plt.tight_layout()
plt.show()
