package application.microprofile;

import java.util.Random;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.eclipse.microprofile.opentracing.Traced;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Traced
@ApplicationScoped
public class Ratings {

  @Inject
  @RestClient
  private RatingsService ratingsService;

  public TwoReviewers getRating(int id) {
    RatingResponse ratings = ratingsService.getRatings(id);
    TwoReviewers twoReviewers = new TwoReviewers();
    Integer stars = ratings.getRatings().get("Reviewer1");
    if (stars != null) {
      twoReviewers.setReviewer1(stars);
    }
    stars = ratings.getRatings().get("Reviewer2");
    if (stars != null) {
      twoReviewers.setReviewer2(stars);
    }
    return twoReviewers;
  }
}
