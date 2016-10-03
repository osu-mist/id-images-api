package edu.oregonstate.mist.idimages

import javax.imageio.ImageIO
import java.awt.Graphics2D
import java.awt.Image
import java.awt.image.BufferedImage

class ImageManipulation {

    //Converts a BufferedImage into a format able to returned by the API
    public static def getImageStream(BufferedImage image, Integer resizeWidth) {
        if (resizeWidth) {
            image = resizeWidthConstraint(image, resizeWidth)
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
        ImageIO.write(image, "jpg", outputStream)

        outputStream.toByteArray()
    }

    //Resizes a BufferedImage to a width constraint and calculates the height accordingly
    private static BufferedImage resizeWidthConstraint(BufferedImage img, Integer resizeWidth) {
        Integer newHeight = ((img.getHeight() * resizeWidth) / img.getWidth())
        Image tmp = img.getScaledInstance(resizeWidth, newHeight, Image.SCALE_SMOOTH)
        BufferedImage resizedImg = new BufferedImage(resizeWidth, newHeight, img.getType())

        Graphics2D g2d = resizedImg.createGraphics()
        g2d.drawImage(tmp, 0, 0, null)
        g2d.dispose()

        resizedImg
    }
}