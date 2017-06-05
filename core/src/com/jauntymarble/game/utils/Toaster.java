package com.jauntymarble.game.utils;

import com.jauntymarble.game.GameController;

import de.tomgrill.gdxdialogs.core.dialogs.GDXButtonDialog;
import de.tomgrill.gdxdialogs.core.dialogs.GDXProgressDialog;
import de.tomgrill.gdxdialogs.core.dialogs.GDXTextPrompt;
import de.tomgrill.gdxdialogs.core.listener.ButtonClickListener;
import de.tomgrill.gdxdialogs.core.listener.TextPromptListener;
import ownLib.Own;

/**
 * Created by ravi on 04.06.17.
 */

public class Toaster {

    private static Toaster toaster = null;

    public static Toaster getToaster() {
        if (toaster == null) {
            toaster = new Toaster();
        }

        return toaster;
    }


    public void showTextInputDialog(String title, String message) {
        GDXTextPrompt textPrompt = GameController.gdxDialogs.newDialog(GDXTextPrompt.class);

        textPrompt.setTitle(title);
        textPrompt.setMessage(message);

        textPrompt.setCancelButtonLabel("Cancel");
        textPrompt.setConfirmButtonLabel("OK");

        textPrompt.setTextPromptListener(new TextPromptListener() {
            @Override
            public void confirm(String text) {
                Own.log("Your name is: " + text);
            }

            @Override
            public void cancel() {
                Own.log("Common, tell me your name please :)");
            }
        });

        textPrompt.build().show();
    }

    private void showANewProgressDialog() {
        GDXProgressDialog progressDialog = GameController.gdxDialogs.newDialog(GDXProgressDialog.class);

        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Loading new level from server...");

//        showProgressUntilTime = TimeUtils.millis() + 5000;
        progressDialog.build().show();

    }


    private void showANewTrippleButtonDialog() {
        GDXButtonDialog bDialog = GameController.gdxDialogs.newDialog(GDXButtonDialog.class);

        bDialog.setTitle("Buy a item");
        bDialog.setMessage("Do you want to buy the mozarella?");

        bDialog.setClickListener(new ButtonClickListener() {

            @Override
            public void click(int button) {
//                buttonClickedFontActor.setText("Last clicked button: " + button);
            }
        });

        bDialog.addButton("Yes, nomnom!");
        bDialog.addButton("No");
        bDialog.addButton("Later");

        bDialog.build().show();

    }

    private void showANewDoubleButtonDialog() {
        GDXButtonDialog bDialog = GameController.gdxDialogs.newDialog(GDXButtonDialog.class);

        bDialog.setTitle("Am i right?");
        bDialog.setMessage("Your age is 29!");

        bDialog.setClickListener(new ButtonClickListener() {

            @Override
            public void click(int button) {

            }
        });

        bDialog.addButton("Correct!");
        bDialog.addButton("Wrong");

        bDialog.build().show();

    }

    public void showSingleButtonDialog(String title, String message) {
        GDXButtonDialog bDialog = GameController.gdxDialogs.newDialog(GDXButtonDialog.class);

        bDialog.setTitle(title);
        bDialog.setMessage(message);

        bDialog.setClickListener(new ButtonClickListener() {

            @Override
            public void click(int button) {

            }
        });

        bDialog.addButton("OK");
        bDialog.build().show();
    }
}

