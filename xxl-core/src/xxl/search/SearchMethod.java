package xxl.search;

import java.util.Comparator;

import xxl.visitor.Visitor;

/**
 * A Content visitor that will define whether or not 
 * Content is a match for a given search.
 */
public interface SearchMethod extends Visitor<Boolean> {
    public Comparator<SearchResult> getComparator();
}
