import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgcodecs.Imgcodecs;
import java.util.ArrayList;
import java.util.List;

public class SimpleFourierTransform {
    
    public static void main(String[] args) {
        // Load OpenCV library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        
        System.out.println("Starting Fourier Transform Demo...\n");
        
        // Part (a): Basic Fourier Transform
        partA();
        
        // Part (b): Rotation Property
        partB();
        
        // Part (c): Convolution Property
        partC();
        
        System.out.println("\nAll images have been saved to disk!");
        System.out.println("Result: Thus the fourier transform and inverse fourier transform has been studied");
        System.out.println("as well as the rotation and convolution properties of the fourier transform.");
    }
    
    private static void partA() {
        System.out.println("Part (a): Fourier Transform and Inverse Transform");
        
        // Create image with white square
        Mat img = Mat.zeros(256, 256, CvType.CV_32F);
        Rect square = new Rect(110, 110, 30, 30);
        img.submat(square).setTo(new Scalar(1.0));
        saveImage(img, "1a_input_image.png");
        
        // Compute FFT
        Mat fft = computeFFT(img);
        Mat magnitude = getMagnitude(fft);
        saveImage(magnitude, "1b_fourier_transform.png");
        
        // Inverse FFT
        Mat inverse = computeInverseFFT(fft);
        saveImage(inverse, "1c_inverse_transform.png");
    }
    
    private static void partB() {
        System.out.println("\nPart (b): Rotation Property");
        
        // Create original image
        Mat img = Mat.zeros(256, 256, CvType.CV_32F);
        Rect square = new Rect(110, 110, 30, 30);
        img.submat(square).setTo(new Scalar(1.0));
        saveImage(img, "2a_original_image.png");
        
        // FFT of original
        Mat magnitude1 = getMagnitude(computeFFT(img));
        saveImage(magnitude1, "2b_fft_original.png");
        
        // Rotate image by 45 degrees
        Point center = new Point(128, 128);
        Mat rotMatrix = Imgproc.getRotationMatrix2D(center, 45, 1.0);
        Mat rotated = new Mat();
        Imgproc.warpAffine(img, rotated, rotMatrix, img.size());
        saveImage(rotated, "2c_rotated_image.png");
        
        // FFT of rotated
        Mat magnitude2 = getMagnitude(computeFFT(rotated));
        saveImage(magnitude2, "2d_fft_rotated.png");
    }
    
    private static void partC() {
        System.out.println("\nPart (c): Convolution Property");
        
        // Create first image
        Mat img1 = Mat.zeros(256, 256, CvType.CV_32F);
        Rect square1 = new Rect(110, 110, 30, 30);
        img1.submat(square1).setTo(new Scalar(1.0));
        saveImage(img1, "3a_first_image.png");
        
        // Create second image
        Mat img2 = Mat.zeros(256, 256, CvType.CV_32F);
        Rect square2 = new Rect(170, 170, 30, 30);
        img2.submat(square2).setTo(new Scalar(1.0));
        saveImage(img2, "3b_second_image.png");
        
        // Spatial convolution
        Mat conv = new Mat();
        Imgproc.filter2D(img1, conv, -1, img2);
        saveImage(conv, "3c_convolution_spatial.png");
        
        // Frequency domain multiplication
        Mat fft1 = computeFFT(img1);
        Mat fft2 = computeFFT(img2);
        Mat product = new Mat();
        Core.mulSpectrums(fft1, fft2, product, 0);
        Mat result = computeInverseFFT(product);
        result = fftShift(result);
        saveImage(result, "3d_convolution_frequency.png");
    }
    
    private static Mat computeFFT(Mat input) {
        List<Mat> planes = new ArrayList<>();
        planes.add(input.clone());
        planes.add(Mat.zeros(input.size(), CvType.CV_32F));
        
        Mat complex = new Mat();
        Core.merge(planes, complex);
        Core.dft(complex, complex);
        
        return complex;
    }
    
    private static Mat computeInverseFFT(Mat complex) {
        Mat result = new Mat();
        Core.idft(complex, result, Core.DFT_SCALE | Core.DFT_REAL_OUTPUT);
        return result;
    }
    
    private static Mat getMagnitude(Mat complex) {
        List<Mat> planes = new ArrayList<>();
        Core.split(complex, planes);
        
        Mat magnitude = new Mat();
        Core.magnitude(planes.get(0), planes.get(1), magnitude);
        
        // log(1 + magnitude)
        Core.add(magnitude, Scalar.all(1), magnitude);
        Core.log(magnitude, magnitude);
        
        // Shift and normalize
        magnitude = fftShift(magnitude);
        Core.normalize(magnitude, magnitude, 0, 1, Core.NORM_MINMAX);
        
        return magnitude;
    }
    
    private static Mat fftShift(Mat input) {
        Mat output = input.clone();
        int cx = output.cols() / 2;
        int cy = output.rows() / 2;
        
        Mat q0 = new Mat(output, new Rect(0, 0, cx, cy));
        Mat q1 = new Mat(output, new Rect(cx, 0, cx, cy));
        Mat q2 = new Mat(output, new Rect(0, cy, cx, cy));
        Mat q3 = new Mat(output, new Rect(cx, cy, cx, cy));
        
        Mat tmp = new Mat();
        q0.copyTo(tmp); q3.copyTo(q0); tmp.copyTo(q3);
        q1.copyTo(tmp); q2.copyTo(q1); tmp.copyTo(q2);
        
        return output;
    }
    
    private static void saveImage(Mat img, String filename) {
        Mat display = new Mat();
        img.convertTo(display, CvType.CV_8U, 255.0);
        Imgcodecs.imwrite(filename, display);
        System.out.println("Saved: " + filename);
    }
}