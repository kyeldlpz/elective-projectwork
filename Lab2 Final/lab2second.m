
% Load image
I = imread('picforlab1.jpg');
subplot(2,2,1); 
imshow(I); 
title('Original Image');

% Get scaling factor from user
s = input('Enter Scaling Factor: ');

% Manual scaling using imresize alternative
I_scaled = manualResize(I, s);
subplot(2,2,2); 
imshow(I_scaled); 
title('Scaled Image');

% Manual rotation by 60 degrees
I_rot60 = manualRotate(I_scaled, 60);
subplot(2,2,3); 
imshow(I_rot60); 
title('Rotated Image 60deg');

% Manual rotation by 45 degrees
I_rot45 = manualRotate(I_scaled, 45);
subplot(2,2,4); 
imshow(I_rot45); 
title('Rotated Image 45deg');

%% Manual Resize Function
function resized_img = manualResize(img, scale_factor)
    [rows, cols, channels] = size(img);
    
    % Calculate new dimensions
    new_rows = round(rows * scale_factor);
    new_cols = round(cols * scale_factor);
    
    % Create coordinate grids for original and new images
    [X_new, Y_new] = meshgrid(1:new_cols, 1:new_rows);
    
    % Map new coordinates back to original image coordinates
    X_orig = (X_new - 1) / scale_factor + 1;
    Y_orig = (Y_new - 1) / scale_factor + 1;
    
    % Initialize output image
    resized_img = zeros(new_rows, new_cols, channels, class(img));
    
    % Perform interpolation for each channel
    for c = 1:channels
        resized_img(:,:,c) = interp2(double(img(:,:,c)), X_orig, Y_orig, 'linear', 0);
    end
    
    % Convert back to original data type
    resized_img = cast(resized_img, class(img));
end

%% Manual Rotate Function
function rotated_img = manualRotate(img, angle_deg)
    [rows, cols, channels] = size(img);
    
    % Convert angle to radians
    theta = deg2rad(angle_deg);
    
    % Calculate rotation matrix
    cos_theta = cos(theta);
    sin_theta = sin(theta);
    
    % Find corners of original image
    corners = [1, 1; cols, 1; cols, rows; 1, rows]';
    center_orig = [cols/2; rows/2];
    
    % Translate corners to origin, rotate, then translate back
    corners_centered = corners - center_orig;
    rotation_matrix = [cos_theta, -sin_theta; sin_theta, cos_theta];
    rotated_corners = rotation_matrix * corners_centered;
    
    % Find bounding box of rotated image
    min_x = min(rotated_corners(1,:));
    max_x = max(rotated_corners(1,:));
    min_y = min(rotated_corners(2,:));
    max_y = max(rotated_corners(2,:));
    
    % Calculate new image dimensions
    new_cols = ceil(max_x - min_x);
    new_rows = ceil(max_y - min_y);
    
    % Create coordinate grids for new image
    [X_new, Y_new] = meshgrid(1:new_cols, 1:new_rows);
    
    % Center of new image
    center_new = [new_cols/2; new_rows/2];
    
    % Convert new image coordinates to original image coordinates
    X_centered = X_new - center_new(1);
    Y_centered = Y_new - center_new(2);
    
    % Apply inverse rotation
    X_orig = cos_theta * X_centered + sin_theta * Y_centered + center_orig(1);
    Y_orig = -sin_theta * X_centered + cos_theta * Y_centered + center_orig(2);
    
    % Initialize output image
    rotated_img = zeros(new_rows, new_cols, channels, class(img));
    
    % Perform interpolation for each channel
    for c = 1:channels
        rotated_img(:,:,c) = interp2(double(img(:,:,c)), X_orig, Y_orig, 'linear', 0);
    end
    
    % Convert back to original data type
    rotated_img = cast(rotated_img, class(img));
end