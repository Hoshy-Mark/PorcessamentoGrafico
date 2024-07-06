package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import com.badlogic.gdx.math.Rectangle;

public class MyGdxGame extends ApplicationAdapter {
	private static int[][] tileMap;
	private static int[][] tileMap2;

	private Rectangle characterBounds; // Rectangle do personagem
	private Rectangle[][] tileBounds; // Rectangles para os tiles
	private final static int HEIGHT = 32;
	private final static int WIDTH = 64;
	private static final float TileSCALE = 1;
	private static final float CharacterSCALE = 2;

	private Animation<TextureRegion> walkAnimation; // #3
	private Texture walkSheet; // #4
	private SpriteBatch spriteBatch;

	// Constantes para controle do grid e do movimento dos sprites
	private static final int FRAME_COLS = 4; // Número de colunas do grid do sprite do character
	private static final int FRAME_ROWS = 1; // Número de linhas do grid do sprite do character

	// Objeto que irá controlar a posição da animação
	private float stateTime;
	private Vector2 characterPosition = new Vector2();
	private static Texture[] tileSets = new Texture[8];// Array para armazenar os tiles


	private SpriteBatch batch;
	private OrthographicCamera camera;
	private float winDelay = 0;
	private boolean gameWon = false;

	public void create() {
		// Inicialize a câmera e o SpriteBatch
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch = new SpriteBatch();

		try {
			FileReader fileReader = new FileReader("map.txt");
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			ArrayList<String[]> lines = new ArrayList<String[]>();
			String line = null;
			while((line = bufferedReader.readLine()) != null) {
				line = line.trim(); // Adicione esta linha para remover espaços em branco antes e depois da linha
				lines.add(line.split(","));
			}
			tileMap = new int[lines.size()][];
			tileMap2 = new int[lines.size()][];
			for(int i = 0; i < lines.size(); i++) {
				String[] lineParts = lines.get(i);
				tileMap[i] = new int[lineParts.length];
				tileMap2[i] = new int[lineParts.length];
				for(int j = 0; j < lineParts.length; j++) {
					// Adicionar bloco try-catch para o Integer.parseInt() para lidar com NumberFormatException
					try {
						tileMap[i][j] = Integer.parseInt(lineParts[j]);
						tileMap2[i][j] = Integer.parseInt(lineParts[j]);
					} catch (NumberFormatException e) {
						System.err.println("Erro ao tentar analisar número na posição " + i + "," + j);
						e.printStackTrace();
					}
				}
			}
			bufferedReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		for(int i = 0; i < tileSets.length; i++){
			tileSets[i] = new Texture("Tile" + (i+1) + ".png"); // Carregando as imagens do tileSet
		}
		walkSheet = new Texture(Gdx.files.internal("spritesheet.png")); // Carrega a sprite sheet como uma textura
		TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth() / FRAME_COLS, walkSheet.getHeight() / FRAME_ROWS); // Divida a textura em pedaços
		TextureRegion[] walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];

		characterBounds = new Rectangle(0, 0, walkSheet.getWidth() / FRAME_COLS * CharacterSCALE, walkSheet.getHeight() / FRAME_ROWS * CharacterSCALE);
		tileBounds = new Rectangle[tileMap.length][tileMap[0].length];

		Gdx.input.setInputProcessor(new InputAdapter() {
			public boolean touchDragged (int x, int y, int pointer) {
				camera.translate(-Gdx.input.getDeltaX(), Gdx.input.getDeltaY());
				camera.update();
				return false;
			}
		});

		for(int i = 0; i < tileMap.length; i++) {
			for(int j = 0; j < tileMap[i].length; j++) {
				int x = j * WIDTH / 2 - i * WIDTH / 2;
				int y = j * HEIGHT / 2 + i * HEIGHT / 2;
				tileBounds[i][j] = new Rectangle(x, y, WIDTH * TileSCALE, HEIGHT * TileSCALE);
			}
		}


