package xxl.search;

import java.util.Comparator;

import xxl.cell.Cell;
import xxl.content.*;
import xxl.content.literal.*;
import xxl.function.*;
import xxl.visitor.ExpressionGenerator;

public class SearchFunctionName implements SearchMethod {
  
  /* Pattern to match in function name */
  private String _name;

  /** Compares by Address */
  private static Comparator<SearchResult> BY_ADDRESS = new Comparator<SearchResult>() {
    public int compare(SearchResult o1, SearchResult o2) {
      int value = o1.getCell().accept(new ExpressionGenerator()).split("=")[1].split("\\(")[0]
        .compareTo(o2.getCell().accept(new ExpressionGenerator()).split("=")[1].split("\\(")[0]);
      if (value == 0)
        return o1.getAddress().compareTo(o2.getAddress());
      else
        return value;
    }
  };

  /**
   * Constructor.
   * @param name
   */
  public SearchFunctionName(String name) {
    _name = name;
  }

  public Comparator<SearchResult> getComparator() {
    return BY_ADDRESS;
  }  

  public Boolean visitCell(Cell c) {
    return c.getContent().accept(this);
  }

  public Boolean visitCellReference(CellReference c) {
    return false;
  }

  public Boolean visitIntLiteral(IntLiteral l) {
    return false;
  }

  public Boolean visitStringLiteral(StringLiteral l) {
    return false;
  }

  public Boolean visitErrorLiteral(ErrorLiteral l) {
    return false;
  }

  public Boolean visitADD(ADD f) {
    return "ADD".contains(_name);
  }

  public Boolean visitSUB(SUB f) {
    return "SUB".contains(_name);
  }

  public Boolean visitMUL(MUL f) {
    return "MUL".contains(_name);
  }

  public Boolean visitDIV(DIV f) {
    return "DIV".contains(_name);
  }

  public Boolean visitAVERAGE(AVERAGE f) {
    return "AVERAGE".contains(_name);
  }

  public Boolean visitPRODUCT(PRODUCT f) {
    return "PRODUCT".contains(_name);
  }

  public Boolean visitCONCAT(CONCAT f) {
    return "CONCAT".contains(_name);
  }

  public Boolean visitCOALESCE(COALESCE f) {
    return "COALESCE".contains(_name);
  }
  
}
