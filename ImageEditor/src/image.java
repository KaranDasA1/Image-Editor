import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javax.imageio.ImageIO;
public class image {
    // black and white
    public static BufferedImage blackandwhite(BufferedImage inputImage) {
        int height = inputImage.getHeight();
        int width = inputImage.getWidth();
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                outputImage.setRGB(j, i, inputImage.getRGB(j, i));
            }
        }
        return outputImage;
    }
    // increase brightness
    public static BufferedImage incbrightness(BufferedImage inputImage, int increase) {
        int height = inputImage.getHeight();
        int width = inputImage.getWidth();
        BufferedImage outImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                Color pixel = new Color(inputImage.getRGB(x, y));
                int r = pixel.getRed();
                int g = pixel.getGreen();
                int b = pixel.getBlue();
                g = g + (increase * g / 100);
                b = b + (increase * b / 100);
                r = r + (increase * r / 100);
                if (r > 255)
                    r = 255;
                if (b > 255)
                    b = 255;
                if (g > 255)
                    g = 255;
                if (r < 0)
                    r = 0;
                if (b < 0)
                    b = 0;
                if (g < 0)
                    g = 0;
                Color newpixel = new Color(r, g, b);
                outImage.setRGB(x, y, newpixel.getRGB());
            }
        }
        return outImage;
    }

    // decrease brightness
    public static BufferedImage decbrightness(BufferedImage inputImage, int decrease) {
        int height = inputImage.getHeight();
        int width = inputImage.getWidth();
        BufferedImage outImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                Color pixel = new Color(inputImage.getRGB(x, y));
                int r = pixel.getRed();
                int g = pixel.getGreen();
                int b = pixel.getBlue();
                g = g - (decrease * g / 100);
                b = b - (decrease * b / 100);
                r = r - (decrease * r / 100);
                if (r > 255)
                    r = 255;
                if (b > 255)
                    b = 255;
                if (g > 255)
                    g = 255;
                if (r < 0)
                    r = 0;
                if (b < 0)
                    b = 0;
                if (g < 0)
                    g = 0;
                Color newpixel = new Color(r, g, b);
                outImage.setRGB(x, y, newpixel.getRGB());
            }
        }
        return outImage;
    }
    // rotate
    public static BufferedImage rotate(BufferedImage inputImage, int degrees) {
        AffineTransform transform = new AffineTransform();
        transform.rotate(Math.toRadians(degrees), inputImage.getWidth() / 2, inputImage.getHeight() / 2);

        AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
        BufferedImage outputImage = new BufferedImage(inputImage.getHeight(), inputImage.getWidth(),
                inputImage.getType());

        op.filter(inputImage, outputImage);

        return outputImage;
    }

    // blur
    public static BufferedImage blur(BufferedImage inputImage, int radius) {
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        BufferedImage blurredImage = new BufferedImage(width, height, inputImage.getType());

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int avgRed = 0;
                int avgGreen = 0;
                int avgBlue = 0;
                int numPixels = 0;

                for (int offsetY = -radius; offsetY <= radius; offsetY++) {
                    for (int offsetX = -radius; offsetX <= radius; offsetX++) {
                        int posX = x + offsetX;
                        int posY = y + offsetY;

                        if (posX >= 0 && posX < width && posY >= 0 && posY < height) {
                            int pixel = inputImage.getRGB(posX, posY);
                            avgRed += (pixel >> 16) & 0xFF;
                            avgGreen += (pixel >> 8) & 0xFF;
                            avgBlue += pixel & 0xFF;
                            numPixels++;
                        }
                    }
                }
                avgRed /= numPixels;
                avgGreen /= numPixels;
                avgBlue /= numPixels;
                int blurredPixel = (avgRed << 16) | (avgGreen << 8) | avgBlue;
                blurredImage.setRGB(x, y, blurredPixel);
            }
        }
        return blurredImage;
    }
    private static void saveImage(BufferedImage image, String outputPath) {
        try {
            File output = new File(outputPath);
            ImageIO.write(image, "jpg", output);
            System.out.println("Image saved as " + outputPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            Scanner sc = new Scanner(System.in);

            // Input and output file paths
            System.out.print("path of the input image: ");
            String inputImagePath = sc.nextLine();
            BufferedImage image = ImageIO.read(new File(inputImagePath));

            if (image == null) {
                System.out.println("Could not read the input image.");
                return;
            }

            System.out.print("path for the output image: ");
            String outputImagePath = sc.nextLine();

            System.out.println("Image loaded successfully");

            System.out.println("1. BlackAndWhite");
            System.out.println("2. Increase Brightness");
            System.out.println("3. Decrease Brightness");
            System.out.println("4. Rotate");
            System.out.println("5. Blur");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    image = blackandwhite(image);
                    break;
                case 2:
                    image = incbrightness(image, 25);
                    break;
                case 3:
                    image = decbrightness(image, 25);
                    break;
                case 4:
                    image = rotate(image, 90);
                    break;
                case 5:
                    image = blur(image, 5);
                    break;
                case 6:
                    System.out.println("Exiting");
                    break;
                default:
                    System.out.println("Invalid choice");
            }

            if (choice >= 1 && choice <= 5) {
                saveImage(image, outputImagePath);
            }

            sc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}