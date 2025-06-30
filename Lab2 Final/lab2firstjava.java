import java.util.Scanner;

public class lab2firstjava {
    
    public static int[][] magicSquare() {
        // Create the magic square matrix
        return new int[][]{
            {17, 24, 1, 8, 15},
            {23, 5, 7, 14, 16},
            {4, 6, 13, 20, 22},
            {10, 12, 19, 21, 3},
            {11, 18, 25, 2, 9}
        };
    }
    
    public static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int i = 0; i < row.length; i++) {
                System.out.print(row[i]);
                if (i < row.length - 1) System.out.print(" ");
            }
            System.out.println();
        }
    }
    
    public static void printArray(int[] array) {
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i]);
            if (i < array.length - 1) System.out.print(" ");
        }
        System.out.println();
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // To find Neighbour of a given Pixel
        int[][] a = magicSquare();
        System.out.println("a=");
        printMatrix(a);
        
        System.out.print("Enter the row < size of the Matrix: ");
        int b = scanner.nextInt();
        System.out.print("Enter the Column < size of matrix: ");
        int c = scanner.nextInt();
        
        System.out.println("Element");
        System.out.println(a[b-1][c-1]); // Convert to 0-based indexing
        
        // Adjust indices for 0-based indexing
        b = b - 1;
        c = c - 1;
        
        // 4 Point Neighbour
        int[] N4 = {a[b+1][c], a[b-1][c], a[b][c+1], a[b][c-1]};
        System.out.println("N4=");
        printArray(N4);
        
        // 8 Point Neighbour
        int[] N8 = {a[b+1][c], a[b-1][c], a[b][c+1], a[b][c-1], 
                    a[b+1][c+1], a[b+1][c-1], a[b-1][c-1], a[b-1][c+1]};
        System.out.println("N8=");
        printArray(N8);
        
        // Diagonal Neighbour
        int[] ND = {a[b+1][c+1], a[b+1][c-1], a[b-1][c-1], a[b-1][c+1]};
        System.out.println("ND=");
        printArray(ND);
        
        scanner.close();
    }
}