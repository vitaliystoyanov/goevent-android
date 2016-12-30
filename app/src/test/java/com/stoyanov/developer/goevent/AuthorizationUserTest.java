package com.stoyanov.developer.goevent;

import android.os.Build;

import com.google.gson.JsonObject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.M)
public class AuthorizationUserTest implements MappingAPI {

/*    @Test
    public void loginUserTest() throws Exception {
        JsonObject json = new JsonObject();
        json.addProperty("username", "melka");
        json.addProperty("password", "1234");
        System.out.println("loginUserTest: Body: " + Ion.with(RuntimeEnvironment.application)
                .load(LOGIN_ENDPOINT)
                .setJsonObjectBody(json)
                .asString()
                .getItem());
    }

    @Test
    public void userEventsTest() throws Exception {
        String response = Ion.with(RuntimeEnvironment.application)
                .load(USER_EVENTS_ENDPOINT)
                .asString()
                .getItem();
        System.out.println("userEventsTest: Body: " + response);
        assertThat(response, allOf(containsString("_id"), containsString("eventCreator")));
    }

    @Test
    public void logoutUserTest() throws Exception {
        String response = Ion.with(RuntimeEnvironment.application)
                .load(LOGOUT_ENDPOINT)
                .setStringBody("")
                .asString()
                .getItem();
        System.out.println("logoutUserTest: Body: " + response);
        assertThat(response, allOf(containsString("Success"), containsString("logout")));
    }*/
}
