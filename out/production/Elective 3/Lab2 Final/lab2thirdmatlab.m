% Display color image and resized versions using different interpolation methods
% Load and display original image
I = imread('formalpicturekzy.jpg');
figure;
subplot(2,2,1);
imshow(I);
title('Original Image');

% Resize using different methods
methods = {'bilinear', 'nearest', 'bicubic'};
titles = {'Bilinear Image', 'Nearest Image', 'Bicubic Image'};

for i = 1:3
    % Resize image to half size
    resized = imresize(I, 0.5, methods{i});
    
    subplot(2,2,i+1);
    imshow(resized);
    title(titles{i});
    
    % Add axis with scale
    axis on;
    set(gca, 'XTick', 0:200:size(resized,2));
    set(gca, 'YTick', 0:200:size(resized,1));
    xlabel('X');
    ylabel('Y');
end

% Add axis to original image too
subplot(2,2,1);
axis on;
set(gca, 'XTick', 0:200:size(I,2));
set(gca, 'YTick', 0:200:size(I,1));
xlabel('X');
ylabel('Y');

% Adjust layout
sgtitle('Image Resizing Comparison');