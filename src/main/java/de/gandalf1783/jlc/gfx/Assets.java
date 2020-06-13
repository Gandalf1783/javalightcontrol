package de.gandalf1783.jlc.gfx;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Assets {

	private static final int width = 32, height = 32;

	public static Font font28;

	public static BufferedImage dirt, grass, stone, tree, rock;
	public static BufferedImage wood;
	public static BufferedImage[] player_down, player_up, player_left, player_right;
	public static BufferedImage[] zombie_down, zombie_up, zombie_left, zombie_right;
	public static BufferedImage[] btn_start;
	public static BufferedImage inventoryScreen;
	public static BufferedImage checked_box, unchecked_box, selected_radio_circle, unselected_radio_circle; // Selection Buttons
	public static BufferedImage[] h_scrollbar, v_scrollbar; // Scrollbars
	public static BufferedImage btn, btn_hover, btn_press; // Square Button

	public static void init() {


		//font28 = FontLoader.loadFont("res/fonts/slkscr.ttf", 28);


		SpriteSheet sheet = new SpriteSheet(ImageLoader.loadImage("textures/sheet.png"));
		SpriteSheet horizontalScrollbarSheet = new SpriteSheet(ImageLoader.loadImage("ui/horizontial_scrollbar.png"));
		SpriteSheet verticalScrollbarSheet = new SpriteSheet(ImageLoader.loadImage("ui/vertical_scrollbar.png"));


		checked_box = ImageLoader.loadImage("ui/checked_box.png");
		unchecked_box = ImageLoader.loadImage("ui/unchecked_box.png");
		selected_radio_circle = ImageLoader.loadImage("ui/selected_radio_button.png");
		unselected_radio_circle = ImageLoader.loadImage("ui/unselected_radio_button.png");

		h_scrollbar = new BufferedImage[4];
		h_scrollbar[0] = horizontalScrollbarSheet.crop(0, 0, 40, 40);
		h_scrollbar[1] = horizontalScrollbarSheet.crop(40, 0, 40, 40);
		h_scrollbar[2] = horizontalScrollbarSheet.crop(80, 0, 40, 40);
		h_scrollbar[3] = horizontalScrollbarSheet.crop(333, 0, 40, 40);

		v_scrollbar = new BufferedImage[4];
		v_scrollbar[0] = verticalScrollbarSheet.crop(0, 0, 40, 40);
		v_scrollbar[1] = verticalScrollbarSheet.crop(0, 40, 40, 40);
		v_scrollbar[2] = verticalScrollbarSheet.crop(0, 80, 40, 40);
		v_scrollbar[3] = verticalScrollbarSheet.crop(0, 333, 40, 40);


		btn = ImageLoader.loadImage("ui/square_button_normal.png");
		btn_hover = ImageLoader.loadImage("ui/square_button_hover.png");
		btn_press = ImageLoader.loadImage("ui/square_button_mouse_down.png");

		inventoryScreen = ImageLoader.loadImage("textures/inventoryScreen.png");

		wood = sheet.crop(width, height, width, height);

		btn_start = new BufferedImage[2];
		btn_start[0] = sheet.crop(width * 6, height * 4, width * 2, height);
		btn_start[1] = sheet.crop(width * 6, height * 5, width * 2, height);

		player_down = new BufferedImage[2];
		player_up = new BufferedImage[2];
		player_left = new BufferedImage[2];
		player_right = new BufferedImage[2];

		player_down[0] = sheet.crop(width * 4, 0, width, height);
		player_down[1] = sheet.crop(width * 5, 0, width, height);
		player_up[0] = sheet.crop(width * 6, 0, width, height);
		player_up[1] = sheet.crop(width * 7, 0, width, height);
		player_right[0] = sheet.crop(width * 4, height, width, height);
		player_right[1] = sheet.crop(width * 5, height, width, height);
		player_left[0] = sheet.crop(width * 6, height, width, height);
		player_left[1] = sheet.crop(width * 7, height, width, height);

		zombie_down = new BufferedImage[2];
		zombie_up = new BufferedImage[2];
		zombie_left = new BufferedImage[2];
		zombie_right = new BufferedImage[2];

		zombie_down[0] = sheet.crop(width * 4, height * 2, width, height);
		zombie_down[1] = sheet.crop(width * 5, height * 2, width, height);
		zombie_up[0] = sheet.crop(width * 6, height * 2, width, height);
		zombie_up[1] = sheet.crop(width * 7, height * 2, width, height);
		zombie_right[0] = sheet.crop(width * 4, height * 3, width, height);
		zombie_right[1] = sheet.crop(width * 5, height * 3, width, height);
		zombie_left[0] = sheet.crop(width * 6, height * 3, width, height);
		zombie_left[1] = sheet.crop(width * 7, height * 3, width, height);

		dirt = sheet.crop(width, 0, width, height);
		grass = sheet.crop(width * 2, 0, width, height);
		stone = sheet.crop(width * 3, 0, width, height);
		tree = sheet.crop(0, 0, width, height * 2);
		rock = sheet.crop(0, height * 2, width, height);
	}

}
