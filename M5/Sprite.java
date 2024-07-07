import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class Sprite {
    private int textureId;
    private float x, y;
    private float width, height;
    private float scaleX, scaleY;
    private float rotation;
    private float originX, originY;
    private float lastX, lastY;

    private double lastTime;
    private int cols;        
    private int rows;        
    private float frameTime;
    private double animSpeed;
    private int frames = 4; 

    public Sprite(String textureFile, float x, float y, float width, float height, float originX, float originY, float scale) {
        this.textureId = loadTexture(textureFile);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.originX = originX;
        this.originY = originY;
        this.scaleX = scale;
        this.scaleY = scale;
    }
    public void move(float dx, float dy) {
        x += dx;
        y += dy;
    }
    public void setFrames(int frames) {
        this.frames = frames;
    }

    public void setAnimSpeed(double animSpeed) {
        this.animSpeed = animSpeed;
    }
    public void draw() {
        double currentTime = GLFW.glfwGetTime();
        double deltaTime = currentTime - lastTime;
        lastTime = currentTime;
        int currentFrame;
        if (x != lastX || y != lastY) {
            frameTime += deltaTime * animSpeed;
            if (frameTime > frames) {
                frameTime -= frames;
            }
        }
        if(lastX == x || lastY == y){
            currentFrame = 1;
        }
        currentFrame = (int) frameTime;

        float frameOffset = 1.f / frames * currentFrame;

        GL11.glPushMatrix();
        GL11.glTranslatef(x + originX, y + originY, 0);
        GL11.glScalef(scaleX, scaleY, 1);
        GL11.glRotatef(rotation, 0, 0, 1);
        GL11.glTranslatef(-originX, -originY, 0);

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(frameOffset, 0);
        GL11.glVertex2f(0, 0);
        GL11.glTexCoord2f(frameOffset + 1.f / frames, 0);
        GL11.glVertex2f(width, 0);
        GL11.glTexCoord2f(frameOffset + 1.f / frames, 1);
        GL11.glVertex2f(width, height);
        GL11.glTexCoord2f(frameOffset, 1);
        GL11.glVertex2f(0, height);
        GL11.glEnd();

        GL11.glPopMatrix();
        lastX = x;
        lastY = y;
    }

    public void setScale(float scaleX, float scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    private int loadTexture(String filePath) {
        int width, height;
        ByteBuffer image;

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer comp = stack.mallocInt(1);

            image = STBImage.stbi_load(filePath, w, h, comp, 4);
            if (image == null) {
                throw new RuntimeException("Failed to load a texture file!" + System.lineSeparator() + STBImage.stbi_failure_reason());
            }

            width = w.get();
            height = h.get();
        }

        int textureID = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);

        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, image);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

        STBImage.stbi_image_free(image);

        return textureID;
    }
}
