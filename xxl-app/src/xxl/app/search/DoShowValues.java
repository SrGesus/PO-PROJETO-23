package xxl.app.search;

import pt.tecnico.uilib.menus.Command;
import xxl.Spreadsheet;
import xxl.search.SearchValue;
import xxl.search.SearchVisitor;

/**
 * Command for searching content values.
 */
class DoShowValues extends Command<Spreadsheet> {

    DoShowValues(Spreadsheet receiver) {
        super(Label.SEARCH_VALUES, receiver);
        addStringField("value", Prompt.searchValue());
    }

    @Override
    protected final void execute() {
        SearchVisitor v = new SearchValue(_receiver, stringField("value"));
        _receiver.searchStore(v).forEach(str -> _display.popup(str));
    }

}
