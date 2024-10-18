package ch.heigvd.dai;

public class ExceptionHelper {
  private boolean verbose;

  public ExceptionHelper(boolean verbose) {
    this.verbose = verbose;
  }

  public void printMessage(String message, Exception e) {
    if (verbose) {
      System.err.println(message);
      e.printStackTrace();
      return;
    }

    System.err.println(message);
    System.err.println(e.getMessage());
  }
}
