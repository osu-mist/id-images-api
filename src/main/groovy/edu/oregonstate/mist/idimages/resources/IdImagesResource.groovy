package edu.oregonstate.mist.idimages.resources

import com.codahale.metrics.annotation.Timed
import edu.oregonstate.mist.api.AuthenticatedUser
import edu.oregonstate.mist.api.Resource
import edu.oregonstate.mist.idimages.IDImageDAO
import io.dropwizard.auth.Auth
import io.dropwizard.jersey.params.IntParam
import javax.imageio.ImageIO
import javax.ws.rs.PathParam
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
        Blob image = idImageDAO.getByID(id)

        if (!image) {
            return notFound().type(MediaType.APPLICATION_JSON).build()
        }
        try {
            BufferedImage buffImg = ImageIO.read(image.getBinaryStream())
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
            ImageIO.write(buffImg, "jpg", outputStream)
            byte[] imageData = outputStream.toByteArray()
            Response.ok(imageData).build()
        } catch (Exception e) {
            logger.error("Exception while calling getIDImages", e)
            return internalServerError("Internal server error").
                    type(MediaType.APPLICATION_JSON).build()
        }
    }
}
