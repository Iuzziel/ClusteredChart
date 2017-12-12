package graph;

/**
 * Classe qui cree un Objet qui servira a remplir les categorieDataset dans le
 * Clustered Chart.
 */
public class AppCatDatasetObject {
	private double value;
	private String serie;
	private String categorie;

	/**
	 * Objet qui servira a remplir les categorieDataset dans les Charts.
	 * 
	 * @param value
	 *            Y
	 * @param serie
	 *            X
	 * @param categorie
	 *            Categorie si plusieurs Y sur le meme X. ex: si serie est une
	 *            date, categorie sera definie par la machine, on aura ainsi
	 *            pour la meme date les deux valeurs cote a cote.
	 */
	public AppCatDatasetObject(double value, String serie, String categorie) {
		this.value = value;
		this.serie = serie;
		this.categorie = categorie;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public String getCategorie() {
		return categorie;
	}

	public void setCategorie(String categorie) {
		this.categorie = categorie;
	}
}
