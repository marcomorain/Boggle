/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package App;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Table;
import java.util.Set;
import static org.hamcrest.CoreMatchers.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class AppTest {
    

    @Test
    public void testSomeMethod() {
        Game app = new Game(2, 4, ImmutableSet.of("foo"));
        //app.getBoard().get(1, 1)
        assertThat(app.getBoard().get(1, 1), equalTo('w'));
    }
    
    @Test
    public void isShouldFindNoWords() {
        Game app = new Game(2, 4, ImmutableSet.of("cat", "dog"));
        assertThat(app.playBoggle().size(), equalTo(0));
    }
    
    private Table make(String... words) {
        return makeArray(words);
    }
    
    private Table makeArray(String words[]) {
        Table<Integer, Integer, Character> board = HashBasedTable.create();
        for(int i=0; i<words.length; i++) {
            for (int j=0; j < words[i].length(); j++) {
                final char c = words[i].charAt(j);
                board.put(i, j, c);
            }
        }
        
        return board;
    }
    
    @Test
    public void isShouldFindAKnownWord() {
        Table<Integer, Integer, Character> board = make(
                "cat",
                "zzz",
                "zzz");
        Game game = new Game(3, board, ImmutableSet.of("cat"));
        
        assertThat(game.playBoggle(), hasItems("cat"));
    }
    
    @Test
    public void isShouldDistinctWords() {
        Table<Integer, Integer, Character> board = make(
                "cat",
                "xxx",
                "ooz");
        
        Game game = new Game(3, board, ImmutableSet.of("cat", "zoo", "hammer"));
        
        Set<String> result = game.playBoggle();
        assertThat(result, hasItems("cat", "zoo"));
        assertThat(result.size(), equalTo(2));
                
    }
    
    @Test
    public void isShouldFindWordsWithTheSameStem() {
        Table<Integer, Integer, Character> board = make(
                "cbz",
                "hat",
                "zzz");
        Game game = new Game(3, board, ImmutableSet.of("cat", "hat", "bat"));
        
        assertThat(game.playBoggle(), hasItems("cat"));
    }
    
//    @Test
//    public void isShouldFindKnownWords() {
//        Table<Integer, Integer, Character> board = HashBasedTable.create();
//        // C B H
//        // Z A T
//        // Z Z Z
//        board.put(0, 0, 'C');
//        
//        Game game = new Game(3, board, ImmutableSet.of("cat", "bat", "hat"));
//    }
    // @pending // word extension
    
}
