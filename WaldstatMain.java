import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.signature.HmacSha1MessageSigner;

public class WaldstatMain {

    public static void main(String[] args) throws Exception {

        OAuthConsumer consumer = new DefaultOAuthConsumer("Consumer Key",
        		"Consumer Secret");
        //consumer.setMessageSigner(new HmacSha1MessageSigner());

        OAuthProvider provider = new DefaultOAuthProvider(
                "http://api.waldstat.com/oauth/request_token",
                "http://api.waldstat.com/oauth/access_token",
                "http://api.waldstat.com/oauth/authorize");

        System.out.println("Fetching request token...");

        String authUrl = provider.retrieveRequestToken(consumer, OAuth.OUT_OF_BAND);

        System.out.println("Request token: " + consumer.getToken());
        System.out.println("Token secret: " + consumer.getTokenSecret());

        System.out.println("Now visit:\n" + authUrl + "\n... and grant this app authorization");
        System.out.println("Enter the verification code and hit ENTER when you're done:");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String verificationCode = br.readLine();

        System.out.println("Fetching access token...");

        provider.retrieveAccessToken(consumer, verificationCode.trim());

        System.out.println("Access token: " + consumer.getToken());
        System.out.println("Token secret: " + consumer.getTokenSecret());

        URL url = new URL("http://api.waldstat.com/locations/10/checkin");
        HttpURLConnection request = (HttpURLConnection) url.openConnection();

        consumer.sign(request);

        System.out.println("Sending request...");
        request.connect();

        System.out.println("Response: " + request.getResponseCode() + " "
                + request.getResponseMessage());
    }
}
