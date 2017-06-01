package ownLib.box2d;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;
import com.badlogic.gdx.utils.Array;
import com.jauntymarble.game.userdata.CircleUserData;
import com.jauntymarble.game.userdata.RectUserData;
import com.jauntymarble.game.userdata.UserData;

public class Factory {
    private World world = null;
    private BodyDef bodyDef = null;
    private FixtureDef fixtureDef = null;
    private Body body = null;

    public Factory() {
    }

    public World getWorld() {
        return this.world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public Array<Body> createRopeJoint(float linkWidth, float linkLength, float gapBetweenLinks, int linkCount) {
        Array<Body> chainLinks = new Array<Body>();

        Vector2 pos = new Vector2(10, 10);
        for (int i = 0; i < linkCount; i++) {
            chainLinks.add(this.getRectangleBody(BodyDef.BodyType.DynamicBody, new Vector2(pos.x + i, pos.y), 0, linkWidth, linkLength, .01f, .5f, 0f, "link"));
        }

        for (int i = 1; i < linkCount; i++) {
            RopeJointDef ropeJointDef = new RopeJointDef();
            ropeJointDef.bodyA = chainLinks.get(i - 1);
            ropeJointDef.bodyB = chainLinks.get(i);
            ropeJointDef.collideConnected = false;
            ropeJointDef.maxLength = gapBetweenLinks;
            ropeJointDef.localAnchorA.set(0, -1 * linkLength);
            ropeJointDef.localAnchorB.set(0, linkLength);
            world.createJoint(ropeJointDef);
        }

        return chainLinks;
    }

    public Body getCircleBody(BodyType bodyType, Vector2 pos, float angle, float radius, float density, float friction, float restitution, String id) {
        bodyDef = getBodyDef(bodyType, pos, angle);
        body = world.createBody(bodyDef);

        CircleShape circleShape = createCircleShape(radius);
        fixtureDef = getFixtureDef(circleShape, density, friction, restitution);

        CircleUserData circleUserData = new CircleUserData(id, "Circle", circleShape, pos, radius);

        body.createFixture(fixtureDef).setUserData(new UserData(id, "circle", body.getPosition()));
        body.setUserData(circleUserData);
        body.setSleepingAllowed(false);
        return body;
    }

    public FixtureDef getCirclulerFixtureDef(BodyType bodyType, Vector2 pos, float angle, float radius, float density, float friction, float restitution, String id) {
        bodyDef = getBodyDef(bodyType, pos, angle);
        body = world.createBody(bodyDef);

        CircleShape circleShape = createCircleShape(radius);
        fixtureDef = getFixtureDef(circleShape, density, friction, restitution);

        return fixtureDef;
    }

    public Body getPolygonBody(BodyType bodyType, Vector2 pos, float angle, Vector2[] vertices, float density, float friction, float restitution, Object userData) {
        bodyDef = getBodyDef(bodyType, pos, angle);
        body = world.createBody(bodyDef);

        PolygonShape polygonShape = createPolygonShape(vertices);

        fixtureDef = getFixtureDef(polygonShape, density, friction, restitution);
//        polygonShape.dispose();

        body.createFixture(fixtureDef).setUserData(userData);
        body.setSleepingAllowed(true);
        return body;

    }

    public Body getRectangleBody(BodyType bodyType, Vector2 pos, float angle, float width, float height, float density, float friction, float restitution, String id) {
        bodyDef = getBodyDef(bodyType, pos, angle);
        body = world.createBody(bodyDef);

        PolygonShape polygonShape = createRectangleShape(width, height);

        fixtureDef = getFixtureDef(polygonShape, density, friction, restitution);

        RectUserData rectUserData = new RectUserData(id, "Rect", polygonShape, pos, width, height);
        body.createFixture(fixtureDef).setUserData(new UserData(id, "rect", body.getPosition()));
        body.setUserData(rectUserData);
        body.setSleepingAllowed(true);
        fixtureDef.isSensor = false;

        return body;
    }

    public Body getChainBody(BodyType bodyType, Vector2[] vectorPoints, Vector2 pos, float angle, float density, float friction, float restitution, Object userData) {
        bodyDef = getBodyDef(bodyType, pos, angle);
        body = world.createBody(bodyDef);

        ChainShape chainShape = new ChainShape();
        chainShape.createChain(vectorPoints);

        fixtureDef = getFixtureDef(chainShape, density, friction, restitution);
//        chainShape.dispose();

        body.createFixture(fixtureDef).setUserData(new UserData("chain", "chain", body.getPosition()));
        body.setSleepingAllowed(false);
        return body;
    }

    //    private utility methods
    private CircleShape createCircleShape(float radius) {
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(radius);

        return circleShape;
    }

    private PolygonShape createPolygonShape(Vector2[] vertices) {
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.set(vertices);
        return polygonShape;
    }

    private PolygonShape createRectangleShape(float width, float height) {
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(width, height);
        return polygonShape;
    }

    private EdgeShape getEdgeShape(float x1, float y1, float x2, float y2) {
        EdgeShape edgeShape = new EdgeShape();
        edgeShape.set(x1, y1, x2, y2);
        return edgeShape;
    }

    private FixtureDef getFixtureDef(Shape shape, float density, float friction, float restitution) {
        fixtureDef = new FixtureDef();
        fixtureDef.density = density;
        fixtureDef.friction = friction;
        fixtureDef.restitution = restitution;
        if (shape != null) fixtureDef.shape = shape;
        return fixtureDef;
    }

    private BodyDef getBodyDef(BodyDef.BodyType bodyType, Vector2 pos, float angle) {
        bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        bodyDef.position.set(pos.x, pos.y);
        bodyDef.angle = angle;
        return bodyDef;
    }

    public void createBrickWall(int count, float width, float height, float posX, float posY) {
        width /= 2;
        height /= 2;
        posX += width;
        float y = height;
        Body body = null;
        for (int c = 0; c < count; c++, y += height * 2) {
            for (float x = c * width; x < count * width * 2 - (c * width); x += width * 2) {
                body = getRectangleBody(BodyType.StaticBody, new Vector2(posX + x, posY + y), 0, width, height, 1f, 1f, 0f, "Brick");
                body.setAwake(true);
            }
        }
    }
}
