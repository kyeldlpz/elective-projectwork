% Complement, Converting and Simulation of an Image (No Toolbox)
I=imread('picforlab1.jpg');

% Replace imcomplement with manual complement
subplot(2,2,1); image(I); axis off; title('Color Image');
c = 255 - I;
subplot(2,2,2); image(c); axis off; title('Complement of color Image');

% Replace rgb2gray with manual conversion
r = uint8(0.299*double(I(:,:,1)) + 0.587*double(I(:,:,2)) + 0.114*double(I(:,:,3)));
subplot(2,2,3); imshow(r); title('Gray scale of color Image');

% Complement of Gray Image
b = 255 - r;
subplot(2,2,4); imshow(b); title('Complement of Gray Image');

% Simulation of an Image (same as original)
a=ones(40);
b=zeros(40);
c=[a b;b a];
d=[b b;a a];
A=10*(c+d);
M=c.*d;
S=c-d;
D=c/4;
figure;
subplot(3,2,1); imshow(c);
subplot(3,2,2); imshow(d);
subplot(3,2,3); imshow(A);
subplot(3,2,4); imshow(M);
subplot(3,2,5); imshow(S);
subplot(3,2,6); imshow(D);