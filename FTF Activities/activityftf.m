clc;
clear all;
close all;

% to create an image, find and display its Fourier transform & also to find the inverse fourier transform %
img = imread('picforlab1.jpg'); % Put your image filename here
if size(img, 3) == 3
a = im2double(img); % Convert to double precision
a = imresize(a, [256, 256]); % Resize to 256x256
end
subplot(3,1,1),
imshow(a),title('Input Image'); 
a1=log(1+abs(fftshift(fft2(a)))); 
subplot(3,1,2),imshow(mat2gray(a1)),title('Fourier Transform of the input image');
a1=fft2(a);
b=ifft2(a1);
subplot(3,1,3), imshow(real(b)), title('Input image obtained by Inverse fourier transform');

% To verify the rotation property of the Fourier Transform %

img = imread('picforlab1.jpg'); % Same image or different one
if size(img, 3) == 3
a = im2double(img);
a = imresize(a, [256, 256]);
end

figure;
subplot(2,2,1), imshow(a), title('Input Image');
a1=log(1+abs(fftshift(fft2(a))));
subplot(2,2,2),imshow(mat2gray(a1)),title('Fourier Transform of the input image');
c=imrotate(a,45,'bilinear','crop');
subplot(2,2,3),imshow(c), title('Input image rotated by 45 degrees');
c1=log(1+abs(fftshift(fft2(c))));
subplot(2,2,4),imshow(mat2gray(c1)), title('Fourier transform of the rotated image');

% TO VERIFY THE CONVOLUTION PROPERTY %
figure; 

img1 = imread('picforlab1.jpg'); % First image
if size(img1, 3) == 3
    img1 = rgb2gray(img1);
a = im2double(img1);
a = imresize(a, [256, 256]);
end

subplot(2,2,1),imshow(a),title('First input image');

% REPLACE THESE LINES:
img2 = imread('picforlab1.jpg'); % Second image
if size(img2, 3) == 3
b = im2double(img2);
b = imresize(b, [256, 256]);
end

subplot(2,2,2), imshow(b), title('Second input image');
d=conv2(a,b,'same');
subplot(2,2,3), imshow(mat2gray(d)),title('Convolution of input images in time domain');
a1=fft2(a);
b1=fft2(b);
e=a1.*b1;
f=fftshift(ifft2(e));
subplot(2,2,4), imshow(mat2gray(abs(f))), title({'Result of Multiplication of FFTs'; 'of input images and IFFT'})