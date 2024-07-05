import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.system.MemoryUtil.NULL;

public class Main {

    private long window;
    private int backgroundTexture;
    private Sprite sprite;



    public void run() {
        try {
            init();
            // Atualize os caminhos das texturas para caminhos absolutos
            backgroundTexture = loadTexture("Background.png");
            sprite = new Sprite("spritesheet.png", 96, 16, 16, 16, 0, 0, 3);

            sprite.setFrames(4);
            sprite.setAnimSpeed(150);

            while (!GLFW.glfwWindowShouldClose(window)) {
                GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
                drawBackground();
                sprite.draw();

                GLFW.glfwSwapBuffers(window);
                GLFW.glfwPollEvents();
            }
        } finally {
            GLFW.glfwTerminate();
        }
    }

    public void init() {
        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);

        window = GLFW.glfwCreateWindow(800, 600, "Test", NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        GLFW.glfwMakeContextCurrent(window);
        GLFW.glfwShowWindow(window);
        GL.createCapabilities();

        GLFW.glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (action == GLFW.GLFW_PRESS || action == GLFW.GLFW_REPEAT) {
                switch (key) {
                    case GLFW.GLFW_KEY_W:
                    case GLFW.GLFW_KEY_UP:
                        sprite.move(0, -5); // Up
                        break;
                    case GLFW.GLFW_KEY_A:
                    case GLFW.GLFW_KEY_LEFT:
                        sprite.move(-5, 0); // Left
                        break;
                    case GLFW.GLFW_KEY_S:
                    case GLFW.GLFW_KEY_DOWN:
                        sprite.move(0, 5); // Down
                        break;
                    case GLFW.GLFW_KEY_D:
                    case GLFW.GLFW_KEY_RIGHT:
                        sprite.move(5, 0); // Right
                        break;
                    default:
                }
            }
        });


        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, 800, 600, 0, 1, -1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
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

    private void drawBackground() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, backgroundTexture);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex2f(0, 0);
        GL11.glTexCoord2f(1, 0);
        GL11.glVertex2f(800, 0);
        GL11.glTexCoord2f(1, 1);
        GL11.glVertex2f(800, 600);
        GL11.glTexCoord2f(0, 1);
        GL11.glTexCoord2f(0, 1);
        GL11.glVertex2f(0, 600);
        GL11.glEnd();
    }

    public static void main(String[] args) {
        new Main().run();
    }
}
