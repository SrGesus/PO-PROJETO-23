package xxl.app.search;

import pt.tecnico.uilib.menus.Command;
import xxl.Spreadsheet;
import xxl.search.SearchFunctionName;
import xxl.search.SearchVisitor;

/**
 * Command for searching function names.
 */
class DoShowFunctions extends Command<Spreadsheet> {

    DoShowFunctions(Spreadsheet receiver) {
        super(Label.SEARCH_FUNCTIONS, receiver);
        addStringField("function", Prompt.searchFunction());
    }

    @Override
    protected final void execute() {
        SearchVisitor v = new SearchFunctionName(stringField("function"));
        _receiver.searchStore(v).forEach(str -> _display.popup(str));
    }

}
