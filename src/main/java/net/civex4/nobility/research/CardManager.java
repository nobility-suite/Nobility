package net.civex4.nobility.research;

import org.bukkit.entity.Player;

import net.civex4.nobility.blueprints.Blueprint;
import net.civex4.nobility.developments.AbstractWorkshop;

public class CardManager {

	//TODO
	public Card generateCard(Blueprint bp, Player p, AbstractWorkshop w) {
		return new Card(CardType.LOCK_IN,bp);
	}
}
