package net.civex4.nobility.gui;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import vg.civcraft.mc.civmodcore.api.ItemAPI;

public enum BannerLetter {
	A(createBanner('a')),
	B(createBanner('b')),
	C(createBanner('c')),
	D(createBanner('d')),
	E(createBanner('e')),
	F(createBanner('f')),
	G(createBanner('g')),
	H(createBanner('h')),
	I(createBanner('i')),
	J(createBanner('j')),
	K(createBanner('k')),
	L(createBanner('l')),
	M(createBanner('m')),
	N(createBanner('n')),
	O(createBanner('o')),
	P(createBanner('p')),
	Q(createBanner('q')),
	R(createBanner('r')),
	S(createBanner('s')),
	T(createBanner('t')),
	U(createBanner('u')),
	V(createBanner('v')),
	W(createBanner('w')),
	X(createBanner('x')),
	Y(createBanner('y')),
	Z(createBanner('z')),
	PERIOD(createBanner('.')),
	EXCLAMATION_MARK(createBanner('!')),
	QUESTION_MARK(createBanner('?'));
	
	private ItemStack banner;
	
	BannerLetter(ItemStack banner) {
		this.banner = banner;		
	}
	
	public ItemStack item() {
		return banner;
	}
	
	public static ItemStack[] createBanners(String word, DyeColor backgroundColor, DyeColor letterColor) {
		char[] chars = word.toCharArray();
		ItemStack[] banners = new ItemStack[chars.length];
		for (int i = 0; i < chars.length; i++) {
			banners[i] = createBanner(chars[i], backgroundColor, letterColor);
		}
		return banners;
	}
	
