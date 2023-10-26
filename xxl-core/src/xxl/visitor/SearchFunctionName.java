package xxl.visitor;

import xxl.cell.Cell;
import xxl.content.*;
import xxl.content.literal.*;
import xxl.function.*;

public class SearchFunctionName implements SearchVisitor {
  
  /* Pattern to match in function name */
  private String _name;

  /**
   * Constructor.
   * @param name
   */
  public SearchFunctionName(String name) {
    _name = name;
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
