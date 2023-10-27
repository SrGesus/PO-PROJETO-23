package xxl.visitor;

import java.util.ArrayList;
import java.util.List;

import xxl.cell.Cell;
import xxl.content.*;
import xxl.content.literal.*;
import xxl.exceptions.InvalidExpressionException;
import xxl.function.*;

public class CopyContent implements Visitor<Content> {

  public Content visitCell(Cell c) {
    if (c.getContent() == null) return null;
    return c.getContent().accept(this);
  }

  public Content visitCellReference(CellReference c) {
    Content newContent = new CellReference(c.getAddress(), c.getCell());
    newContent.value();
    return newContent;
  }

  public Content visitIntLiteral(IntLiteral l) {
    return new IntLiteral(l.getInt());
  }

  public Content visitStringLiteral(StringLiteral l) {
    try {
      return new StringLiteral("\'" + l.getString());
    } catch (InvalidExpressionException e) {
      /** Logically Unreachable */
      e.printStackTrace();
      return null;
    }
  }

  public Content visitErrorLiteral(ErrorLiteral l) {
    return new ErrorLiteral();
  }

  public Content visitADD(ADD f) {
    Content newContent = new ADD(f.getArg(0), f.getArg(1));
    newContent.value();
    return newContent;
  }

  public Content visitSUB(SUB f) {
    Content newContent =  new SUB(f.getArg(0), f.getArg(1));
    newContent.value();
    return newContent;
  }

  public Content visitMUL(MUL f) {
    Content newContent =  new MUL(f.getArg(0), f.getArg(1));
    newContent.value();
    return newContent;
  }

  public Content visitDIV(DIV f) {
    Content newContent = new DIV(f.getArg(0), f.getArg(1));
    newContent.value();
    return newContent;
  }

  public Content visitAVERAGE(AVERAGE f) {
    List<Content> args = getArgs(f);
    Content newContent = new AVERAGE(args);
    newContent.value();
    return newContent;
  }

  public Content visitPRODUCT(PRODUCT f) {
    List<Content> args = getArgs(f);
    Content newContent = new PRODUCT(args);
    newContent.value();
    return newContent;
  }

  public Content visitCONCAT(CONCAT f) {
    List<Content> args = getArgs(f);
    Content newContent = new CONCAT(args);
    newContent.value();
    return newContent;
  }

  public Content visitCOALESCE(COALESCE f) {
    List<Content> args = getArgs(f);
    Content newContent = new COALESCE(args);
    newContent.value();
    return newContent;
  }

  private List<Content> getArgs(RangeFunction f) {
    List<Content> args = new ArrayList<>();
    for (int i = 0; i < f.size(); i++) {args.add(f.getArg(i));}
    return args;
  }

}
