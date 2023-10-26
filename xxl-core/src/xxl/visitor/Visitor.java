package xxl.visitor;

import xxl.cell.Cell;
import xxl.content.CellReference;
import xxl.content.literal.*;
import xxl.function.*;

public interface Visitor<T> {
    public T visitCell(Cell c);
    public T visitCellReference(CellReference cr);
    public T visitIntLiteral(IntLiteral l);
    public T visitStringLiteral(StringLiteral l);
    public T visitErrorLiteral(ErrorLiteral l);
    public T visitADD(ADD f);
    public T visitSUB(SUB f);
    public T visitMUL(MUL f);
    public T visitDIV(DIV f);
    public T visitAVERAGE(AVERAGE f);
    public T visitPRODUCT(PRODUCT f);
    public T visitCONCAT(CONCAT f);
    public T visitCOALESCE(COALESCE f);
}
