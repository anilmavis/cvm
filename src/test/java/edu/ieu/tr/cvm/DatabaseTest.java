package edu.ieu.tr.cvm;

import junit.framework.TestCase;
import org.junit.Test;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

public class DatabaseTest {

    @Test
    public void testGetInstance() throws SQLException, ClassNotFoundException {
        Database.getInstance().open();
        assertTrue(Database.getInstance()  != null );
        Database.getInstance().close();
    }


    @Test
    public void testInsert() throws SQLException, ClassNotFoundException {
        Database.getInstance().open();
        int oldlength=Database.getInstance().getAll().size();
        Database.getInstance().insert(new Cv("hümay",2001,3,"humayzehraozer@gmail.com","student","home","address","05533000958","github.com",new HashMap<>(),new HashMap<>(),new HashMap<>(),new ArrayList<>()));
        int newlength=Database.getInstance().getAll().size();
        assertEquals(newlength, oldlength + 1);
        Database.getInstance().close();

    }

    @Test
    public void testInsertwithEmptyName() throws SQLException, ClassNotFoundException {
        Database.getInstance().open();
        int oldlength=Database.getInstance().getAll().size();
        Database.getInstance().insert(new Cv("",2001,3,"humayzehraozer@gmail.com","student","home","address","05533000958","github.com",new HashMap<>(),new HashMap<>(),new HashMap<>(),new ArrayList<>()));
        int newlength=Database.getInstance().getAll().size();
        assertEquals(newlength, oldlength + 1);
        Database.getInstance().close();

    }

    @Test
    public void testDelete() throws SQLException, ClassNotFoundException{
        Database.getInstance().open();
        Cv cv= new Cv("hümay",2001,3,"humayzehraozer@gmail.com","student","home","address","05533000958","github.com",new HashMap<>(),new HashMap<>(),new HashMap<>(),new ArrayList<>());
        Database.getInstance().insert(cv);
        int oldlength=Database.getInstance().getAll().size();
        Database.getInstance().delete(cv);
        int newlength=Database.getInstance().getAll().size();
        assertEquals(oldlength , newlength+1);
        Database.getInstance().close();
    }
}



