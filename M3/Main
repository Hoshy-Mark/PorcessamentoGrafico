import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.system.MemoryUtil.NULL;

public class Main {

    private long window;
    private int score = 300;

    public void run() {
        try {
            init();
            List<List<Rectangle>> grid = new ArrayList<>();
            float[][] colors = {
                    {1.0f, 0.0f, 0.0f}, // Red
                    {0.0f, 1.0f, 0.0f}, // Green
                    {0.0f, 0.0f, 1.0f}, // Blue
                    {1.0f, 1.0f, 0.0f}, // Yellow
                    {1.0f, 0.0f, 1.0f}, // Magenta
                    {0.0f, 1.0f, 1.0f}  // Cyan
            };

            for(int i = 0; i < 4; i++){
                List<Rectangle> row = new ArrayList<>();
                for(int j = 0; j < 8; j++){
                    int randomColorIndex = (int)(Math.random()*colors.length);
                    float[] color = colors[randomColorIndex];
                    row.add(new Rectangle(i*200, j*100, 200, 100,
                            color[0],
                            color[1],
                            color[2]));
                }
                grid.add(row);
            }

            // Coloca o callback do mouse aqui
            GLFW.glfwSetMouseButtonCallback(window, new GLFWMouseButtonCallback() {
                @Override
                public void invoke(long window, int button, int action, int mods) {
                    if (button == GLFW.GLFW_MOUSE_BUTTON_1 && action == GLFW.GLFW_PRESS) {
                        DoubleBuffer xb = BufferUtils.createDoubleBuffer(1);
                        DoubleBuffer yb = BufferUtils.createDoubleBuffer(1);
                        GLFW.glfwGetCursorPos(window, xb, yb);
                        float x = (float) xb.get(0);
                        float y = 600 - (float) yb.get(0);

                        for (List<Rectangle> row : grid) {
                            for (Rectangle rectangle : row) {
                                if (rectangle.isInside(x, y)) {
                                    float[] clickedColor = rectangle.getColor();
                                    int count = 0;
                                    for (List<Rectangle> row2 : grid) {
                                        for (Rectangle rectangle2 : row2) {
                                            if (Arrays.equals(clickedColor, rectangle2.getColor())) {
                                                rectangle2.setVisible(false);
                                                count++;
                                            }
                                        }
                                    }
                                    score -= (5 * count); // Adicione esta linha
                                    System.out.println(count + " retângulos foram excluídos. Pontuação atual: " + score); // Modifique esta linha
                                    break;
                                }
                            }
                        }
                    }
                }
            });

            while (!GLFW.glfwWindowShouldClose(window)) {
                GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

                for(List<Rectangle> row : grid){
                    for(Rectangle rectangle : row)
                        rectangle.draw();
                }

                GLFW.glfwSwapBuffers(window);
                GLFW.glfwPollEvents();
            }
        } finally {
            GLFW.glfwTerminate();
        }
    }

    public void init() {
        // Initialize GLFW
        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Configure GLFW
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

        org.lwjgl.opengl.GL11.glMatrixMode(org.lwjgl.opengl.GL11.GL_PROJECTION);
        org.lwjgl.opengl.GL11.glLoadIdentity();
        org.lwjgl.opengl.GL11.glOrtho(0, 800, 0, 600, 1, -1);
        org.lwjgl.opengl.GL11.glMatrixMode(org.lwjgl.opengl.GL11.GL_MODELVIEW);
    }


    public static void main(String[] args) {
        new Main().run();
    }
}
