package ownLib.box2d;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.jauntymarble.game.userdata.CircleUserData;
import com.jauntymarble.game.userdata.JsonUserData;

import ownLib.Own;

/**
 * Created by ravi on 17.09.16.
 */
public class GUI {
    public void drawCircleWithRotation(Batch batch, String textRegionID, Body body) {
        float radius = ((CircleUserData) body.getUserData()).getRadius();
        batch.draw(new TextureRegion(Own.assets.getTexture(textRegionID)),
                body.getPosition().x - radius, // x
                body.getPosition().y - radius, // y
                radius, // rotationCenterX
                radius, // rotationCenterY
                radius * 2, // width
                radius * 2, // height
                1, // scaleX
                1, // scaleY
                body.getAngle() * MathUtils.radiansToDegrees); // rotation
    }

    public void drawRectangleWithRotation(SpriteBatch batch, String textRegionID, Body body, float width, float height) {
        batch.draw(new TextureRegion(Own.assets.getTexture(textRegionID)),
                body.getPosition().x - width, // x
                body.getPosition().y - height, // y
                width, // rotationCenterX
                height, // rotationCenterY
                width * 2, // width
                height * 2, // height
                1, // scaleX
                1, // scaleY
                body.getAngle() * MathUtils.radiansToDegrees); // rotation
    }

    public void drawJsonBody(SpriteBatch batch, String textRegionID, Body body) {
        JsonUserData bottleUserData = (JsonUserData) body.getUserData();
        batch.draw(new TextureRegion(Own.assets.getTexture(textRegionID)),
                body.getPosition().x, // x
                body.getPosition().y, // y
                0, // rotationCenterX
                0, // rotationCenterY
                bottleUserData.getWidth(), // width
                bottleUserData.getHeight(), // height
                1, // scaleX
                1, // scaleY
                body.getAngle() * MathUtils.radiansToDegrees); // rotation
    }

    public void applyForceFromSource(float strength, Body body, Vector3 forceOrigin, boolean isPushForce) {
        float x2 = forceOrigin.x;
        float y2 = forceOrigin.y;
        double x1 = body.getPosition().x;
        double y1 = body.getPosition().y;

        float angle = (float) Math.atan2(y2 - y1, x2 - x1);

        Vector2 force = new Vector2();
        float sin = (float) Math.sin(angle);
        float cos = (float) Math.cos(angle);
        if (isPushForce) strength *= -1;
        force.x = strength * cos;
        force.y = strength * sin;
        body.applyForceToCenter(force, true);
    }
}
