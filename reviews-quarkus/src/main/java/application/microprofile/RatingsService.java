package application.microprofile;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/")
@RegisterRestClient
public interface RatingsService {

  @GET
  @Path("/ratings/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  RatingResponse getRatings(@PathParam("id") int id);
}
