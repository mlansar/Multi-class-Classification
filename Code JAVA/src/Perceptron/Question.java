package Perceptron;

import java.io.File;
import java.io.FileWriter;

public class Question {

	/**
    Enregistre une image en format pgm d'un neuronne d'une classe.
    @param tab Un tableau (Double).
    @param classe Un entier (int) qui est le numéro de la classe associé.
	*/
	public static void impressionClasse(double[] tab,int classe) {
		try{
			File ff=new File("\\Chemin"+ Integer.toString(classe+1) +".pgm"); // définir l'arborescence
			ff.createNewFile();
			FileWriter ffw=new FileWriter(ff);
			ffw.write("P2");
			ffw.write ("\r\n");
			ffw.write("28 28");
			ffw.write ("\r\n");
			double Min = 0;
			double Max = 0;
			for(int i = 1 ; i < tab.length ; i++ ) {
				if(tab[i] < Min) {
					Min = tab[i];
				}
				if(tab[i] > Max) {
					Max = tab[i];
				}
			}
			for(int i = 1 ; i < tab.length ; i++ ) {
				tab[i] = tab[i] + Math.abs(Min);
			}
			ffw.write(Integer.toString((int)(Max)));
			ffw.write ("\r\n");
			for(int i = 0 ; i < 28 ; i++) {
				for(int j = 0 ; j < 28 ; j++) {
					if(i != 0 && j != 0)ffw.write(Integer.toString((int)(tab[j+i*28])) + " ");
				}
				ffw.write ("\r\n");
			}
			ffw.close();
			} catch (Exception e) {}
	}
	
	/**
    On effectue la phase d'apprentissage du perceptron en enregistrant le nombre d'erreur 
    en fonction de l'epoque dans un fichier.
    @param data Le tableau 2D (double) contenant les pixels des images.
    @param w Le tableau 2D (double) contenant les poids du perceptron.
    @param refs Le tableau 2D (int) contenant les bonnes références de chaque image.
    @param itMax Le nombre d'ittération maximal.
	*/
	public static void ImpressionErreur(double[][] data, double[][] w, int[] refs, int itMax) {
		try{
			File ff=new File("\\Chemin" + "ErreurEntrainement.txt");
			ff.createNewFile();
			FileWriter fw=new FileWriter(ff);
			fw.write("Nombre d'époque" + "\t" + "Nombre d'image mal classé" + "\r\n");
			int it = 0;
			int NbErr = 1;
			while (NbErr > 0 && it < itMax) {
				NbErr = Perceptron.Cerveau.epoque(data, w, refs);						
				it = it + 1;
				fw.write(it + "\t" + NbErr + "\r\n");
			}
			fw.close();
			} catch (Exception e) {}
	}
	
	/**
    On enregistre dans un fichier texte le nombre d'erreur par classe.
    @param data Le tableau 2D (double) contenant les pixels des images.
    @param w Le tableau 2D (double) contenant les poids du perceptron.
    @param refs Le tableau 2D (int) contenant les bonnes références de chaque image.
	*/
	public static void reperageErreur(double[][] data, double[][] w, int[] refs) {
		int[] NbImageClass = new int[101];
		double[] yProba = new double[w.length];
		for (int image = 0; image < data.length; image++) { 
			yProba = Perceptron.Cerveau.probSoftmax(data, w, image);			
			int yMax = Perceptron.Cerveau.argumentMax(yProba);					
			if (yMax != refs[image]) {
				NbImageClass[refs[image]-1] += 1;
			}
		}
		int[] Moyenne = new int[101];
		for(int n = 0; n < 101 ; n++) {
			Moyenne[n] = NbImageClass[n];
		}
		try{
			File ff=new File("\\Chemin" + "ClasseDifficileTest.txt");
			ff.createNewFile();
			FileWriter fw=new FileWriter(ff);
			fw.write("Classe" + "\t" + "Part de mauvaise réponse" + "\r\n");
			for(int i = 0 ; i < Moyenne.length ; i++ ) {
				fw.write(i+1 + "\t" + Moyenne[i] + "\r\n");
			}
			fw.close();
			} catch (Exception e) {}
	}
}


