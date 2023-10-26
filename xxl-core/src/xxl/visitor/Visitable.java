package xxl.visitor;

/**
 * Interface for classes visitable by a search visitor
 */
public interface Visitable {

  /**
   * @param <T> the type the visitor being accepted returns
   * @param visitor 
   * @return the value visitor seeks
   */
  public <T> T accept(Visitor<T> visitor);

}
