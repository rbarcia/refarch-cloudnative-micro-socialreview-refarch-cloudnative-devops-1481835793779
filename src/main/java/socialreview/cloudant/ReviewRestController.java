package socialreview.cloudant;

//import org.ektorp.CouchDbConnector;
//import org.ektorp.support.CouchDbRepositorySupport;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.cloudant.client.api.model.IndexField;
import com.cloudant.client.api.model.IndexField.SortOrder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/review")
public class ReviewRestController {

  @Autowired
  private Database db;

  @RequestMapping(method=RequestMethod.GET)
  public @ResponseBody List<Review> getAll(@RequestParam(value="itemId", required=false) String itemId) {

    //db.save(new Review(true));
    //Review doc = db.find(Review.class,"111");
    //return doc.toString();

    // Get all documents from socialreviewdb
    List<Review> allDocs = null;
    try
    {
      if(itemId == null)
      {
        allDocs = db.getAllDocsRequestBuilder().includeDocs(true).build().getResponse()
            .getDocsAs(Review.class);
      }else{

          // create Index
          // Here is create a design doc named designdoc
          // A view named querybyitemIdView
          // and an index named itemId
		      db.createIndex("querybyitemIdView","designdoc","json",
				      new IndexField[]{
                	new IndexField("itemId",SortOrder.asc)
              }
		      );
          System.out.println("Successfully created index");
          allDocs = db.findByIndex("{\"itemId\" :\"" + itemId + "\"}", Review.class);
      }


    } catch (Exception e) {
				System.out.println("Exception thrown : " + e.getMessage());
		}


    return allDocs;
  }


}