package apiTest;

import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import tweets.TweetsUser;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TweetsTest {
    protected TweetsUser tweetsUser;
    protected Long tweetId;
    protected List<Long> tweetIdList= new ArrayList<Long>();

    @BeforeClass
    public void setUp(){
        this.tweetsUser = new TweetsUser();
    }
    @Test
    public void testGetUserTimeLine(){
        tweetsUser.getUserTimeLine();
    }

   @Test
    public void testCreateTweet()throws Exception{
        String tweet = "hello it's me, i am in jersey"+ UUID.randomUUID();
        Response response = tweetsUser.createTweet(tweet);
        response.then().statusCode(HttpStatus.SC_OK);
        Assert.assertEquals(200,response.getStatusCode());
        this.tweetId = response.path("id");
        this.tweetIdList.add(this.tweetId);
    }
   @Test
    public void cantNotTweetWithSameTweetTwiceInRow()throws Exception{
        String tweet = "hello i am in jersey with samir and youcef" + UUID.randomUUID();
        Response response = tweetsUser.createTweet(tweet);
        response.then().statusCode(HttpStatus.SC_OK);
        Assert.assertEquals(200,response.getStatusCode());
       this.tweetIdList.add(this.tweetId);

        response = tweetsUser.createTweet(tweet);
        response.then().statusCode(HttpStatus.SC_FORBIDDEN);
        Assert.assertEquals(403,response.getStatusCode());

    }
   @Test(dependsOnMethods = {"testCreateTweet"},enabled = false)
    public void testUserCanDeleteTweet(){
        Response response = this.tweetsUser.deleteTweet(this.tweetId);
        response.then().statusCode(HttpStatus.SC_OK);
        Assert.assertEquals(200,response.getStatusCode());
    }
    @AfterClass
    public void cleanUp(){
        if(this.tweetIdList!= null){
            for(Long tweetId: tweetIdList){
                this.tweetsUser.deleteTweet(tweetId);
            }
        }
    }
}
