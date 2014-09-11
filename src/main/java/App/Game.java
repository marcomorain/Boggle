package App;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;
import java.util.Random;
import java.util.Set;

public class Game {
    
    // array[][]
    // table<int, int, char>
    
    private final Table<Integer, Integer, Character> board;
    
    private final Set<String> words;
    private final int n;
    
    public Game(int n, Table<Integer, Integer, Character> board, Set<String> words) {
        this.board = HashBasedTable.create(board);
        this.words = words;
        this.n = n;
    }

    // n - board size
    public Game(int n, long seed, Set<String> words) {
        this.words = ImmutableSet.copyOf(words);
        this.n = n;
        
        for (String word : words) {
            Preconditions.checkArgument(word.length() >= 3, "Words must be 3 letters or longer");
        }
        
        Random random = new Random(seed);
        
        this.board = HashBasedTable.create();
        for (int i=0; i<n; i++) {
            for (int j=0; j<n; j++) {
                this.board.put(i, j, randCharacter(random));
            }
        }
    }
    
    public static void main(String[] args) {
    }
    
    // return value is here is a subset of the strings passed in.
    public Set<String> playBoggle() {
        ImmutableSet.Builder<String> result = ImmutableSet.builder();
        for (int i=0; i<n; i++) {
            for (int j=0; j<n; j++) {
                result.addAll(searchStart(i, j));
            }
        }
        return result.build();
    }
    
    // Assert that i is a valid index [0,n)
    private boolean validIndex(int i) {
        return i >= 0 && i < n;
    }
    
    protected Set<String> search(int i, int j,
            String matchedSoFar,
            Set<String> candidates,
            Table<Integer, Integer, Character> visited) {
        
        Preconditions.checkArgument(validIndex(i));
        Preconditions.checkArgument(validIndex(j));
        
        String currentMatch = matchedSoFar + board.get(i, j);
        
        
        ImmutableSet.Builder<String> result = ImmutableSet.builder();
        
        if (candidates.contains(currentMatch)) {
            result.add(currentMatch);
        }
        
        Set<String> passingCandidates = Sets.newHashSet();
        for (String candidate : candidates) {
            if (candidate.startsWith(currentMatch)) {
                passingCandidates.add(candidate);
            }
        }
        
        if (passingCandidates.isEmpty()) {
            return ImmutableSet.of();
        }
        
        Table<Integer, Integer, Character> toBeVisited = HashBasedTable.create(visited);
        toBeVisited.remove(i, j);
        
        for (int in = -1; in <= 1; in++) {
            for (int jn = -1; jn <= 1; jn++) {
                if (in == 0 && jn == 0) {
                    continue;
                }
                
                final int nextI = i + in;
                final int nextJ = j + jn;
                
                if (toBeVisited.get(nextI, nextJ) == null) {
                    continue;
                }
                
                if (validIndex(nextI) && validIndex(nextJ)) {
                    result.addAll(search(nextI, nextJ, currentMatch, passingCandidates, toBeVisited));
                }
            }
        }
        
        return result.build();
    }
    
    public Set<String> searchStart(int i, int j) {
        return search(i, j, "", words, HashBasedTable.create(board));
    }

    // TODO: could pass this in a function?
    private Character randCharacter(Random random) {
        int c = 'a' + random.nextInt(26);
        Preconditions.checkState(c <= 'z');
        Preconditions.checkState(c >= 'a');
        return (char)c;
    }
    
    public Table<Integer, Integer, Character> getBoard() {
        return ImmutableTable.copyOf(board);
    }
}
