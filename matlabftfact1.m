clc; 
clear all;
close all; 

% to create an image, find and display its Fourier transform & also to find the inverse fourier transform % 

a=zeros(256,256); 
a(110:140,110:140)=1; 

subplot(3,1,1), 
imshow(a),title('Input Image'); a1=log(1+abs(fftshift(fft2(a)))); subplot(3,1,2),imshow(mat2gray(a1)),title('Fourier Transform of the input image'); 

a1=fft2(a); 
b=ifft2(a1); 
subplot(3,1,3), imshow (b), title('Input image obtained by Inverse fourier transform'); 

% To verify the rotation property of the Fourier Transform %

a=zeros(256,256); 
a(110:140,110:140)=1;
figure; 
subplot(2,2,1), imshow(a), title('Input Image'); 
a1=log(1+abs(fftshift(fft2(a)))); 
subplot(2,2,2),imshow(mat2gray(a1)),title('Fourier Transform of the input image'); 

c=imrotate(a,45,'bilinear','crop'); 
subplot(2,2,3),imshow(c), title('Input image rotated by 45 degrees'); 

c1=log(1+abs(fftshift(fft2(c)))); 
subplot(2,2,4),imshow(mat2gray(c1)), title('Fourier transform of the rotated image'); 

% TO VERIFY THE CONVOLUTION PROPERTY %

figure; a=zeros(256,256); 
a(110:140,110:140)=1; 
subplot(2,2,1),imshow(a),title('First input image'); 

b=zeros(256,256); 
b(170:200,170:200)=1; 
subplot(2,2,2), imshow(b), title('Second input image'); 

d=conv2(a,b,'same'); 
subplot(2,2,3), imshow(d),title('Convolution of input images in time domain');


a1=fft2(a); 
b1=fft2(b); 
e=a1.*b1; 
f=fftshift(ifft2(e)); 
subplot(2,2,4), imshow(f), title({'Result of Multiplication of FFTs'; 'of input images and IFFT'}) 
