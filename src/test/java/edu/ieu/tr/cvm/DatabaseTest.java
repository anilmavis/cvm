package edu.ieu.tr.cvm;

import junit.framework.TestCase;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

public class DatabaseTest {

    @Test
    public void testGetInstance() throws SQLException, ClassNotFoundException {
        assertNull(Database.getInstance());
    }


    @Test
    public void testInsert() throws SQLException, ClassNotFoundException {
        int oldlength=Database.getInstance().getAll().size();
        Database.getInstance().open();
        Database.getInstance().insert(new Cv("hümay",2001,3,"humayzehraozer@gmail.com","student","home","address","05533000958","github.com",new HashMap<>(),new HashMap<>(),new HashMap<>(),new ArrayList<>()));
        int newlength=Database.getInstance().getAll().size();
        assertEquals(newlength, oldlength + 1);


}
    }