
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Interpolation {

	public String filename;
	public double[][] dividedDifferenceTable;
	public ArrayList<Double> Yresult = new ArrayList<Double>();

	public Interpolation(String filename) {
		this.filename = filename;
	}

	public void createTable() {
		int num = 0;
		String[] x = new String[10];
		String[] y = new String[10];
		FileInputStream fstream = null;
		try {
			fstream = new FileInputStream(this.filename);
		} catch (FileNotFoundException err) {
			System.out.println("The file does not exist");
		}
		DataInputStream dis = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(dis));
		try {
			num = Integer.parseInt(br.readLine());
			for (int i = 0; i < num; i++) {
				String[] temp = br.readLine().split("\\s+");
				x[i] = temp[0];
				y[i] = temp[1];
			}
		}
		catch (IOException io) {
			System.out.println("There is nothing to read");
		}
		dividedDifferenceTable = new double[num][num + 1];
		for (int i = 0; i < num; ++i) {
			dividedDifferenceTable[i][0] = Double.parseDouble(x[i]);
		}
		for (int i = 0; i < num; ++i) {
			dividedDifferenceTable[i][1] = Double.parseDouble(y[i]);
		}
	}
	static float repeat(int i, float value, double x[]) {
		float rep = 1;
		for (int j = 0; j < i; j++) {
			rep = (float) (rep * (value - x[j]));
		}
		return rep;
	}

	public void divideDifference() {
		int n = dividedDifferenceTable[0].length;
		for (int j = 2; j < n; j++) {
			for (int i = 0; i < n - j; i++) {
				dividedDifferenceTable[i][j] = (dividedDifferenceTable[i + 1][j - 1] - dividedDifferenceTable[i][j - 1]) / (dividedDifferenceTable[i + (j - 1)][0] - dividedDifferenceTable[i][0]);
			}
		}

		for (int i = 1; i < dividedDifferenceTable[0].length; ++i) {
			Yresult.add(dividedDifferenceTable[0][i]);
		}
	}

	public void interpolation() {
		ArrayList<String> inter = new ArrayList<String>();

		String s = "";


		for (int i = 0; i < this.dividedDifferenceTable.length - 1; ++i) {

			double xnum = this.dividedDifferenceTable[i][0];

			if (xnum < 0) {
				s = "+";
			} else if (xnum > 0) {
				s = "-";
			}
			if (round(xnum) == 0) {
				inter.add("(x)");
			} else {
				inter.add(String.format("(x%s%.3f)", s, xnum));
			}
		}

		String interpolationPolynomial = String.format("%.3f", Yresult.get(0));



		for (int i = 1; i < inter.size() + 1; ++i) {
			double ynum = Yresult.get(i);
			if (ynum != 0) {
				if (ynum > 0) {
					s = "+";
				} else {
					s = "-";
				}
				String add = "";
				for (int j = 0; j < i; ++j) {
					add += inter.get(j);
				}
				interpolationPolynomial += String.format(" %s %.3f%s", s, Math.abs(ynum), add);
			}
		}
		System.out.println(" ");
		System.out.println("\nThe interpolating polynomial is: " + interpolationPolynomial);


	}

	public void Approx(Float value) {
		double[] xarray = new double[10];
		double[] yarray = new double[10];

		for (int i = 0; i < this.dividedDifferenceTable.length; ++i) {
			xarray[i] = dividedDifferenceTable[i][0];

		}
		for (int i = 0; i < dividedDifferenceTable[0].length - 1; i++) {
			yarray[i] = Yresult.get(i);

		}
		double sum = Yresult.get(0);

		for (int i = 1; i < dividedDifferenceTable[0].length - 1; i++) {
			double temp = Yresult.get(i);

			sum = (sum + repeat(i, value, xarray) * temp);
		}

		System.out.println("Answer:" + sum);

	}

	private double round(double value) {
		return (double) Math.round(value * 1000) / 1000;
	}

	public static void main(String[] args) {
		Interpolation interpolation = new Interpolation("input.txt");
		interpolation.createTable();
		interpolation.divideDifference();
		int entry = 0;
		do {
			System.out.println("1- Show the polynomial");
			System.out.println("2- Approximate the polynomial");
			System.out.println("3- Exit");
			Scanner myInput = new Scanner(System.in);
			entry = myInput.nextInt();

			if (entry == 1) {
				interpolation.interpolation();
			} else if (entry == 2) {
				float value = 0;
				System.out.println("Insert a point for approvimation:");
				value = myInput.nextFloat();
				interpolation.Approx(value);
			}
			;

		} while (entry != 3);

	}
}