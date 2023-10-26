package xxl.visitor;

import xxl.Spreadsheet;
import xxl.cell.Cell;
import xxl.content.*;
import xxl.content.literal.*;
import xxl.exceptions.FunctionNameException;
import xxl.exceptions.UnexpectedContentException;
import xxl.function.*;

public class SearchValue implements SearchVisitor {
  
  /* Value to be matched */
  private Content _value;

  /**
   * Constructor.
   * @param name
   */
  public SearchValue(Spreadsheet spreadsheet, String expression) {
    try {
        _value = spreadsheet.parseContent(expression, false);
    } catch (FunctionNameException e) {
        /** Guaranteed to be Unreachable */
        e.printStackTrace();
    }
  }

  public Boolean visitCell(Cell c) {
    return c.value().accept(this);
  }

  public Boolean visitCellReference(CellReference c) {
    return c.value().accept(this);
  }

  public Boolean visitIntLiteral(IntLiteral l) {
    try {
        return l.getInt() == _value.getInt();
    } catch (UnexpectedContentException e) {
        return false;
    }
  }

  public Boolean visitStringLiteral(StringLiteral l) {
    try {
        return l.getString().equals(_value.getString());
    } catch (UnexpectedContentException e) {
        return false;
    }
  }

  public Boolean visitErrorLiteral(ErrorLiteral l) {
    return false;
  }

  public Boolean visitADD(ADD f) {
    return f.value().accept(this);
  }

  public Boolean visitSUB(SUB f) {
    return f.value().accept(this);
  }

  public Boolean visitMUL(MUL f) {
    return f.value().accept(this);
  }

  public Boolean visitDIV(DIV f) {
    return f.value().accept(this);
  }

  public Boolean visitAVERAGE(AVERAGE f) {
    return f.value().accept(this);
  }

  public Boolean visitPRODUCT(PRODUCT f) {
    return f.value().accept(this);
  }

  public Boolean visitCONCAT(CONCAT f) {
    return f.value().accept(this);
  }

  public Boolean visitCOALESCE(COALESCE f) {
    return f.value().accept(this);
  }
  
}
