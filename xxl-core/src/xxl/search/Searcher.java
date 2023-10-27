package xxl.search;

import java.util.ArrayList;
import java.util.List;

import xxl.cell.CellStore;

public class Searcher {

    private SearchMethod _visitor;

    public Searcher(SearchMethod visitor) {
        _visitor = visitor;
    }

    public List<SearchResult> search(CellStore store) {
        List<SearchResult> results = new ArrayList<>();
        store.forEach(sr -> {
            if (sr.getCell().accept(_visitor))
                results.add(sr);
        });
        results.sort(_visitor.getComparator());
        return results;
    }

}