	public static ItemStack createBanner(char letter, DyeColor backgroundColor, DyeColor letterColor) {
		ItemStack banner = new ItemStack(Material.WHITE_BANNER);
		banner = setColor(banner, backgroundColor);
		BannerMeta meta = (BannerMeta) banner.getItemMeta();
		
		switch (letter) {
			case 'a':
				add(meta, PatternType.STRIPE_LEFT, letterColor);
				add(meta, PatternType.STRIPE_RIGHT, letterColor);
				add(meta, PatternType.STRIPE_TOP, letterColor);
				add(meta, PatternType.STRIPE_MIDDLE, letterColor);
				add(meta, PatternType.BORDER, backgroundColor);
				banner.setItemMeta(meta);
				break;
			case 'b':
				banner = setColor(banner, letterColor);
				meta = (BannerMeta) banner.getItemMeta();
				add(meta, PatternType.MOJANG, backgroundColor);
				add(meta, PatternType.STRIPE_RIGHT, letterColor);
				add(meta, PatternType.CURLY_BORDER, backgroundColor);
				add(meta, PatternType.STRIPE_LEFT, letterColor);
				add(meta, PatternType.BORDER, backgroundColor);
				banner.setItemMeta(meta);
				break;
			case 'c':
				add(meta, PatternType.STRIPE_RIGHT, letterColor);
				add(meta, PatternType.STRIPE_MIDDLE, backgroundColor);
				add(meta, PatternType.STRIPE_BOTTOM, letterColor);
				add(meta, PatternType.STRIPE_TOP, letterColor);
				add(meta, PatternType.STRIPE_LEFT, letterColor);
				add(meta, PatternType.BORDER, backgroundColor);
				banner.setItemMeta(meta);
				break;
			case 'd':
				add(meta, PatternType.STRIPE_RIGHT, letterColor);
				add(meta, PatternType.STRIPE_TOP, letterColor);
				add(meta, PatternType.STRIPE_BOTTOM, letterColor);
				add(meta, PatternType.CURLY_BORDER, backgroundColor);
				add(meta, PatternType.STRIPE_LEFT, letterColor);
				add(meta, PatternType.BORDER, backgroundColor);
				banner.setItemMeta(meta);
				break;
			case 'e':
				add(meta, PatternType.STRIPE_MIDDLE, letterColor);
				add(meta, PatternType.STRIPE_LEFT, letterColor);
				add(meta, PatternType.STRIPE_TOP, letterColor);
				add(meta, PatternType.STRIPE_BOTTOM, letterColor);
				add(meta, PatternType.BORDER, backgroundColor);
				banner.setItemMeta(meta);
				break;
			case 'f':
				add(meta, PatternType.STRIPE_MIDDLE, letterColor);
				add(meta, PatternType.STRIPE_LEFT, letterColor);
				add(meta, PatternType.STRIPE_TOP, letterColor);
				add(meta, PatternType.BORDER, backgroundColor);
				banner.setItemMeta(meta);
				break;
			case 'g':
				add(meta, PatternType.STRIPE_DOWNRIGHT, letterColor);
				add(meta, PatternType.HALF_HORIZONTAL, backgroundColor);
				add(meta, PatternType.STRIPE_BOTTOM, letterColor);
				add(meta, PatternType.STRIPE_LEFT, letterColor);
				add(meta, PatternType.STRIPE_TOP, letterColor);
				add(meta, PatternType.BORDER, backgroundColor);
				banner.setItemMeta(meta);
				break;
			case 'h':
				add(meta, PatternType.STRIPE_RIGHT, letterColor);
				add(meta, PatternType.STRIPE_LEFT, letterColor);
				add(meta, PatternType.STRIPE_MIDDLE, letterColor);
				add(meta, PatternType.BORDER, backgroundColor);
				banner.setItemMeta(meta);
				break;
			case 'i':
				add(meta, PatternType.STRIPE_TOP, letterColor);
				add(meta, PatternType.STRIPE_BOTTOM, letterColor);
				add(meta, PatternType.STRIPE_CENTER, letterColor);
				add(meta, PatternType.BORDER, backgroundColor);
				banner.setItemMeta(meta);
				break;
			case 'j':
				add(meta, PatternType.STRIPE_LEFT, letterColor);
				add(meta, PatternType.HALF_HORIZONTAL, backgroundColor);
				add(meta, PatternType.STRIPE_BOTTOM, letterColor);
				add(meta, PatternType.STRIPE_RIGHT, letterColor);
				add(meta, PatternType.BORDER, backgroundColor);
				banner.setItemMeta(meta);
				break;
			case 'k':
				add(meta, PatternType.STRIPE_DOWNRIGHT, letterColor);
				add(meta, PatternType.HALF_HORIZONTAL, backgroundColor);
				add(meta, PatternType.STRIPE_LEFT, letterColor);
				add(meta, PatternType.STRIPE_DOWNLEFT, letterColor);
				add(meta, PatternType.BORDER, backgroundColor);
				banner.setItemMeta(meta);
				break;
			case 'l':
				add(meta, PatternType.STRIPE_LEFT, letterColor);
				add(meta, PatternType.STRIPE_BOTTOM, letterColor);
				add(meta, PatternType.BORDER, backgroundColor);
				banner.setItemMeta(meta);
				break;
			case 'm':
				add(meta, PatternType.TRIANGLE_TOP, letterColor);
				add(meta, PatternType.TRIANGLES_TOP, backgroundColor);
				add(meta, PatternType.STRIPE_RIGHT, letterColor);
				add(meta, PatternType.STRIPE_LEFT, letterColor);
				add(meta, PatternType.BORDER, backgroundColor);
				banner.setItemMeta(meta);
				break;
			case 'n':
				add(meta, PatternType.STRIPE_LEFT, letterColor);
				add(meta, PatternType.STRIPE_DOWNRIGHT, letterColor);
				add(meta, PatternType.STRIPE_RIGHT, letterColor);
				add(meta, PatternType.BORDER, backgroundColor);
				banner.setItemMeta(meta);
				break;
			case 'o':
				banner = setColor(banner, letterColor);
				meta = (BannerMeta) banner.getItemMeta();
				add(meta, PatternType.RHOMBUS_MIDDLE, backgroundColor);
				add(meta, PatternType.STRIPE_RIGHT, letterColor);
				add(meta, PatternType.STRIPE_LEFT, letterColor);
				add(meta, PatternType.BORDER, backgroundColor);
				banner.setItemMeta(meta);
				break;
			case 'p':
				add(meta, PatternType.HALF_HORIZONTAL, letterColor);
				add(meta, PatternType.STRIPE_CENTER, backgroundColor);
				add(meta, PatternType.STRIPE_TOP, letterColor);
				add(meta, PatternType.STRIPE_MIDDLE, letterColor);
				add(meta, PatternType.STRIPE_LEFT, letterColor);
				add(meta, PatternType.BORDER, backgroundColor);
				banner.setItemMeta(meta);
				break;
			case 'q':
				banner = setColor(banner, letterColor);
				meta = (BannerMeta) banner.getItemMeta();
				add(meta, PatternType.RHOMBUS_MIDDLE, backgroundColor);
				add(meta, PatternType.STRIPE_RIGHT, letterColor);
				add(meta, PatternType.SQUARE_BOTTOM_RIGHT, letterColor);
				add(meta, PatternType.STRIPE_LEFT, letterColor);
				add(meta, PatternType.BORDER, backgroundColor);
				banner.setItemMeta(meta);
				break;
			case 'r':
				add(meta, PatternType.HALF_HORIZONTAL, letterColor);
				add(meta, PatternType.STRIPE_LEFT, letterColor);
				add(meta, PatternType.STRIPE_DOWNRIGHT, letterColor);
				add(meta, PatternType.BORDER, backgroundColor);
				banner.setItemMeta(meta);
				break;
			case 's':
				add(meta, PatternType.STRIPE_BOTTOM, letterColor);
				add(meta, PatternType.STRIPE_TOP, letterColor);
				add(meta, PatternType.STRIPE_DOWNRIGHT, letterColor);
				add(meta, PatternType.BORDER, backgroundColor);
				banner.setItemMeta(meta);
				break;
			case 't':
				add(meta, PatternType.STRIPE_TOP, letterColor);
				add(meta, PatternType.STRIPE_CENTER, letterColor);
				add(meta, PatternType.BORDER, backgroundColor);
				banner.setItemMeta(meta);
				break;
			case 'u':
				add(meta, PatternType.STRIPE_RIGHT, letterColor);
				add(meta, PatternType.STRIPE_BOTTOM, letterColor);
				add(meta, PatternType.STRIPE_LEFT, letterColor);
				add(meta, PatternType.BORDER, backgroundColor);
				banner.setItemMeta(meta);
				break;
			case 'v':
				add(meta, PatternType.STRIPE_LEFT, letterColor);
				add(meta, PatternType.SQUARE_BOTTOM_LEFT, backgroundColor);
				add(meta, PatternType.STRIPE_DOWNLEFT, letterColor);
				add(meta, PatternType.BORDER, backgroundColor);
				banner.setItemMeta(meta);
				break;
			case 'w':
				add(meta, PatternType.TRIANGLE_BOTTOM, letterColor);
				add(meta, PatternType.TRIANGLES_BOTTOM, backgroundColor);
				add(meta, PatternType.STRIPE_RIGHT, letterColor);
				add(meta, PatternType.STRIPE_LEFT, letterColor);
				add(meta, PatternType.BORDER, backgroundColor);
				banner.setItemMeta(meta);
				break;
			case 'x':
				add(meta, PatternType.CROSS, letterColor);
				add(meta, PatternType.BORDER, backgroundColor);
				banner.setItemMeta(meta);
				break;
			case 'y':
				add(meta, PatternType.STRIPE_DOWNRIGHT, letterColor);
				add(meta, PatternType.SQUARE_BOTTOM_RIGHT, backgroundColor);
				add(meta, PatternType.STRIPE_RIGHT, backgroundColor);
				add(meta, PatternType.STRIPE_DOWNLEFT, letterColor);
				add(meta, PatternType.BORDER, backgroundColor);
				banner.setItemMeta(meta);
				break;
			case 'z':
				add(meta, PatternType.STRIPE_TOP, letterColor);
				add(meta, PatternType.STRIPE_BOTTOM, letterColor);
				add(meta, PatternType.STRIPE_DOWNLEFT, letterColor);
				add(meta, PatternType.BORDER, backgroundColor);
				banner.setItemMeta(meta);
				break;
			case '.':
				add(meta, PatternType.TRIANGLES_BOTTOM, letterColor);
				add(meta, PatternType.STRIPE_LEFT, backgroundColor);
				add(meta, PatternType.STRIPE_RIGHT, backgroundColor);
				add(meta, PatternType.BORDER, backgroundColor);
				banner.setItemMeta(meta);
				break;
			case '?':
				banner = setColor(banner, letterColor);
				meta = (BannerMeta) banner.getItemMeta();
				add(meta, PatternType.HALF_HORIZONTAL, backgroundColor);
				add(meta, PatternType.STRIPE_RIGHT, letterColor);
				add(meta, PatternType.STRIPE_LEFT, backgroundColor);
				add(meta, PatternType.SQUARE_BOTTOM_RIGHT, backgroundColor);
				add(meta, PatternType.STRIPE_TOP, letterColor);
				add(meta, PatternType.BORDER, backgroundColor);
				banner.setItemMeta(meta);
				break;
			case '!':
				add(meta, PatternType.STRIPE_CENTER, letterColor);
				add(meta, PatternType.STRIPE_BOTTOM, backgroundColor);
				add(meta, PatternType.TRIANGLES_BOTTOM, letterColor);
				add(meta, PatternType.STRIPE_LEFT, backgroundColor);
				add(meta, PatternType.STRIPE_RIGHT, backgroundColor);
				add(meta, PatternType.BORDER, backgroundColor);
				banner.setItemMeta(meta);
				break;
				
				
				
				
		}
		ItemAPI.setDisplayName(banner, (letter + "").toUpperCase());
		return banner;
	}
	
	
	public static ItemStack createBanner(char letter) {
		return createBanner(letter, DyeColor.WHITE, DyeColor.BLACK);
	}
	
