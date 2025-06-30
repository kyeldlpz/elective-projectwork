import numpy as np

def magic_square(n):
    """Create a magic square like MATLAB's magic() function"""
    return np.array([[17, 24, 1, 8, 15],
                     [23, 5, 7, 14, 16],
                     [4, 6, 13, 20, 22],
                     [10, 12, 19, 21, 3],
                     [11, 18, 25, 2, 9]])

def print_array(arr):
    """Print array without brackets"""
    print(' '.join(map(str, arr)))

# To find Neighbour of a given Pixel
a = magic_square(5)
print('a=')
for row in a:
    print(' '.join(map(str, row)))

b = int(input('Enter the row < size of the Matrix: '))
c = int(input('Enter the Column < size of matrix: '))
print('Element'); print(a[b-1, c-1])  # Convert to 0-based indexing

# Adjust indices for 0-based indexing
b, c = b-1, c-1

# 4 Point Neighbour
N4 = [int(a[b+1,c]), int(a[b-1,c]), int(a[b,c+1]), int(a[b,c-1])]
print('N4='); print_array(N4)

# 8 Point Neighbour  
N8 = [int(a[b+1,c]), int(a[b-1,c]), int(a[b,c+1]), int(a[b,c-1]), int(a[b+1,c+1]), int(a[b+1,c-1]), int(a[b-1,c-1]), int(a[b-1,c+1])]
print('N8='); print_array(N8)

# Diagonal Neighbour
ND = [int(a[b+1,c+1]), int(a[b+1,c-1]), int(a[b-1,c-1]), int(a[b-1,c+1])]
print('ND='); print_array(ND)