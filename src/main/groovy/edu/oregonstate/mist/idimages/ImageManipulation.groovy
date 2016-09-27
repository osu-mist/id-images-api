package edu.oregonstate.mist.idimages

import javax.imageio.ImageIO
import java.awt.Graphics2D
import java.awt.Image
import java.awt.image.BufferedImage

class ImageManipulation {

    //Converts a BufferedImage into a format able to returned by the API
    public static def getImageStream(BufferedImage origImg) {
        int resizeToWidth = 150

        BufferedImage resizedImg = resizeWidthConstraint(origImg, resizeToWidth)
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
        ImageIO.write(resizedImg, "jpg", outputStream)
        byte[] imageData = outputStream.toByteArray()

        imageData
    }

    //Resizes a BufferedImage to a width constraint and calculates the height accordingly
    private static BufferedImage resizeWidthConstraint(BufferedImage img, int newWidth) {
        Integer newHeight = ((img.getHeight() * newWidth) / img.getWidth())

        Image tmp = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH)
        BufferedImage resizedImg = new BufferedImage(newWidth, newHeight, img.getType())

        Graphics2D g2d = resizedImg.createGraphics()
        g2d.drawImage(tmp, 0, 0, null)
        g2d.dispose()

        resizedImg
    }
}