import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;


public class EmbeddedSmallDS {
    public static void main(String[] args) {

        MongoClientURI uri = new MongoClientURI(
                "mongodb+srv://nelli:nellikai@dsproject4cluster-raad1.mongodb.net/test?retryWrites=true&w=majority");

        MongoClient mongoClient = new MongoClient(uri);
        MongoDatabase database = mongoClient.getDatabase("nosqlDB");
        MongoCollection<Document> collection = database.getCollection("embeddedSmallTest");

        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            collection.insertOne(new Document().append("studentID", i).append("name", "TestName").append("courses", new Document().append("courseId", 10).append("courseName", "TestCourse 1").append("instructor", "Dr. ABC").append("grade", "A")));
        }
        System.out.println("Total time taken to insert: " + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        BasicDBObject courseId = new BasicDBObject();
        courseId.put("courses.courseId", 10);
        collection.find(courseId);
        System.out.println("Total time taken to find all documents: " + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        BasicDBObject newDocument = new BasicDBObject();
        newDocument.append("$set", new BasicDBObject().append("courses.grade", "B"));
        BasicDBObject searchQuery = new BasicDBObject().append("studentID", new BasicDBObject("$mod", new int[]{2,0}));
        collection.updateMany(searchQuery, newDocument);
        System.out.println("Total time taken to update even documents: " + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        newDocument = new BasicDBObject();
        newDocument.append("$set", new BasicDBObject().append("courses.instructor", "Mr. John Doe"));
        searchQuery = new BasicDBObject();
        collection.updateMany(searchQuery, newDocument);
        System.out.println("Total time taken to update all documents: " + (System.currentTimeMillis() - start));


        start = System.currentTimeMillis();
        collection.deleteMany(new Document());
        System.out.println("Total time taken to delete all documents: " + (System.currentTimeMillis() - start));


    }


}
