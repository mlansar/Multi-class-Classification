package Perceptron;

public class Cerveau {

	
	/* On initialise un Eta qui est le pas d'apprentissage.
	 * Après plusieurs test en interne, cette valeur est plutot optimal*/
	static double eta = 0.1;

	
	/**
    Créer un nouveau tableau 2D où l'on rajoute une case, pour chaque ligne, initialisé à 1.
    @param data Un tableau 2D (double).
    @return Le tableau data (double) d'une case plus grande pour chaque ligne où la premiere valeur est 1.
	*/
	public static double[][] rajoutCase(double[][] data) {
		double[][] newData = new double[data.length][data[0].length + 1];
		for (int i = 0; i < data.length; i++) {
			newData[i][0] = 1;							// On initialise tout les premiers poids à 1
			for (int j = 0; j < data[0].length; j++) {
				newData[i][j + 1] = data[i][j];
			}
		}
		return newData;
	}

	
	/**
    Calcul le produit scalaire de deux vecteurs.
    @param vector1 Un tableau (double).
    @param vector2 Un deuxieme (double).
    @return La valeur (double) du produit scalaire entre les vecteur vecteur1 et vecteur2.
	*/
	public static double produitScalaire(double[] vector1, double[] vector2) {
		double valeur = 0;
		for (int i = 0; i < vector1.length; i++) {
			valeur = valeur + vector1[i] * vector2[i];
		}
		return valeur;
	}

	
	/**
    Obtenir l'argument maximal d'un tableau.
    @param yProbabilite Un tableau (double) de double.
    @return La valeur (int) de la classe où la probabilité est la plus élevée.
    */
	public static int argumentMax(double[] yProbabilite) {
		double probMax = 0;
		int indMax = 0;
		for (int i = 0; i < yProbabilite.length; i++) {
			if (yProbabilite[i] > probMax) {			// On parcourt le tableau de probabilité
				probMax = yProbabilite[i];				// On stock le maximum
				indMax = i;								// On stocke l'indice
			}
		}
		return indMax + 1;
	}
	
	/**
    Obtenir le tableau de probabilité qu'une image appartienne à une classe.
    @param data Le tableau 2D (double) contenant les pixels des images.
    @param w Le tableau 2D (double) contenant les poids du perceptron.
    @param image L'indice (int) de l'image.
    @return Un tableau (double) contenant la probabilité qu'une image appartienne à une classe donnée.
	*/
	public static double[] probSoftmax(double[][] data,double[][] w, int image) {
		double[] numerateur = new double[w.length];
		double denominateur = 0;
		for (int classe = 0; classe < w.length; classe++) {								
			numerateur[classe] = Math.exp((produitScalaire(w[classe], data[image]))); // On calcul le numérateur pour chaque
			denominateur += numerateur[classe];										  // image et on calcul le numérateur
		}
		for (int classe = 0; classe < w.length; classe++) {
			numerateur[classe] = numerateur[classe] / denominateur;					// On stocke la probabilté d'appartenir															        
		}																			// à chaque classe c
		return numerateur;
	}
	

	/**
    Met à jour tout les poids de w.
    @param data Le tableau 2D (double) contenant les pixels des images.
    @param w Le tableau 2D (double) contenant les poids du perceptron.
    @param yProba Le tableau 2D (double) contenant les probabilités d'appartenance à une classe.
    @param refs Le tableau 2D (int) contenant les bonnes références de chaque image.
    @param image L'indice (int) de l'image.
	*/
	public static void update(double[][] data, double[][] w, double[] yProba, int[] refs, int image) {
		for (int classe = 0; classe < w.length; classe++) {								 //On parcout les classes
			for (int pixel = 0; pixel < w[0].length; pixel++) {							 //On parcout les pixels
				if (refs[image] == classe + 1) {
					w[classe][pixel] += eta * (1 - yProba[classe]) * data[image][pixel]; // Cas où la classe correspond 
																						 // à la bonne classe de l'image
				} else {
					w[classe][pixel] += eta * (0 - yProba[classe]) * data[image][pixel]; // Cas où la classe ne correspond
																						 // à la bonne classe de l'image
				}
			}
		}
	}

	
	/**
    Mesure le nombre d'image mal classé en une vérification des images (tout en corrigeant w).
    @param data Le tableau 2D (double) contenant les pixels des images.
    @param w Le tableau 2D (double) contenant les poids du perceptron.
    @param refs Le tableau 2D (int) contenant les bonnes références de chaque image.
	@return Le nombre d'erreur (int) autrement dit le nombre d'image mal classé
	*/
	public static int epoque(double[][] data, double[][] w, int[] refs) {
		int nbErr = 0;
		double[] yProba = new double[w.length];
		for (int image = 0; image < data.length; image++) { // On parcourt les images
			yProba = probSoftmax(data, w, image);           // On stocke le tableau Softmax pour l'update et l'indice yMax
			int yMax = argumentMax(yProba);					// On stocke l'indice yMax
			if (yMax != refs[image]) {						// Si une image est mal classée alors on fait une mise à jour
				update(data, w, yProba, refs, image);
				nbErr += 1;									// On itére le nombre d'erreur
			}
		}
		return nbErr;
	}
	
	
	/**
    On effectue la phase d'apprentissage du perceptron.
    @param data Le tableau 2D (double) contenant les pixels des images.
    @param w Le tableau 2D (double) contenant les poids du perceptron.
    @param refs Le tableau 2D (int) contenant les bonnes références de chaque image.
    @param itMax Le nombre d'ittération maximal.
	*/
	public static void perceptron(double[][] data, double[][] w, int[] refs, int itMax) {
		System.err.println("/* Lancement de l'apprentisage... ");
		int it = 0;
		int NbErr = 1;
		while (NbErr > 0 && it < itMax) {								// Si le nombre d'erreur est nul ou on arrive à
																		// itMax on arrète l'apprentissage
			NbErr = Cerveau.epoque(data, w, refs);						// On lance une epoque
			if((int) ((float) (it) / (float) (itMax) * 100) % 25 == 0) {
				System.err.println(" * L'apprentissage est à " + (int) ((float) (it) / (float) (itMax) * 100) + "% ..."); // Affichage sur le terminale pour savoir où en est l'apprentissage
			}
			it = it + 1;
		}
		System.err.println(" * L'apprentissage est terminé\n * Les " + itMax + " ittérations ont été éffectuées. */");
	}

	/**
    On calcul et affiche le pourcentage de reussite pour ensemble de donnée (image).
    @param data Le tableau 2D (double) contenant les pixels des images.
    @param w Le tableau 2D (double) contenant les poids du perceptron.
    @param refs Le tableau 2D (int) contenant les bonnes références de chaque image.
	*/
	public static void perceptronTest(double[][] data, double[][] w, int[] refs) {
		double nbErr = 0;
		double[] yProba = new double[w.length];
		for (int image = 0; image < data.length; image++) { // On parcourt les images
			yProba = probSoftmax(data, w, image);			// On stocke le tableau Softmax pour l'update et l'indice yMax
			int yMax = argumentMax(yProba);					// On stocke l'indice yMax
			if (yMax != refs[image]) {						// Si une image est mal classée alors on fait une mise à jour
				nbErr += 1;									// On itére le nombre d'erreur
			}
		}
		double pourcentage = 100 - nbErr / data.length * 100; // On calcul le pourcentage de reussite
		System.err.println("/* Il y a " + (float)(pourcentage) + "% de reussite. */");
	}
}

