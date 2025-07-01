% Load and convert image to grayscale double
img = imread('picforlab1.jpg');

% You can replace with your own image
img_gray = im2double(img); 

% Compute 2D FFT
 F = fft2(img_gray);

% Separate amplitude and phase
 amplitude = abs(F);
 phase = angle(F);

% Zero out amplitude (set all to 1 for numerical stability)
 F_phase_only = exp(1i * phase);  

% Complex representation: magnitude 1, original phase

% Inverse FFT to reconstruct
 img_reconstructed = ifft2(F_phase_only);

% Display results
 figure;
 subplot(1,3,1), imshow(img_gray, []), title('Original Image');
 subplot(1,3,2), imshow(log(1 + abs(fftshift(F))), []), title('FFT Spectrum');
 subplot(1,3,3), imshow(abs(img_reconstructed), []), title('Reconstructed (Phase Only)');

% Load and convert image to grayscale double
 img = imread('picforlab1.jpg');
 
% Replace with your own image if desired
 img_gray = im2double(img); % Compute 2D FFT
 F = fft2(img_gray);

% Reconstruct using original amplitude and zero phase
 F_amp_only = amplitude .* exp(1i * zero_phase);

% Inverse FFT
 img_reconstructed = ifft2(F_amp_only);

% Display results
 figure;
 subplot(1,3,1), imshow(img_gray, []), title('Original Image');
 subplot(1,3,2), imshow(log(1 + abs(fftshift(F))), []), title('FFT Spectrum');
 subplot(1,3,3), imshow(abs(img_reconstructed), []), title('Reconstructed (Amplitude Only)');
