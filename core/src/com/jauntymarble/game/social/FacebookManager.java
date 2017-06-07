package com.jauntymarble.game.social;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.jauntymarble.game.utils.Toaster;

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
    // singleton instance
    private static FacebookManager facebookManager;
    private String FB_INVITE_MESSAGE = "Hey guys!! Do try this game, its awesome!!";
    private String FB_WALL_MESSAGE = "Hey guys!! Do try this game, its awesome!!";
    private String FB_WALL_LINK = "http://www.caesiumgames.com/2017/02/blog-post.html";
    private String FB_WALL_CAPTION = "Jaunty Marble";
    private Preferences prefs;
    private GDXFacebook gdxFacebook = null;
    private Array<String> permissionsRead = new Array<String>();
    private Array<String> permissionsPublish = new Array<String>();
    private Array<String> REQUEST_QUEUE = new Array<>();

    private FacebookManager() {
    }

    public static FacebookManager getInstance() {
        if (facebookManager == null) {
            Own.log("Creating facebook manager");
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
                Own.log("ReqId: " + result.getRequestId());
                Own.log("Invited Friends: " + result.getRecipients().toString());
            }

            @Override
            public void onError(GDXFacebookError error) {
                Own.log(error.getErrorMessage());
                Toaster.getToaster().showSingleButtonDialog("Error ", error.getErrorMessage());
            }

            @Override
            public void onFail(Throwable t) {
                // does not happen when doing game requests
            }

            @Override
            public void onCancel() {
                Own.log("User canceled request");
            }
        });
    }

    public void postMessageOnWall() {
        GDXFacebookGraphRequest request = new GDXFacebookGraphRequest().setNode("me/feed").useCurrentAccessToken();
        request.setMethod(Net.HttpMethods.POST);
//        request.putField("message", FB_WALL_MESSAGE);
        request.putField("link", FB_WALL_LINK);
        request.putField("caption", FB_WALL_CAPTION);

        if (gdxFacebook == null) {
            REQUEST_QUEUE.add("MESSAGE_ON_WALL");
            loginWithPublishPermissions();
            return;
        }
        gdxFacebook.graph(request, new GDXFacebookCallback<JsonResult>() {
            @Override
            public void onFail(Throwable t) {
                Own.log("Exception occured while trying to post to user wall.");
                t.printStackTrace();
            }

            @Override
            public void onCancel() {
                Own.log("Post to user wall has been cancelled.");
            }

            @Override
            public void onSuccess(JsonResult result) {
                Own.log("Posted to user wall successful." + result.getJsonValue().prettyPrint(JsonWriter.OutputType.json, 1));
            }

            @Override
            public void onError(GDXFacebookError error) {
                Own.log("Error: " + error.getErrorMessage());
                Toaster.getToaster().showSingleButtonDialog("Error ", error.getErrorMessage());
            }
        });
    }

    public void loginWithPublishPermissions() {
        prefs = Gdx.app.getPreferences("gdx-facebook-app-data.txt");

        permissionsRead.add("email");
        permissionsRead.add("public_profile");
        permissionsRead.add("user_friends");

        permissionsPublish.add("publish_actions");

        FacebookAppConfig config = new FacebookAppConfig();

        gdxFacebook = GDXFacebookSystem.install(config);
        gdxFacebook.signIn(SignInMode.PUBLISH, permissionsPublish, new GDXFacebookCallback<SignInResult>() {
            @Override
            public void onSuccess(SignInResult result) {
                for (int i = 0; i < REQUEST_QUEUE.size; i++) {
                    switch (REQUEST_QUEUE.get(i)) {
                        case "MESSAGE_ON_WALL":
                            postMessageOnWall();
                            break;
                        default:
                    }

                }
                Own.log("SIGN IN (publish permissions): User logged in successfully.");
                gainMoreUserInfo();
            }

            @Override
            public void onCancel() {
                Own.log("SIGN IN (publish permissions): User canceled login process");
            }

            @Override
            public void onFail(Throwable t) {
                Own.log("SIGN IN (publish permissions): Technical error occured:");
                logout();
                t.printStackTrace();
            }

            @Override
            public void onError(GDXFacebookError error) {
                Toaster.getToaster().showSingleButtonDialog("Error ", error.getErrorMessage());
                Own.log("SIGN IN (publish permissions): Error login: " + error.getErrorMessage());
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

                Own.log("Hello " + fbNickname + ", your unique ID is: " + fbIdForThisApp);
            }

            @Override
            public void onCancel() {
                Own.log("Graph Reqest: Request cancelled. Reason unknown.");
                logout();
            }

            @Override
            public void onFail(Throwable t) {
                Own.log("Graph Reqest: Failed with exception.");
                logout();
                t.printStackTrace();
            }

            @Override
            public void onError(GDXFacebookError error) {
                Own.log("Graph Request: Error. Something went wrong with the access token.");
                logout();
            }
        });

    }

    private void logout() {
        gdxFacebook.signOut();
    }
}
