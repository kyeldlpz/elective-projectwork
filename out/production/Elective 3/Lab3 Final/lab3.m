% Smoothing demo in MATLAB using forlab3.png
close all; clc;

% Load the image
img = imread('forlab3.jpg');

% Convert to double for bilateral filter if needed
imgDouble = im2double(img);

% Display original
subplot(1,5,1);
imshow(img);
title('Original');

% Homogeneous Blur (average filter)
homoBlur = imfilter(img, fspecial('average', [9 9]));
subplot(1,5,2);
imshow(homoBlur);
title('Homogeneous Blur');

% Gaussian Blur
gaussBlur = imgaussfilt(img, 2); % sigma = 2
subplot(1,5,3);
imshow(gaussBlur);
title('Gaussian Blur');

% Median Blur
% Apply median to each channel
medBlur = img;
for c = 1:size(img,3)
    medBlur(:,:,c) = medfilt2(img(:,:,c), [9 9]);
end
subplot(1,5,4);
imshow(medBlur);
title('Median Blur');

% Bilateral Filter
bilateral = imbilatfilt(imgDouble, 0.1, 15);
subplot(1,5,5);
imshow(bilateral);
title('Bilateral Filter');
