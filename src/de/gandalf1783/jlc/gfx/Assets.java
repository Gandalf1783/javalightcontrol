package de.gandalf1783.jlc.gfx;

import java.awt.image.BufferedImage;

public class Assets {

	public static BufferedImage checked_box, unchecked_box, selected_radio_circle, unselected_radio_circle; // Selection Buttons
	public static BufferedImage[] h_scrollbar, v_scrollbar; // Scrollbars
	public static BufferedImage btn, btn_hover, btn_press; // Square Button
	public static BufferedImage arrow_right, arrow_left, arrow_up, arrow_down; //Arrow Buttons
	public static BufferedImage icon;
	public static BufferedImage[] dropdown_round;


	public static void init() {

		//font28 = FontLoader.loadFont("res/fonts/slkscr.ttf", 28);
		icon = ImageLoader.loadImage("ui/icon.png");
		SpriteSheet horizontalScrollbarSheet = new SpriteSheet(ImageLoader.loadImage("ui/horizontial_scrollbar.png"));
		SpriteSheet verticalScrollbarSheet = new SpriteSheet(ImageLoader.loadImage("ui/vertical_scrollbar.png"));
		SpriteSheet dropdownRoundSheet = new SpriteSheet(ImageLoader.loadImage("ui/rounded_dropdown_dropped.png"));

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

		arrow_left = h_scrollbar[0]; //Using the Arrows from the scrollbars, and renaming them for easier access
		arrow_right = h_scrollbar[3];
		arrow_up = v_scrollbar[0];
		arrow_down = v_scrollbar[3];

		btn = ImageLoader.loadImage("ui/square_button_normal.png");
		btn_hover = ImageLoader.loadImage("ui/square_button_hover.png");
		btn_press = ImageLoader.loadImage("ui/square_button_mouse_down.png");


		dropdown_round = new BufferedImage[3];
		dropdown_round[0] = dropdownRoundSheet.crop(0, 0, 90, 30);
		dropdown_round[1] = dropdownRoundSheet.crop(0, 30, 90, 30);
		dropdown_round[2] = dropdownRoundSheet.crop(0, 60, 90, 30);

	}

}
