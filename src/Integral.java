import java.text.DecimalFormat;
import java.util.Scanner;

// f(x) = 3x^3 + 2x - 5; 2 <= x <= 3
public class Integral {

    // Method untuk mendapatkan nilai dari kuadrat dan koefisien setiap suku
    static void getTiapSuku(String integral, int koefisien[], int kuadrat[], String suku[]) {
        for (int i = 0; i < suku.length; i++) {
            if (suku[i].contains("x^")) {
                String jenis[] = suku[i].split("x\\^");
                kuadrat[i] = Integer.parseInt(jenis[1]);
                koefisien[i] = jenis[0] != "" ? Integer.parseInt(jenis[0]) : 1;
            } else if (suku[i].contains("x")) {
                String jenis[] = suku[i].split("x");
                kuadrat[i] = 1;
                koefisien[i] = jenis.length > 0 ? Integer.parseInt(jenis[0]) : 1;
            } else {
                kuadrat[i] = 0;
                koefisien[i] = Integer.parseInt(suku[i]);
            }
        }
    }

    // Method untuk mendapatkan list operator pada fungsi polinomial
    static char[] getOperator(String integral, int length) {
        int indexOperator = 0;
        char operator[] = new char[length];
        for (int i = 0; i < integral.length(); i++) {
            if (integral.charAt(i) == '+' || integral.charAt(i) == '-') {
                operator[indexOperator] = integral.charAt(i);
                indexOperator++;
            }
        }

        return operator;
    }

    // Integral untuk fungsi polinomial
    static double integralTertentu(String integral, int interval[]) {
        String suku[] = integral.split("\s+(\\+|-)\s+");
        int kuadrat[] = new int[suku.length];
        int koefisien[] = new int[suku.length];

        getTiapSuku(integral, koefisien, kuadrat, suku);

        // mendapatkan operator dari fungsi yang diberikan
        char operator[] = getOperator(integral, suku.length - 1);

        // proses integral
        double new_kuadrat[] = new double[kuadrat.length];
        double new_koefisien[] = new double[koefisien.length];

        for (int i = 0; i < kuadrat.length; i++) {
            new_kuadrat[i] = kuadrat[i] + 1;
            new_koefisien[i] = (double) koefisien[i] / (kuadrat[i] + 1);
        }

        // subtitusi
        double subtitusi[] = new double[2];
        for (int i = 0; i < interval.length; i++) {
            double sum = 0;
            int indexOper = 0;
            for (int j = 0; j < new_kuadrat.length; j++) {
                double hasil = Math.pow(interval[i], new_kuadrat[j]) * new_koefisien[j];
                if (j == 0) {
                    sum = hasil;
                } else if (j > 0) {
                    if (operator[indexOper] == '+')
                        sum += hasil;
                    else
                        sum -= hasil;
                    indexOper++;
                }
            }

            subtitusi[i] = sum;
        }

        return subtitusi[0] - subtitusi[1];
    }

    // Method Hampiran Ujung Kiri
    static double ujungKiri(double deltaX, String integral, int interval[]) {
        String suku[] = integral.split("\s+(\\+|-)\s+");
        int kuadrat[] = new int[suku.length];
        int koefisien[] = new int[suku.length];
        char operator[] = getOperator(integral, suku.length - 1);

        getTiapSuku(integral, koefisien, kuadrat, suku);

        double hasil = 0;

        for (double i = interval[1]; i < interval[0]; i += deltaX) {
            double tinggi = 0;
            int indexOper = 0;

            // subtitusi fungsi polinom dengan x dari nilai deltaX
            for (int j = 0; j < kuadrat.length; j++) {
                double suku_x = Math.pow(i, kuadrat[j]) * koefisien[j];
                if (j == 0) {
                    tinggi = suku_x;
                } else if (j > 0) {
                    if (operator[indexOper] == '+')
                        tinggi += suku_x;
                    else
                        tinggi -= suku_x;
                    indexOper++;
                }
            }

            hasil += tinggi * deltaX;
        }

        return hasil;
    }

