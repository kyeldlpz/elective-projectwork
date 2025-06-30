% To find Neighbour of a given Pixel
a=magic(5);
disp('a='); disp(a);
b=input('Enter the row < size of the Matrix');
c=input(' Enter the Column < size of matrix');
disp('Element'); disp(a(b,c));
% 4 Point Neighbour
N4=[a(b+1,c), a(b-1,c), a(b,c+1), a(b,c-1)];
disp('N4='); disp(N4);
%8 Point Neighbour
N8=[a(b+1,c), a(b-1,c), a(b,c+1), a(b,c-1), a(b+1,c+1), a(b+1,c-1), a(b-1,c-1), a(b-1,c+1)]; disp('N8=');
disp(N8);
%Diagonal Neighbour
ND=[ a(b+1,c+1), a(b+1,c-1), a(b-1,c-1), a(b-1,c+1)];
disp('ND='); disp(ND);