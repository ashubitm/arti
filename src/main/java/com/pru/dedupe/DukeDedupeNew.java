package com.pru.dedupe;



import no.priv.garshol.duke.ConfigLoader;
import no.priv.garshol.duke.Configuration;
import no.priv.garshol.duke.Processor;
import no.priv.garshol.duke.Record;
import no.priv.garshol.duke.datasources.JsonDataSource;
import no.priv.garshol.duke.matchers.MatchListener;
import no.priv.garshol.duke.matchers.PrintMatchListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

//controller for duke; gets matched records
public final class DukeDedupeNew {	
    public static void performDedupe(String config_path, String mode) throws Exception {

    	System.out.println("config_path:"+config_path+"\n"+"mode:"+mode);
        Configuration config = ConfigLoader.load(config_path);
        
        //TODO Build Record pass parameters from flink
        
        //CSV tested 
        //TODO dedupe right now call being made for existing 2 files - ultimately pass this stream and dedupe from destination 
     //   Record r = TestUtils.makeRecord("ID", "1", "NAME", "J.RandomHacker","COMPANY","Main");
        JsonDataSource source = new JsonDataSource();
   //     Record r = TestUtils.makeRecord("{\"FIRSTNAME\":\"aaaaa\",\"LASTNAME\" : \"bbbbb\"}");
        Record r = source.getRecordsFromString("{\"FIRSTNAME\":\"aaaaa\",\"LASTNAME\" : \"bbbbb\"}").next();

// 	    System.out.println("############# Test single record##############"+r.getValue("F1"));

     
        Collection<Record> records = new HashSet();
        
        records.add(r);
        //false means leave existing data
        
        
        Processor proc = new Processor(config,false);

    //    DatashopMatchListener matchListner = new DatashopMatchListener("ID", config.getProperties(), true);
   //     proc.addMatchListener(matchListner);
        
      
        
        PrintMatchListener listener =new PrintMatchListener(true, true, true, false,
                config.getProperties(),
                true);
        proc.addMatchListener(listener);

  //      proc.setThreads(4);
/*        if (mode.equals("link")) proc.link();
        else proc.deduplicate();*/
      //  proc.deduplicate(r);
        proc.deduplicate(records);
 //       proc.deduplicate(r);
 //       proc.link();
        proc.close();
//        return listener.getMatchCount();
      //  return null;
    }
}