    // Method Hampiran Ujung Kanan
    static double ujungKanan(double deltaX, String integral, int interval[]) {
        String suku[] = integral.split("\s+(\\+|-)\s+");
        int kuadrat[] = new int[suku.length];
        int koefisien[] = new int[suku.length];
        char operator[] = getOperator(integral, suku.length - 1);

        getTiapSuku(integral, koefisien, kuadrat, suku);

        double hasil = 0;

        for (double i = interval[1] + deltaX; i <= interval[0] + 0.00001; i += deltaX) {
            double tinggi = 0;
            int indexOper = 0;

            // subtitusi fungsi polinom dengan x dari nilai deltaX
            for (int j = 0; j < kuadrat.length; j++) {
                double suku_x = Math.pow(i, kuadrat[j]) * koefisien[j];
                if (j == 0) {
                    tinggi = suku_x;
                } else if (j > 0) {
                    if (operator[indexOper] == '+')
                        tinggi += suku_x;
                    else
                        tinggi -= suku_x;
                    indexOper++;
                }
            }

            hasil += tinggi * deltaX;
        }

        return hasil;
    }

    // Method Hampiran Titik Tengah
    static double titikTengah(double deltaX, String integral, int interval[]) {
        String suku[] = integral.split("\s+(\\+|-)\s+");
        int kuadrat[] = new int[suku.length];
        int koefisien[] = new int[suku.length];
        char operator[] = getOperator(integral, suku.length - 1);

        getTiapSuku(integral, koefisien, kuadrat, suku);

        double hasil = 0;

        for (double i = (double) interval[1]; i <= interval[0]; i += deltaX) {

            double x = (i + (i + deltaX)) / 2;

            double tinggi = 0;
            int indexOper = 0;

            // subtitusi fungsi polinom dengan x dari nilai deltaX
            for (int j = 0; j < kuadrat.length; j++) {
                double suku_x = Math.pow(x, kuadrat[j]) * koefisien[j];
                if (j == 0) {
                    tinggi = suku_x;
                } else if (j > 0) {
                    if (operator[indexOper] == '+')
                        tinggi += suku_x;
                    else
                        tinggi -= suku_x;
                    indexOper++;
                }
            }

            hasil += tinggi * deltaX;
        }

        return hasil;
    }

    public static void main(String[] args) {
        Scanner data = new Scanner(System.in);

        String integral;
        int interval[] = new int[2];
        int n;

        System.out.print("Beri nilai integral : ");
        integral = data.nextLine();

        System.out.println(" ");

        do {
            System.out.print("Beri nilai untuk batas atas : ");
            interval[0] = data.nextInt();

            System.out.print("Beri nilai untuk batas bawah : ");
            interval[1] = data.nextInt();

            if (interval[0] < interval[1]) {
                System.out.println("\nBatas 1 harus lebih besar daripada batas 2\n");
            }
        } while (interval[0] < interval[1]);

        System.out.print("Beri nilai n : ");
        n = data.nextInt();

        data.close();

        double deltaX = (double) (interval[0] - interval[1]) / n; // besar perubahan

        System.out.println(" ");

        double integralTentu = integralTertentu(integral, interval);
        double ujungKiri = ujungKiri(deltaX, integral, interval);
        double ujungKanan = ujungKanan(deltaX, integral, interval);
        double titikTengah = titikTengah(deltaX, integral, interval);

        double diffA = Math.abs(integralTentu - ujungKiri);
        double diffB = Math.abs(integralTentu - ujungKanan);
        double diffC = Math.abs(integralTentu - titikTengah);

        String intTentu = new DecimalFormat("#.##").format(integralTentu);
        String ujung_kiri = new DecimalFormat("#.##").format(ujungKiri);
        String ujung_kanan = new DecimalFormat("#.##").format(ujungKanan);
        String titik_tengah = new DecimalFormat("#.##").format(titikTengah);

        String errUjungKiri = new DecimalFormat("#.##").format(diffA);
        String errUjungKanan = new DecimalFormat("#.##").format(diffB);
        String errTitikTengah = new DecimalFormat("#.##").format(diffC);

        System.out.println("Hasil dari integral tertentu : " + intTentu + "\n");

        System.out.println("Hasil dari ujung kiri : " + ujung_kiri +
                "\nError : " + errUjungKiri + "\n");

        System.out.println("Hasil dari ujung kanan : " + ujung_kanan +
                "\nError : " + errUjungKanan + "\n");

        System.out.println("Hasil dari titik tengah : " + titik_tengah +
                "\nError : " + errTitikTengah);

        double pendekatan = Math.min(Math.min(diffA, diffB), diffC);

        System.out.print("\nNilai yang paling mendekati Nilai Eksak adalah : Nilai ");
        if (pendekatan == diffA)
            System.out.print("Ujung Kiri");
        else if (pendekatan == diffB)
            System.out.print("Ujung Kanan");
        else
            System.out.print("Titik Tengah");
    }
}