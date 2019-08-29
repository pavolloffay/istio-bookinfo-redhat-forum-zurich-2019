package application.microprofile;

public class TwoReviewers {

  private int reviewer1 = -1;
  private int reviewer2 = -1;

  public void setReviewer1(int reviewer1) {
    this.reviewer1 = reviewer1;
  }

  public void setReviewer2(int reviewer2) {
    this.reviewer2 = reviewer2;
  }

  public int getReviewer1() {
    return reviewer1;
  }

  public int getReviewer2() {
    return reviewer2;
  }
}
