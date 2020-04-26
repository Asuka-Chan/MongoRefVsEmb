import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;


public class ReferenceLargeDS {
    public static void main(String[] args) {

        MongoClientURI uri = new MongoClientURI(
                "mongodb+srv://nelli:nellikai@dsproject4cluster-raad1.mongodb.net/test?retryWrites=true&w=majority");

        MongoClient mongoClient = new MongoClient(uri);
        MongoDatabase database = mongoClient.getDatabase("nosqlDB");
        MongoCollection<Document> collection = database.getCollection("referenceLargeStudentTest");
        MongoCollection<Document> collectionCourse = database.getCollection("referenceLargeCourseTest");

        long start = System.currentTimeMillis();
        collectionCourse.insertOne(new Document().append("courseId", 10).append("courseName", "TestCourse 1").append("instructor", "Dr. ABC"));
        ObjectId obectId = null;
        for (Document document : collectionCourse.find()) {
            obectId = document.getObjectId("_id");
        }
        for (int i = 0; i < 100000; i++) {
            collection.insertOne(new Document().append("studentID", i).append("name", "TestName").append("courseId", obectId).append("grade", "A"));
        }
        System.out.println("Total time taken to insert: " + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        obectId = null;
        for (Document document : collectionCourse.find()) {
            obectId = document.getObjectId("_id");
        }
        BasicDBObject courseId = new BasicDBObject();
        courseId.put("courseId", obectId);
        collection.find(courseId);
        System.out.println("Total time taken to find all documents: " + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        BasicDBObject newDocument = new BasicDBObject();
        newDocument.append("$set", new BasicDBObject().append("grade", "B"));
        BasicDBObject searchQuery = new BasicDBObject().append("studentID", new BasicDBObject("$mod", new int[]{2,0}));
        collection.updateMany(searchQuery, newDocument);
        System.out.println("Total time taken to update even documents: " + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        newDocument = new BasicDBObject();
        newDocument.append("$set", new BasicDBObject().append("instructor", "Mr. John Doe"));
        searchQuery = new BasicDBObject();
        collectionCourse.updateMany(searchQuery, newDocument);
        System.out.println("Total time taken to update all documents: " + (System.currentTimeMillis() - start));



        start = System.currentTimeMillis();
        collectionCourse.deleteMany(new Document());
        collection.deleteMany(new Document());
        System.out.println("Total time taken to delete all documents: " + (System.currentTimeMillis() - start));


    }


}
