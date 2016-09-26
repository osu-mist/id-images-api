package edu.oregonstate.mist.idimages.resources

import com.codahale.metrics.annotation.Timed
import edu.oregonstate.mist.api.AuthenticatedUser
import edu.oregonstate.mist.api.Resource
import edu.oregonstate.mist.idimages.IDImageDAO
import io.dropwizard.auth.Auth
import javax.imageio.ImageIO
import javax.ws.rs.PathParam
import javax.ws.rs.core.Response
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import java.awt.Graphics2D
import java.awt.Image
import java.awt.image.BufferedImage
import java.sql.Blob
import org.slf4j.LoggerFactory
import org.slf4j.Logger

@Path('/idimages/')
@Produces("image/jpg")
class IdImagesResource extends Resource {
    Logger logger = LoggerFactory.getLogger(IdImagesResource.class)

    private IDImageDAO idImageDAO

    IdImagesResource(IDImageDAO idImageDAO) {
        this.idImageDAO = idImageDAO
    }
    /**
     * GET Image by OSU ID
     */
    @GET
    @Path ('{id: \\d+}')
    @Produces("image/jpg")
    @Timed
    public Response getByOSUID(@Auth AuthenticatedUser _, @PathParam('id') String id) {
        Integer bannerPIDM = idImageDAO.getPIDM(id)

        if (!bannerPIDM) {
            return notFound().type(MediaType.APPLICATION_JSON).build()
        }
        Blob image = idImageDAO.getByID(id)

        //Return a placeholder image if a person exists but doesn't have an ID image
        if (!image) {
            return ok(getImageStream(ImageIO.read(new File("images/defaultImage.jpg")))).build()
        }
        //Return an ID image of a person
        try {
            Response.ok(getImageStream(ImageIO.read(image.getBinaryStream()))).build()
        } catch (Exception e) {
            logger.error("Exception while calling getIDImages", e)
            return internalServerError("Internal server error").
                    type(MediaType.APPLICATION_JSON).build()
        }
    }
    //Resizes a BufferedImage to a width constraint and calculates the height accordingly
    public static BufferedImage resizeWidthConstraint(BufferedImage img, int newWidth) {
        Integer newHeight = ((img.getHeight() * newWidth) / img.getWidth())

        Image tmp = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH)
        BufferedImage resizedImg = new BufferedImage(newWidth, newHeight, img.getType())

        Graphics2D g2d = resizedImg.createGraphics()
        g2d.drawImage(tmp, 0, 0, null)
        g2d.dispose()

        resizedImg
    }
    //Converts a BufferedImage into a format able to returned by the API
    public def getImageStream(BufferedImage origImg) {
        BufferedImage resizedImg = resizeWidthConstraint(origImg, 150)
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
        ImageIO.write(resizedImg, "jpg", outputStream)
        byte[] imageData = outputStream.toByteArray()

        imageData
    }
}
