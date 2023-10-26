package xxl.search;

import xxl.visitor.Visitor;

/**
 * A Content visitor that will define whether or not 
 * Content is a match for a given search.
 */
public interface SearchVisitor extends Visitor<Boolean> {
  
}
