package edu.oregonstate.mist.idimages

import org.junit.Test

import javax.imageio.ImageIO
import java.awt.image.BufferedImage

class ImageManipulationTest {
    //Load test image from resources
    ClassLoader classloader = Thread.currentThread().getContextClassLoader()
    InputStream imageInputStream = classloader.getResourceAsStream("sampleimage.jpg")
    BufferedImage testImage = ImageIO.read(imageInputStream)

    private static BufferedImage byteToBuffImg(byte[] imageData) {
        ImageIO.read(new ByteArrayInputStream(imageData))
    }

    @Test
    public void testNoResize() {
        byte[] imageData = ImageManipulation.getImageStream(testImage, null)

        assert testImage.getWidth() == byteToBuffImg(imageData).getWidth()
        assert testImage.getHeight() == byteToBuffImg(imageData).getHeight()
    }

    @Test
    public void testResize() {
        int newWidth = 175
        byte[] imageData = ImageManipulation.getImageStream(testImage, newWidth)
        Integer expectedHeight = ((testImage.getHeight() * newWidth) / testImage.getWidth())

        assert byteToBuffImg(imageData).getWidth() == newWidth
        assert byteToBuffImg(imageData).getHeight() == expectedHeight
    }
}