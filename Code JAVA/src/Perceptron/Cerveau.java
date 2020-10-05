package Perceptron;

public class Cerveau {

	
	/* On initialise un Eta qui est le pas d'apprentissage.
	 * Apr�s plusieurs test en interne, cette valeur est plutot optimal*/
	static double eta = 0.1;

	
	/**
    Cr�er un nouveau tableau 2D o� l'on rajoute une case, pour chaque ligne, initialis� � 1.
    @param data Un tableau 2D (double).
    @return Le tableau data (double) d'une case plus grande pour chaque ligne o� la premiere valeur est 1.
	*/
	public static double[][] rajoutCase(double[][] data) {
		double[][] newData = new double[data.length][data[0].length + 1];
		for (int i = 0; i < data.length; i++) {
			newData[i][0] = 1;							// On initialise tout les premiers poids � 1
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
    @return La valeur (int) de la classe o� la probabilit� est la plus �lev�e.
    */
	public static int argumentMax(double[] yProbabilite) {
		double probMax = 0;
		int indMax = 0;
		for (int i = 0; i < yProbabilite.length; i++) {
			if (yProbabilite[i] > probMax) {			// On parcourt le tableau de probabilit�
				probMax = yProbabilite[i];				// On stock le maximum
				indMax = i;								// On stocke l'indice
			}
		}
		return indMax + 1;
	}
	
	/**
    Obtenir le tableau de probabilit� qu'une image appartienne � une classe.
    @param data Le tableau 2D (double) contenant les pixels des images.
    @param w Le tableau 2D (double) contenant les poids du perceptron.
    @param image L'indice (int) de l'image.
    @return Un tableau (double) contenant la probabilit� qu'une image appartienne � une classe donn�e.
	*/
	public static double[] probSoftmax(double[][] data,double[][] w, int image) {
		double[] numerateur = new double[w.length];
		double denominateur = 0;
		for (int classe = 0; classe < w.length; classe++) {								
			numerateur[classe] = Math.exp((produitScalaire(w[classe], data[image]))); // On calcul le num�rateur pour chaque
			denominateur += numerateur[classe];										  // image et on calcul le num�rateur
		}
		for (int classe = 0; classe < w.length; classe++) {
			numerateur[classe] = numerateur[classe] / denominateur;					// On stocke la probabilt� d'appartenir															        
		}																			// � chaque classe c
		return numerateur;
	}
	

	/**
    Met � jour tout les poids de w.
    @param data Le tableau 2D (double) contenant les pixels des images.
    @param w Le tableau 2D (double) contenant les poids du perceptron.
    @param yProba Le tableau 2D (double) contenant les probabilit�s d'appartenance � une classe.
    @param refs Le tableau 2D (int) contenant les bonnes r�f�rences de chaque image.
    @param image L'indice (int) de l'image.
	*/
	public static void update(double[][] data, double[][] w, double[] yProba, int[] refs, int image) {
		for (int classe = 0; classe < w.length; classe++) {								 //On parcout les classes
			for (int pixel = 0; pixel < w[0].length; pixel++) {							 //On parcout les pixels
				if (refs[image] == classe + 1) {
					w[classe][pixel] += eta * (1 - yProba[classe]) * data[image][pixel]; // Cas o� la classe correspond 
																						 // � la bonne classe de l'image
				} else {
					w[classe][pixel] += eta * (0 - yProba[classe]) * data[image][pixel]; // Cas o� la classe ne correspond
																						 // � la bonne classe de l'image
				}
			}
		}
	}

	
	/**
    Mesure le nombre d'image mal class� en une v�rification des images (tout en corrigeant w).
    @param data Le tableau 2D (double) contenant les pixels des images.
    @param w Le tableau 2D (double) contenant les poids du perceptron.
    @param refs Le tableau 2D (int) contenant les bonnes r�f�rences de chaque image.
	@return Le nombre d'erreur (int) autrement dit le nombre d'image mal class�
	*/
	public static int epoque(double[][] data, double[][] w, int[] refs) {
		int nbErr = 0;
		double[] yProba = new double[w.length];
		for (int image = 0; image < data.length; image++) { // On parcourt les images
			yProba = probSoftmax(data, w, image);           // On stocke le tableau Softmax pour l'update et l'indice yMax
			int yMax = argumentMax(yProba);					// On stocke l'indice yMax
			if (yMax != refs[image]) {						// Si une image est mal class�e alors on fait une mise � jour
				update(data, w, yProba, refs, image);
				nbErr += 1;									// On it�re le nombre d'erreur
			}
		}
		return nbErr;
	}
	
	
	/**
    On effectue la phase d'apprentissage du perceptron.
    @param data Le tableau 2D (double) contenant les pixels des images.
    @param w Le tableau 2D (double) contenant les poids du perceptron.
    @param refs Le tableau 2D (int) contenant les bonnes r�f�rences de chaque image.
    @param itMax Le nombre d'itt�ration maximal.
	*/
	public static void perceptron(double[][] data, double[][] w, int[] refs, int itMax) {
		System.err.println("/* Lancement de l'apprentisage... ");
		int it = 0;
		int NbErr = 1;
		while (NbErr > 0 && it < itMax) {								// Si le nombre d'erreur est nul ou on arrive �
																		// itMax on arr�te l'apprentissage
			NbErr = Cerveau.epoque(data, w, refs);						// On lance une epoque
			if((int) ((float) (it) / (float) (itMax) * 100) % 25 == 0) {
				System.err.println(" * L'apprentissage est � " + (int) ((float) (it) / (float) (itMax) * 100) + "% ..."); // Affichage sur le terminale pour savoir o� en est l'apprentissage
			}
			it = it + 1;
		}
		System.err.println(" * L'apprentissage est termin�\n * Les " + itMax + " itt�rations ont �t� �ffectu�es. */");
	}

	/**
    On calcul et affiche le pourcentage de reussite pour ensemble de donn�e (image).
    @param data Le tableau 2D (double) contenant les pixels des images.
    @param w Le tableau 2D (double) contenant les poids du perceptron.
    @param refs Le tableau 2D (int) contenant les bonnes r�f�rences de chaque image.
	*/
	public static void perceptronTest(double[][] data, double[][] w, int[] refs) {
		double nbErr = 0;
		double[] yProba = new double[w.length];
		for (int image = 0; image < data.length; image++) { // On parcourt les images
			yProba = probSoftmax(data, w, image);			// On stocke le tableau Softmax pour l'update et l'indice yMax
			int yMax = argumentMax(yProba);					// On stocke l'indice yMax
			if (yMax != refs[image]) {						// Si une image est mal class�e alors on fait une mise � jour
				nbErr += 1;									// On it�re le nombre d'erreur
			}
		}
		double pourcentage = 100 - nbErr / data.length * 100; // On calcul le pourcentage de reussite
		System.err.println("/* Il y a " + (float)(pourcentage) + "% de reussite. */");
	}
}