		int index = 0;
		for (int i = 0; i < FRAME_ROWS; i++) {
			for (int j = 0; j < FRAME_COLS; j++) {
				walkFrames[index++] = tmp[i][j];
			}
		}

		walkAnimation = new Animation<TextureRegion>(0.100f, walkFrames); // Initialize the Animation with the frame interval and array of frames
		spriteBatch = new SpriteBatch(); // Instantiate a SpriteBatch for drawing and reset the elapsed animation

		stateTime = 0f; // tempo que a sprite está em uma frame, zero significa começar do começo.



		// Inicializa a posição do personagem.
		characterPosition.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
	}

	public void render() {
		ScreenUtils.clear(0, 0, 0, 1);

		stateTime += Gdx.graphics.getDeltaTime();
		TextureRegion currentFrame = walkAnimation.getKeyFrame(stateTime, true);

		batch.setProjectionMatrix(camera.combined);

		batch.begin();

		// Renderizar o mapa calibrando com os offsets
		for(int i = 0; i < tileMap.length; i++) {
			for(int j = 0; j < tileMap[i].length; j++) {
				// Posição ajustada para obter um padrão de diamante
				int x = j * WIDTH / 2 - i * WIDTH / 2;
				int y = j * HEIGHT / 2 + i * HEIGHT / 2;
				batch.draw(tileSets[tileMap[i][j]-1], x, y, 64 * TileSCALE, 32 * TileSCALE);
			}
		}


		batch.end();
		spriteBatch.begin();
		spriteBatch.draw(currentFrame, characterPosition.x - currentFrame.getRegionWidth() / 2, characterPosition.y - currentFrame.getRegionHeight() / 2, currentFrame.getRegionWidth() * CharacterSCALE, currentFrame.getRegionHeight() * CharacterSCALE);
		spriteBatch.end();
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
			characterPosition.x = characterPosition.x + Gdx.graphics.getDeltaTime() * 100;
		} else if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
			characterPosition.x = characterPosition.x - Gdx.graphics.getDeltaTime() * 100;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.UP)){
			characterPosition.y = characterPosition.y + Gdx.graphics.getDeltaTime() * 100;
		} else if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
			characterPosition.y = characterPosition.y - Gdx.graphics.getDeltaTime() * 100;
		}
		checkCollision();

		camera.position.set(characterPosition.x, characterPosition.y, 0);
		camera.update();
		if(gameWon) {
			winDelay += Gdx.graphics.getDeltaTime();
			if(winDelay > 2) {
				Gdx.app.exit();
			}
		}
	}

	@Override
	public void dispose () {
		batch.dispose();
		for (Texture tileSet : tileSets) {
			tileSet.dispose();
		}
		walkSheet.dispose();
		spriteBatch.dispose();
	}

	public void checkCollision() {
		characterBounds.setPosition(characterPosition.x, characterPosition.y);
		for (int i = 0; i < tileMap.length; i++) {
			for (int j = 0; j < tileMap[i].length; j++) {
				if (characterBounds.overlaps(tileBounds[i][j])) {
					if (tileMap[i][j] == 3) { // Se o tile for tile3
						System.out.println("Player morreu!");
						Gdx.app.exit(); // Fecha o jogo
						return;
					}
					if (tileMap[i][j] == 6) { // Se o tile for tile6
						System.out.println("Player colidiu com um Baú!");
						tileMap[i][j] = 7; // Muda para tile7

						if (i+1 < tileMap.length) {
							tileMap[i+1][j] = 8;   // Cria um tile8 na linha abaixo
						}
					}
					if (tileMap[i][j] == 8) {
						System.out.println("Parabéns! Você ganhou uma espada e venceu o jogo!");
						tileMap[i][j] = 2;
						gameWon = true;  // Define o gameWon para true quando o jogador vence
						return;
					}
				}
			}
		}
	}
}
