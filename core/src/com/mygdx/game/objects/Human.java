package com.mygdx.game.objects;

import static com.mygdx.game.helpers.AssetManager.frameActual;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.helpers.AssetManager;
import com.mygdx.game.utils.Methods;
import com.mygdx.game.utils.Settings;

public class Human extends Actor {
    // Diferents posicions de Human: recta, pujant i baixant
    public static final int HUMAN_IDLE = Settings.IDLE;
    public static final int HUMAN_UP = Settings.UP;
    public static final int HUMAN_RIGHT = Settings.RIGHT;
    public static final int HUMAN_DOWN = Settings.DOWN;
    public static final int HUMAN_LEFT = Settings.LEFT;

    // Paràmetres de Human
    private Vector2 position;
    private int width, height;
    private int direction;
    private Rectangle boundingBox;
    private float tiempoAnim = 0f;
    public static int humanFacing = Settings.IDLE;
    public static int MAX_HEALTH = 100;
    public static int health = MAX_HEALTH;
    public static int regeneration = 1;
    private float timer = 0;
    private float TIMER_VELOCITY = 2f;
    private float centreHumanX, centreHumanY;



    public Human(float x, float y, int width, int height) {

        // Inicialitzem els arguments segons la crida del constructor
        this.width = width;
        this.height = height;
        position = new Vector2(x, y);

        // Inicialitzem Human a l'estat normal
        direction = HUMAN_IDLE;
        // Creem el rectangle de col·lisions
        boundingBox = new Rectangle(position.x+4, position.y, width/2, height/2);
        centreHumanX = x+5;
        centreHumanY = y+5;

    }
    public void act(float delta) {
        Settings.FIREBALL_SPAWN_TIMER += delta;
        // Movem l'Spacecraft depenent de la direcció controlant que no surti de la pantalla
        switch (direction) {
            case HUMAN_UP:
                humanFacing = Settings.UP;
                if (this.position.y + Settings.HUMAN_VELOCITY * delta <= Settings.GAME_HEIGHT) {
                    this.position.y += Settings.HUMAN_VELOCITY * delta;
                    this.centreHumanY += Settings.HUMAN_VELOCITY * delta;
                }
                break;
            case HUMAN_RIGHT:
                humanFacing = Settings.RIGHT;
                if (this.position.x - Settings.HUMAN_VELOCITY * delta <= Settings.GAME_WIDTH) {
                    this.position.x -= Settings.HUMAN_VELOCITY * delta;
                    this.centreHumanX -= Settings.HUMAN_VELOCITY * delta;
                }
                break;
            case HUMAN_DOWN:
                humanFacing = Settings.DOWN;
                if (this.position.y - height + Settings.HUMAN_VELOCITY * delta >= 0) {
                    this.position.y -= Settings.HUMAN_VELOCITY * delta;
                    this.centreHumanY -= Settings.HUMAN_VELOCITY * delta;
                }
                break;
            case HUMAN_LEFT:
                humanFacing = Settings.LEFT;
                if (this.position.x + Settings.HUMAN_VELOCITY * delta >= 0) {
                    this.position.x += Settings.HUMAN_VELOCITY * delta;
                    this.centreHumanX += Settings.HUMAN_VELOCITY * delta;
                }
                break;
            case HUMAN_IDLE:
                humanFacing = Settings.DOWN;
                break;
        }
        if(timer > 0) {
            timer -= delta;
        }
        regenerarVida(regeneration);
    }

    private void regenerarVida(int regeneration) {
        if (health < MAX_HEALTH && timer <= 0) {
            health = health + regeneration;
            timer = TIMER_VELOCITY;
        }
    }

    public boolean canFireFireball() {
        return (Settings.FIREBALL_SPAWN_TIMER - Settings.TIME_BETWEEN_FIREBALL_SPAWNS >= 0);
    }


    public void getHit(int damage) {

        health = health-damage;

    }

    public Fireball[] fireFireball() {
        Fireball[] fireball = new Fireball[2];
        fireball[0] = new Fireball(getCentreX(), getCentreY(), Settings.BULLET_WIDTH, Settings.BULLET_HEIGHT);
        fireball[1] = new Fireball(getCentreX(), getCentreY()*0.93f, Settings.BULLET_WIDTH, Settings.BULLET_HEIGHT);

        Settings.FIREBALL_SPAWN_TIMER = 0;

        return fireball;
    }

    // Obtenim el TextureRegion depenent de la posició de l'spacecraft
    public Animation getHumanTexture() {

        switch (direction) {

            case HUMAN_UP:
                return AssetManager.aHumanUp;
            case HUMAN_RIGHT:
                return AssetManager.aHumanLeft;
            case HUMAN_DOWN:
                return AssetManager.aHumanDown;
            case HUMAN_LEFT:
                return AssetManager.aHumanRight;
            default:
                return AssetManager.aHumanIdle;
        }
    }

    // Getters dels atributs principals
    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public Rectangle getCollisionRect() {
        return boundingBox;
    }

    // Canviem la direcció de l'Spacecraft: Puja
    public void goUp() {
        direction = HUMAN_UP;
    }

    // Canviem la direcció de l'Spacecraft: Dreta
    public void goRight() {
        direction = HUMAN_RIGHT;
    }

    // Canviem la direcció de l'Spacecraft: Baixa
    public void goDown() {
        direction = HUMAN_DOWN;
    }

    // Canviem la direcció de l'Spacecraft: Esquerra
    public void goLeft() {
        direction = HUMAN_LEFT;
    }

    // Posem l'Spacecraft al seu estat original
    public void goStraight() {
        direction = HUMAN_IDLE;
    }

    public static int getHealth() {
        return health;
    }

    public static int getMaxHealth() {
        return MAX_HEALTH;
    }

    public float getCentreX() {
        return centreHumanX;
    }

    public float getCentreY() {
        return centreHumanY;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        tiempoAnim += Gdx.graphics.getDeltaTime(); //es el tiempo que paso desde el ultimo render
        Animation frameDir = getHumanTexture();
        frameActual = (TextureRegion) frameDir.getKeyFrame(tiempoAnim,true);
        batch.draw(frameActual,getX(),getY());
    }



}
