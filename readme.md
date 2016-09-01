# ID Images API
##Description
This API is designed to GET an OSU ID Image from Banner.

### Dependencies

#### Oracle Driver

Download `ojdbc6_g.jar` from [Oracle](http://www.oracle.com/technetwork/apps-tech/jdbc-112010-090769.html) and save in `bin/` directory.

## Resources

The Web API definition is contained in the [Swagger specification](swagger.yaml).

### GET idimages/{osuid}
Returns the ID image of a person in the media type image/jpg. Errors are returned in application/json format.