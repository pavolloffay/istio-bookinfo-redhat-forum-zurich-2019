package application.microprofile;

import java.util.Map;

public class RatingResponse {

  private int id;
  private Map<String, Integer> ratings;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Map<String, Integer> getRatings() {
    return ratings;
  }

  public void setRatings(Map<String, Integer> ratings) {
    this.ratings = ratings;
  }
}
