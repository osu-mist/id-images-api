package edu.oregonstate.mist.idimages.resources

import com.codahale.metrics.annotation.Timed
import edu.oregonstate.mist.api.AuthenticatedUser
import edu.oregonstate.mist.api.Resource
import edu.oregonstate.mist.idimages.IDImageDAO
import edu.oregonstate.mist.idimages.ImageManipulation
import io.dropwizard.auth.Auth
import javax.imageio.ImageIO
import javax.ws.rs.PathParam
import javax.ws.rs.QueryParam
import javax.ws.rs.core.Response
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import java.awt.image.BufferedImage
import java.sql.Blob
import org.slf4j.LoggerFactory
import org.slf4j.Logger

@Path('/idimages/')
@Produces("image/jpg")
class IdImagesResource extends Resource {
    Logger logger = LoggerFactory.getLogger(IdImagesResource.class)

    private IDImageDAO idImageDAO

    public static final Integer maxWidth = 2000

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
    public Response getByOSUID(@Auth AuthenticatedUser _,
                               @PathParam('id') String id,
                               @QueryParam('w') Integer resizeWidth) {
        Integer bannerPIDM = idImageDAO.getPIDM(id)

        if (!bannerPIDM) {
            return notFound().type(MediaType.APPLICATION_JSON).build()
        }
        if ((resizeWidth != null) && resizeWidth <= 0) {
            String smallWidth = "Width must be greater than 0"
            return badRequest(smallWidth).type(MediaType.APPLICATION_JSON).build()
        } else if (resizeWidth > maxWidth) {
            String largeWidth = "Width must be value from 1-" + maxWidth + "."
            return badRequest(largeWidth).type(MediaType.APPLICATION_JSON).build()
        }
        Blob imageData = idImageDAO.getByID(id)

        //Return a placeholder image if a person exists but doesn't have an ID image
        if (!imageData) {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader()
            InputStream imageInputStream = classloader.getResourceAsStream("defaultImage.jpg")
            BufferedImage defaultImage = ImageIO.read(imageInputStream)
            return ok(ImageManipulation.getImageStream(defaultImage, resizeWidth)).build()
        }
        //Return an ID image of a person
        try {
            BufferedImage idImage = ImageIO.read(imageData.getBinaryStream())
            Response.ok(ImageManipulation.getImageStream(idImage, resizeWidth)).build()
        } catch (Exception e) {
            logger.error("Exception while calling getIDImages", e)
            return internalServerError("Internal server error").
                    type(MediaType.APPLICATION_JSON).build()
        }
    }
}
