package Perceptron;

import java.io.FileNotFoundException;
import java.io.IOException;

import caltech.CalTech101;

public class ReconnaissanceImage {

	public static final int DIM = 28 * 28 + 1; /* Dimension de l'espace de representation 
												* ( la dimension de l'image + 1 afin de 
												* prendre en compte le biais)*/
	
	public static double[][] w = new double[101][DIM]; /* tableau contenant tout les w pour
														chaque classe */

	public static void main(String[] args) throws FileNotFoundException, IOException {

		String CaltechCheminFichier = "caltech101.mat";
		CalTech101 CT = new CalTech101(CaltechCheminFichier);

		// charger toutes les donnees d'apprentissage (train)
		double[][] trainIm = new double[4100][28 * 28 + 1];
		int[] trainRefs = new int[4100]; // reference, mais au format "classificaition binaire"
		for (int i = 0; i < 4100; i++) {
			trainRefs[i] = CT.getTrainLabel(i);
			trainIm[i] = CT.getTrainImage(i);
		}

		// Pour les donnees de test, utiliser CT.getTestLabel et CT.getTestImage
		double[][] testIm = new double[2307][28 * 28 + 1];
		int[] testRefs = new int[2307]; // reference, mais au format "classificaition binaire"
		for (int i = 0; i < 2307; i++) {
			testRefs[i] = CT.getTestLabel(i);
			testIm[i] = CT.getTestImage(i);
		}

		// On initialise tout les poids w pour chaque classe à 0
		for (int i = 0; i < w.length; i++) {
			for (int j = 0; j < w[0].length; j++) {
				w[i][j] = 0;
			}
		}

		/* On rajoute une case (ou un pixel) pour toutes les images afin 
		 * de prendre en compte le fait d'avoir un biais en w
		 */
		double[][] newData = Cerveau.rajoutCase(trainIm);
		double[][] newTest = Cerveau.rajoutCase(testIm);
		
		// On choisit un nombre d'itération maximale ou époque (88 est un bon nombre)
		int itMax = 88;
		
		// On lance l'apprentissage du Perceptron
		Cerveau.perceptron(newData, w, trainRefs, itMax);
		//Convert.Convertir.reperageErreur(newdata, w, trainRefs);
		
		//On lance les tests pour observer notre pourcentage de reussite
		System.err.println("On test notre Perceptron pour notre ensemble d'entrainement :");
		Cerveau.perceptronTest(newData, w, trainRefs);
		
		System.err.println("On test notre Perceptron pour notre ensemble de test :");
		Cerveau.perceptronTest(newTest, w, testRefs);
		
		

	}

}
