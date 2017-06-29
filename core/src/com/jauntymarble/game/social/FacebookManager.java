package com.jauntymarble.game.social;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;

import de.tomgrill.gdxfacebook.core.GDXFacebook;
import de.tomgrill.gdxfacebook.core.GDXFacebookCallback;
import de.tomgrill.gdxfacebook.core.GDXFacebookError;
import de.tomgrill.gdxfacebook.core.GDXFacebookGameRequest;
import de.tomgrill.gdxfacebook.core.GDXFacebookGraphRequest;
import de.tomgrill.gdxfacebook.core.GDXFacebookSystem;
import de.tomgrill.gdxfacebook.core.GameRequestResult;
import de.tomgrill.gdxfacebook.core.JsonResult;
import de.tomgrill.gdxfacebook.core.SignInMode;
import de.tomgrill.gdxfacebook.core.SignInResult;
import ownLib.Own;

public class FacebookManager {
    private static FacebookManager facebookManager;
    // singleton instance
    private String TAG = "FacebookManager";
    private String FB_INVITE_MESSAGE = "Hey guys!! Do try this game, its awesome!!";
    private String FB_WALL_LINK = "http://www.caesiumgames.com/2017/02/blog-post.html";
    private String FB_WALL_CAPTION = "Jaunty Marble";

    private Preferences prefs;
    private GDXFacebook gdxFacebook = null;
    private Array<String> permissionsRead = new Array<String>();
    private Array<String> permissionsPublish = new Array<String>();
    private Array<String> REQUEST_QUEUE = new Array<>();

    public static FacebookManager getInstance() {
        if (facebookManager == null) {
            //creating facebook manager
            facebookManager = new FacebookManager();
        }

        return facebookManager;
    }

    public void postInvitationToFriends() {
        GDXFacebookGameRequest request = new GDXFacebookGameRequest();
        request.setMessage(FB_INVITE_MESSAGE);

        gdxFacebook.showGameRequest(request, new GDXFacebookCallback<GameRequestResult>() {
            @Override
            public void onSuccess(GameRequestResult result) {
                Gdx.app.debug(TAG, "ReqId: " + result.getRequestId());
                Gdx.app.debug(TAG, "Invited Friends: " + result.getRecipients().toString());
            }

            @Override
            public void onError(GDXFacebookError error) {
                Gdx.app.error(TAG, error.getErrorMessage());
            }

            @Override
            public void onFail(Throwable t) {
                // does not happen when doing game requests
            }

            @Override
            public void onCancel() {
                Gdx.app.error(TAG, "User canceled request");
            }
        });
    }

    public void postMessageOnWall() {
        GDXFacebookGraphRequest request = new GDXFacebookGraphRequest().setNode("me/feed").useCurrentAccessToken();
        request.setMethod(Net.HttpMethods.POST);
        // facebook doesn't allow putting message on behalf of users
//        request.putField("message", FB_WALL_MESSAGE);

        // you can put link info and caption for the wall post
        request.putField("link", FB_WALL_LINK);
        request.putField("caption", FB_WALL_CAPTION);

        // fist time post message, may be the user is not logged in so make an entry into the request queue
        if (gdxFacebook == null) {
            REQUEST_QUEUE.add("MESSAGE_ON_WALL");
            loginWithPublishPermissions();
            // return from here because this function will be called again from loginWithPublishPermissions() as callback after login;
            return;
        }

        // post on the wall and wait for the response
        gdxFacebook.graph(request, new GDXFacebookCallback<JsonResult>() {
            @Override
            public void onFail(Throwable t) {
                Gdx.app.debug(TAG, "Exception occured while trying to post to user wall.");
            }

            @Override
            public void onCancel() {
                Own.log("Post to user wall has been cancelled.");
            }

            @Override
            public void onSuccess(JsonResult result) {
                Gdx.app.debug(TAG, "Posted to user wall successful." + result.getJsonValue().prettyPrint(JsonWriter.OutputType.json, 1));
            }

            @Override
            public void onError(GDXFacebookError error) {
                Gdx.app.error(TAG, "Error: " + error.getErrorMessage());
            }
        });
    }

    public void loginWithPublishPermissions() {
        permissionsRead.add("email");
        permissionsRead.add("public_profile");
        permissionsRead.add("user_friends");

        // this is the permission which facebook needs to approve for posting on the wall
        permissionsPublish.add("publish_actions");

        FacebookAppConfig config = new FacebookAppConfig();
        gdxFacebook = GDXFacebookSystem.install(config);

        // attempt signing in
        gdxFacebook.signIn(SignInMode.PUBLISH, permissionsPublish, new GDXFacebookCallback<SignInResult>() {
            @Override
            public void onSuccess(SignInResult result) {
                // on successful singing in process the request queue
                for (int i = 0; i < REQUEST_QUEUE.size; i++) {
                    switch (REQUEST_QUEUE.get(i)) {
                        case "MESSAGE_ON_WALL":
                            postMessageOnWall();
                            break;
                        default:
                    }

                }
                Gdx.app.debug(TAG, "User logged in successfully.");
                gainMoreUserInfo();
            }

            @Override
            public void onCancel() {
                Gdx.app.debug(TAG, "User canceled login process");
            }

            @Override
            public void onFail(Throwable t) {
                Gdx.app.debug(TAG, "Technical error occured:");
                logout();
            }

            @Override
            public void onError(GDXFacebookError error) {
                Gdx.app.error(TAG, error.getErrorMessage());
                logout();
            }
        });
    }

    private void gainMoreUserInfo() {
        GDXFacebookGraphRequest request = new GDXFacebookGraphRequest().setNode("me").useCurrentAccessToken();

        gdxFacebook.newGraphRequest(request, new GDXFacebookCallback<JsonResult>() {
            @Override
            public void onSuccess(JsonResult result) {
                JsonValue root = result.getJsonValue();

                String fbNickname = root.getString("name");
                String fbIdForThisApp = root.getString("id");

                Gdx.app.debug(TAG, "Username " + fbNickname + ", user unique ID is: " + fbIdForThisApp);
            }

            @Override
            public void onCancel() {
                Gdx.app.debug(TAG, "Graph Reqest: Request cancelled. Reason unknown.");
                logout();
            }

            @Override
            public void onFail(Throwable t) {
                Gdx.app.debug(TAG, "Graph Reqest: Failed with exception.");
                logout();
            }

            @Override
            public void onError(GDXFacebookError error) {
                Gdx.app.debug(TAG, "Graph Request: Error. Something went wrong with the access token.");
                logout();
            }
        });

    }

    private void logout() {
        gdxFacebook.signOut();
    }
}
