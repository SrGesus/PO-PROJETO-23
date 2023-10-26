package xxl.visitor;

import xxl.cell.Cell;
import xxl.content.CellReference;
import xxl.content.literal.ErrorLiteral;
import xxl.content.literal.IntLiteral;
import xxl.content.literal.StringLiteral;
import xxl.function.ADD;
import xxl.function.AVERAGE;
import xxl.function.COALESCE;
import xxl.function.CONCAT;
import xxl.function.DIV;
import xxl.function.MUL;
import xxl.function.PRODUCT;
import xxl.function.SUB;

public class ExpressionGenerator implements Visitor<String> {

  public String visitCell(Cell c) {
    if (c.isEmpty()) return "";
    if (c.isLiteral()) return c.value().accept(this);
    return c.value().accept(this) + "=" + c.getContent().accept(this);
  }

  public String visitCellReference(CellReference c) {
    return c.getAddress().toString();
  }

  public String visitIntLiteral(IntLiteral l) {
    return String.valueOf(l.getInt());
  }

  public String visitStringLiteral(StringLiteral l) {
    return "\'" + l.getString();
  }

  public String visitErrorLiteral(ErrorLiteral l) {
    return "#VALUE";
  }

  public String visitADD(ADD f) {
    return "ADD(" + f.getArg(0).accept(this) + "," + f.getArg(1).accept(this) + ")";
  }

  public String visitSUB(SUB f) {
    return "SUB(" + f.getArg(0).accept(this) + "," + f.getArg(1).accept(this) + ")";
  }

  public String visitMUL(MUL f) {
    return "MUL(" + f.getArg(0).accept(this) + "," + f.getArg(1).accept(this) + ")";
  }

  public String visitDIV(DIV f) {
    return "DIV(" + f.getArg(0).accept(this) + "," + f.getArg(1).accept(this) + ")";
  }

  public String visitAVERAGE(AVERAGE f) {
    return "AVERAGE(" + f.getArg(0).accept(this) + ":" + f.getArg(f.size()-1).accept(this) + ")";
  }

  public String visitPRODUCT(PRODUCT f) {
    return "PRODUCT(" + f.getArg(0).accept(this) + ":" + f.getArg(f.size()-1).accept(this) + ")";
  }

  public String visitCONCAT(CONCAT f) {
    return "CONCAT(" + f.getArg(0).accept(this) + ":" + f.getArg(f.size()-1).accept(this) + ")";
  }

  public String visitCOALESCE(COALESCE f) {
    return "COALESCE(" + f.getArg(0).accept(this) + ":" + f.getArg(f.size()-1).accept(this) + ")";
  }

}
