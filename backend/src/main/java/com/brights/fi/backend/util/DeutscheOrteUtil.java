package com.brights.fi.backend.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DeutscheOrteUtil {
	private static final List<String> deutscheOrte = new ArrayList<>(Arrays.asList(
			"Berlin", "Hamburg", "München", "Köln", "Frankfurt", "Essen", "Dortmund", "Stuttgart",
			"Düsseldorf", "Bremen", "Hannover", "Duisburg", "Nürnberg", "Leipzig", "Dresden",
			"Bochum", "Wuppertal", "Bielefeld", "Bonn", "Mannheim", "Karlsruhe", "Gelsenkirchen",
			"Wiesbaden", "Münster", "Mönchengladbach", "Chemnitz", "Augsburg", "Braunschweig",
			"Aachen", "Krefeld", "Halle", "Kiel", "Magdeburg", "Oberhausen", "Lübeck", "Freiburg",
			"Hagen", "Erfurt", "Kassel", "Rostock", "Mainz", "Hamm", "Saarbrücken", "Herne",
			"Mülheim", "Solingen", "Osnabrück", "Ludwigshafen", "Leverkusen", "Oldenburg", "Neuss",
			"Paderborn", "Heidelberg", "Darmstadt", "Potsdam", "Würzburg", "Göttingen", "Regensburg",
			"Recklinghausen", "Bottrop", "Wolfsburg", "Heilbronn", "Ingolstadt", "Ulm", "Remscheid",
			"Pforzheim", "Bremerhaven", "Offenbach", "Fürth", "Reutlingen", "Salzgitter", "Siegen",
			"Gera", "Koblenz", "Moers", "Bergisch Gladbach", "Cottbus", "Hildesheim", "Witten",
			"Zwickau", "Erlangen", "Iserlohn", "Trier", "Kaiserslautern", "Jena", "Schwerin",
			"Gütersloh", "Marl", "Lünen", "Esslingen", "Velbert", "Ratingen", "Düren", "Ludwigsburg",
			"Wilhelmshaven", "Hanau", "Minden", "Flensburg", "Dessau", "Villingen-Schwenningen"
	));

	private static final Random random = new Random();

	public static String gebeNeuenOrt() {
		if (deutscheOrte.isEmpty()) {
			return "Keine Städte mehr verfügbar";
		}
		int index = random.nextInt(deutscheOrte.size());
		return deutscheOrte.remove(index);
	}

	public static void erneuerOrte() {
		deutscheOrte.addAll(Arrays.asList(
				// Add all the cities again...
		));
	}
}
