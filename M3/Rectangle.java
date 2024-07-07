import org.lwjgl.opengl.GL11;

public class Rectangle {

    private float x, y;
    private float width, height;
    private float red, green, blue;

    public Rectangle(float x, float y, float width, float height, float red, float green, float blue){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public void draw(){
        if (!visible) return;

        GL11.glColor3f(red, green, blue);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x + width, y);
        GL11.glVertex2f(x + width, y + height);
        GL11.glVertex2f(x, y + height);
        GL11.glEnd();
    }

    public boolean isInside(float x, float y){
        return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.height;
    }

    public float[] getColor() {
        return new float[]{red, green, blue};
    }

    // Adicione um campo booleano para controlar a visibilidade
    private boolean visible = true;

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    public float colorDifference(float r, float g, float b){
        return Math.abs(this.red - r) + Math.abs(this.green - g) + Math.abs(this.blue - b);
    }
}
