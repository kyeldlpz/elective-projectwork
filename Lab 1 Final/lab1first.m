% Red Blue and Green and Gray Components
i=imread('picforlab1.jpg');
subplot(3,2,1); imshow(i); title('Original Image');

%Red Component
r=i(:,:,1);
subplot(3,2,2); imshow(r);title('Red Component');

%Green Component
g=i(:,:,2);
subplot(3,2,3); imshow(g); title('Green Component');

%Blue Component
b=i(:,:,3); subplot(3,2,4); imshow(b); title('Blue Component');

%Color to Gray note that 0.2989 for red, 0.5870 for green, 0.1140 for blue
rg=0.2989*double(i(:,:,1))+0.5870*double(i(:,:,2))+0.1140*double(i(:,:,3));
rg=uint8(rg); subplot(3,2,5); imshow(rg); title('Gray Image');