	private static void add(BannerMeta meta, PatternType patternType, DyeColor color) {
		Pattern pattern = new Pattern(color, patternType);
		meta.addPattern(pattern);
	}
	
	// clears patterns and adds new color
	private static ItemStack setColor(ItemStack banner, DyeColor color) {
		// TODO add the rest of the banner cases
		switch (color) {
			case WHITE:
				banner = new ItemStack(Material.WHITE_BANNER);
				break;
			case YELLOW:
				banner = new ItemStack(Material.YELLOW_BANNER);
				break;
			case BLUE:
				banner = new ItemStack(Material.BLUE_BANNER);
				break;
			case GREEN:
				banner = new ItemStack(Material.GREEN_BANNER);
				break;
			case BLACK:
				banner = new ItemStack(Material.BLACK_BANNER);
				break;
			case BROWN:
				banner = new ItemStack(Material.BROWN_BANNER);
				break;
			case CYAN:
				banner = new ItemStack(Material.CYAN_BANNER);
				break;
			case GRAY:
				banner = new ItemStack(Material.GRAY_BANNER);
				break;
			case LIGHT_BLUE:
				banner = new ItemStack(Material.LIGHT_BLUE_BANNER);
				break;
			case LIGHT_GRAY:
				banner = new ItemStack(Material.LIGHT_GRAY_BANNER);
				break;
			case LIME:
				banner = new ItemStack(Material.LIME_BANNER);
				break;
			case MAGENTA:
				banner = new ItemStack(Material.MAGENTA_BANNER);
				break;
			case ORANGE:
				banner = new ItemStack(Material.ORANGE_BANNER);
				break;
			case PINK:
				banner = new ItemStack(Material.PINK_BANNER);
				break;
			case PURPLE:
				banner = new ItemStack(Material.PURPLE_BANNER);
				break;
			case RED:
				banner = new ItemStack(Material.RED_BANNER);
				break;
			default:
				break;
		}
		
		return banner;
	}
	
}
