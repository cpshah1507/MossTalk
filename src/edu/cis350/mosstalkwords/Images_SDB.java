package edu.cis350.mosstalkwords;
//test1
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.util.Log;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.BatchPutAttributesRequest;
import com.amazonaws.services.simpledb.model.CreateDomainRequest;
import com.amazonaws.services.simpledb.model.DeleteAttributesRequest;
import com.amazonaws.services.simpledb.model.DeleteDomainRequest;
import com.amazonaws.services.simpledb.model.Item;
import com.amazonaws.services.simpledb.model.PutAttributesRequest;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;
import com.amazonaws.services.simpledb.model.ReplaceableItem;
import com.amazonaws.services.simpledb.model.SelectRequest;



public class Images_SDB {

        private static int index=0;
    private static String myDomain = "mosswords";
    private static AmazonSimpleDB sdb;
    
    public Images_SDB() {
               try {
        sdb = new AmazonSimpleDBClient(new PropertiesCredentials(
                Images_SDB.class.getResourceAsStream("AwsCredentials.properties")));
               }
               catch(Exception e) {
                       System.out.println(e);
               }
    }
    
    public static void main(String[] args) throws Exception {

 
        System.out.println("===========================================");
        System.out.println("Getting Started with Amazon SimpleDB");
        System.out.println("===========================================\n");

        try {

            // List domains
            System.out.println("Listing all domains in your account:\n");
            for (String domainName : sdb.listDomains().getDomainNames()) {
                System.out.println(" " + domainName);
            }
            System.out.println();

            // Put data into a domain
            putData();
            
            //Get 20 image urls displayImages calls returnImages() which returns 20 objects
            
            displayImages();
            
        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which means your request made it "
                    + "to Amazon SimpleDB, but was rejected with an error response for some reason.");
            System.out.println("Error Message: " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code: " + ase.getErrorCode());
            System.out.println("Error Type: " + ase.getErrorType());
            System.out.println("Request ID: " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with SimpleDB, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
    }
    
    public List<Image> returnAllImages(){
    	List<Image> imgList = new ArrayList<Image>();
        String selectExpression = "select * from `" + myDomain + "`";
        try{
            SelectRequest selectRequest = new SelectRequest(selectExpression);
            List<Item> items = sdb.select(selectRequest).getItems();
            
            System.out.println("Items size is:" + items.size());
            
            Item item;
            String[] arg= new String[7];
            for(int i=0;i<items.size(); i++){
	        	item=items.get(i);
	            arg[0]=item.getName();
	            int j=1;
	            for (Attribute attribute : item.getAttributes()) {
	        		arg[j]=attribute.getValue();
	    			j++;         
	            }
	            
	            imgList.add(new Image(arg[0], arg[1], arg[2], arg[3], arg[4], arg[5], arg[6]));
	        }
            
        }
        catch(Exception e)
        {
                Log.d("exception",e.toString());
                
        }
        return imgList;
    }
    
    public List<Image> returnImages(String category) {
            List<Image> img= new ArrayList<Image>();
        String selectExpression = "select * from `" + myDomain + "` where Category = '"+category+"' ";
        System.out.println("Selecting: " + selectExpression + "\n");
        try{
        SelectRequest selectRequest = new SelectRequest(selectExpression);
        String[] arg= new String[7];       

        List<Item> items = sdb.select(selectRequest).getItems();
        // To randomize images
        Collections.shuffle(items);
        
   
        Item item;
        for(int i=0; i<20 && i<items.size(); i++) {
                item=items.get(i);
                 arg[0]=item.getName();
         int j=1;
         for (Attribute attribute : item.getAttributes()) {
        		 arg[j]=attribute.getValue();
                 j++;    
         }
         img.add(new Image(arg[0], arg[1], arg[2], arg[3], arg[4], arg[5], arg[6]));
         }
        }
        catch(Exception e)
        {
                Log.d("exception",e.toString());
                
        }
        return img;
    }
    

    /*Just to test returnImages()
* We are printing the urls of the images
* We have getters for all the attributes of the images.
*/
    public static void displayImages() {
            List<Image> img=new Images_SDB().returnImages("Living");
            for(int i=0;i<img.size();i++) {
                    System.out.println(img.get(i).getUrl());
            }
    }
    /**
* Creates an array of SimpleDB ReplaceableItems populated with sample data.
*
* @return An array of sample item data.
*/
    

    
    
    /*Puts data into the domain
* this calls createData which puts batches of 25 items into the domain
*/
    
    public static void putData() {
            try {
             while(index!=-1) {
             System.out.println("Putting data into " + myDomain + " domain.\n");
                 sdb.batchPutAttributes(new BatchPutAttributesRequest(myDomain, createData()));
         }
         System.out.println("Ended putting data");
            }
            catch(Exception e) {
                    System.out.println(e);
            }
    }
     
    /*Creates Data for the domain
* calls 2 functions addLivingData and addNonLivingData
* Just for 1st Iteration
*/
    private static List<ReplaceableItem> createData() {
                String word,category;
                List<ReplaceableItem> sampleData=new ArrayList<ReplaceableItem>();
                    
                if(index==0) {
                    sampleData=addLivingData(sampleData,"Ant",8,7);
                    sampleData=addLivingData(sampleData,"Apple",10,10);
                    sampleData=addLivingData(sampleData,"Asparagus",5,4);
                    sampleData=addLivingData(sampleData,"Avocado",4,3);
                    sampleData=addLivingData(sampleData,"Bean",7,8);
                    sampleData=addLivingData(sampleData,"Bear",9,9);
                    sampleData=addLivingData(sampleData,"Bird",9,10);
                    sampleData=addLivingData(sampleData,"Broccoli",5,6);
                    sampleData=addLivingData(sampleData,"Carrot",8,10);
                    sampleData=addLivingData(sampleData,"Cat",10,10);
                    sampleData=addLivingData(sampleData,"Cauliflower",7,6);
                    sampleData=addLivingData(sampleData,"Chipmunk",6,8);
                    sampleData=addLivingData(sampleData,"Cockroach",7,9);
                    sampleData=addLivingData(sampleData,"Corn",8,6);
                    sampleData=addLivingData(sampleData,"Cow",9,10);
                    sampleData=addLivingData(sampleData,"Crocodile",8,8);
                    sampleData=addLivingData(sampleData,"Dog",9,10);
                    sampleData=addLivingData(sampleData,"Eagle",7,8);
                    sampleData=addLivingData(sampleData,"Elephant",9,9);
                    sampleData=addLivingData(sampleData,"Fish",8,9);
                    sampleData=addLivingData(sampleData,"Flower",9,9);
                    sampleData=addLivingData(sampleData,"Frog",7,8);
                    sampleData=addLivingData(sampleData,"Giraffe",6,7);
                    sampleData=addLivingData(sampleData,"Iguana",5,6);
                    sampleData=addLivingData(sampleData,"Lizard",5,6);
                    index=25;
                    return sampleData;
                }
                
                else if (index==25) {
                sampleData=addLivingData(sampleData,"Snake",7,8);
                sampleData=addLivingData(sampleData,"Tiger",8,8);
                    sampleData=addLivingData(sampleData,"Tomato",8,9);
                    sampleData=addLivingData(sampleData,"Toucan",3,4);
                    sampleData=addLivingData(sampleData,"Zebra",5,7);
                    sampleData=addNonLivingData(sampleData,"Banana",5,7);
                    sampleData=addNonLivingData(sampleData,"Basketball",5,7);
                    sampleData=addNonLivingData(sampleData,"Bed",5,7);
                    sampleData=addNonLivingData(sampleData,"Bicycle",5,7);
                    sampleData=addNonLivingData(sampleData,"Book",5,7);
                    sampleData=addNonLivingData(sampleData,"Boomerang",5,7);
                    sampleData=addNonLivingData(sampleData,"Box",5,7);
                    sampleData=addNonLivingData(sampleData,"Bubble",5,7);
                    sampleData=addNonLivingData(sampleData,"Calculator",5,7);
                    sampleData=addNonLivingData(sampleData,"Camera",5,7);
                    sampleData=addNonLivingData(sampleData,"Chair",5,7);
                    sampleData=addNonLivingData(sampleData,"Cinnamon",5,7);
                    sampleData=addNonLivingData(sampleData,"Clock",5,7);
                    sampleData=addNonLivingData(sampleData,"Computer",5,7);
                    sampleData=addNonLivingData(sampleData,"Cup",5,7);
                    sampleData=addNonLivingData(sampleData,"Darts",5,7);
                    index=50;
                    
                    return sampleData;
                }
                else if (index==50) {
                        sampleData=addNonLivingData(sampleData,"Door",5,7);
                        sampleData=addNonLivingData(sampleData,"Elevator",5,7);
                        sampleData=addNonLivingData(sampleData,"Football",5,7);
                        sampleData=addNonLivingData(sampleData,"Freezer",5,7);
                        sampleData=addNonLivingData(sampleData,"Glove",5,7);
                        sampleData=addNonLivingData(sampleData,"Hat",5,7);
                        sampleData=addNonLivingData(sampleData,"House",5,7);
                        sampleData=addNonLivingData(sampleData,"Lamp",5,7);
                        sampleData=addNonLivingData(sampleData,"Money",5,7);
                        sampleData=addNonLivingData(sampleData,"Plate",5,7);
                        sampleData=addNonLivingData(sampleData,"Shirt",5,7);
                        sampleData=addNonLivingData(sampleData,"Shoe",5,7);
                        sampleData=addNonLivingData(sampleData,"Spoon",5,7);
                        sampleData=addNonLivingData(sampleData,"Table",5,7);
                        
                        index=-1;
                        return sampleData;
                }
                return sampleData;
    }

    //Adds Living Data
    private static List<ReplaceableItem> addLivingData(List<ReplaceableItem> sampleData, String word, int f, int i) {

            System.out.println("putting word: "+word);
            
            sampleData.add(new ReplaceableItem(word).withAttributes(
                                 new ReplaceableAttribute("Category", "Living", true),
         new ReplaceableAttribute("Length", wordLength(word), true),
         new ReplaceableAttribute("frequency", Integer.toString(f), true),
         new ReplaceableAttribute("Imageability", Integer.toString(i), true),
         new ReplaceableAttribute("url", "https://s3.amazonaws.com/mosswords/images/living/"+word.toLowerCase()+".jpg", true)));

            return sampleData;
    }
    
    //Adds Non living Data
    private static List<ReplaceableItem> addNonLivingData(List<ReplaceableItem> sampleData, String word, int f, int i) {
            
            System.out.println("putting word: "+word);
            sampleData.add(new ReplaceableItem(word).withAttributes(
                                 new ReplaceableAttribute("Category", "NonLiving", true),
         new ReplaceableAttribute("Length", wordLength(word), true),
         new ReplaceableAttribute("frequency", Integer.toString(f), true),
         new ReplaceableAttribute("Imageability", Integer.toString(i), true),
         new ReplaceableAttribute("url", "https://s3.amazonaws.com/mosswords/images/nonliving/"+word.toLowerCase()+".jpg", true)));

            return sampleData;
    }
    
    public static String wordLength(String word) {
            return Integer.toString(word.length());
    }

    public List<String> getAllCategories(){
    	List<String> catogoriesList = new ArrayList<String>();
        String selectExpression = "select * from `" + myDomain + "`";
        try{
            SelectRequest selectRequest = new SelectRequest(selectExpression);
            List<Item> items = sdb.select(selectRequest).getItems();
            
            System.out.println("Items size is:" + items.size());
            
            Item item;
            for(int i=0;i<items.size(); i++){
	        	item=items.get(i);
	            
	        	String itemCategory = item.getAttributes().get(0).getValue();
	        	int match = 0;
	        	for(String str: catogoriesList){
	        		if(str.equals(itemCategory))
	        		{
	        			match = 1;
	        			break;
	        		}
	        	}
	        	if(match == 0)
	        		catogoriesList.add(itemCategory);
            }
        }catch(Exception e)
        {
            Log.d("exception",e.toString());
        }
        return catogoriesList;
    }
}