import java.util.*;

class SortowaniePrzezWstawianie<T> {
    public void sortuj(List<T> lista, Comparator<T> porównywacz) {
        for (int i = 1; i < lista.size(); i++) {
            T klucz = lista.get(i);
            int j = i - 1;
            while (j >= 0 && porównywacz.compare(lista.get(j), klucz) > 0) {
                lista.set(j + 1, lista.get(j));
                j--;
            }
            lista.set(j + 1, klucz);
        }
    }
}

class SortowanieSzybkie {
    public void sortuj(List<Double> lista) {
        Stack<Integer> stos = new Stack<>();
        stos.push(0);
        stos.push(lista.size() - 1);

        while (!stos.isEmpty()) {
            int prawy = stos.pop();
            int lewy = stos.pop();

            if (lewy < prawy) {
                int pi = podziel(lista, lewy, prawy);

                // Dodajemy nowe zakresy do stosu
                if (pi - 1 > lewy) {
                    stos.push(lewy);
                    stos.push(pi - 1);
                }
                if (pi + 1 < prawy) {
                    stos.push(pi + 1);
                    stos.push(prawy);
                }
            }
        }
    }

    private int podziel(List<Double> lista, int lewy, int prawy) {
        double pivot = lista.get(prawy);
        int i = lewy - 1;

        for (int j = lewy; j < prawy; j++) {
            if (lista.get(j) <= pivot) {
                i++;
                Collections.swap(lista, i, j);
            }
        }
        Collections.swap(lista, i + 1, prawy);
        return i + 1;
    }
}


class Wezel {
    int wartosc;
    Wezel lewy, prawy;

    Wezel(int wartosc) {
        this.wartosc = wartosc;
        lewy = prawy = null;
    }
}

class Drzewo {
    Wezel korzen;

    public void wstaw(int wartosc) {
        if (korzen == null) {
            korzen = new Wezel(wartosc);
            return;
        }
        Wezel aktualny = korzen;
        while (true) {
            if (wartosc < aktualny.wartosc) {
                if (aktualny.lewy == null) {
                    aktualny.lewy = new Wezel(wartosc);
                    break;
                }
                aktualny = aktualny.lewy;
            } else if (wartosc > aktualny.wartosc) {
                if (aktualny.prawy == null) {
                    aktualny.prawy = new Wezel(wartosc);
                    break;
                }
                aktualny = aktualny.prawy;
            } else {
                break;
            }
        }
    }

    public void inorder() {
        Stack<Wezel> stos = new Stack<>();
        Wezel aktualny = korzen;

        while (aktualny != null || !stos.isEmpty()) {
            while (aktualny != null) {
                stos.push(aktualny);
                aktualny = aktualny.lewy;
            }
            aktualny = stos.pop();
            System.out.print(aktualny.wartosc + " ");
            aktualny = aktualny.prawy;
        }
    }

    public void preorder() {
        if (korzen == null) return;
        Stack<Wezel> stos = new Stack<>();
        stos.push(korzen);

        while (!stos.isEmpty()) {
            Wezel aktualny = stos.pop();
            System.out.print(aktualny.wartosc + " ");

            if (aktualny.prawy != null) stos.push(aktualny.prawy);
            if (aktualny.lewy != null) stos.push(aktualny.lewy);
        }
    }

    public void postorder() {
        if (korzen == null) return;
        Stack<Wezel> stos1 = new Stack<>();
        Stack<Wezel> stos2 = new Stack<>();
        stos1.push(korzen);

        while (!stos1.isEmpty()) {
            Wezel aktualny = stos1.pop();
            stos2.push(aktualny);
            if (aktualny.lewy != null) stos1.push(aktualny.lewy);
            if (aktualny.prawy != null) stos1.push(aktualny.prawy);
        }

        while (!stos2.isEmpty()) {
            System.out.print(stos2.pop().wartosc + " ");
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Wybierz opcję:");
            System.out.println("1. Sortowanie przez wstawianie");
            System.out.println("2. Sortowanie szybkie");
            System.out.println("3. Drzewo binarne");
            System.out.println("4. Wyjście");
            int opcja = scanner.nextInt();
            scanner.nextLine();

            switch (opcja) {
                case 1:
                    sortowaniePrzezWstawianie(scanner);
                    break;
                case 2:
                    sortowanieSzybkie(scanner);
                    break;
                case 3:
                    drzewoBinarne(scanner);
                    break;
                case 4:
                    System.exit(0);
                default:
                    System.out.println("Nieprawidłowa opcja.");
            }
        }
    }

    private static void sortowaniePrzezWstawianie(Scanner scanner) {
        SortowaniePrzezWstawianie<Object> sorter = new SortowaniePrzezWstawianie<>();
        System.out.println("Podaj typ danych (int, string, date):");
        String typ = scanner.nextLine();

        System.out.println("Podaj wartości oddzielone spacją:");
        List<Object> lista = new ArrayList<>();
        String[] dane = scanner.nextLine().split(" ");

        switch (typ) {
            case "int":
                for (String s : dane) lista.add(Integer.parseInt(s));
                break;
            case "string":
                lista.addAll(Arrays.asList(dane));
                break;
            case "date":
                for (String s : dane) {
                    try {
                        lista.add(new Date(Long.parseLong(s)));
                    } catch (Exception e) {
                        System.out.println("Nieprawidłowy format daty.");
                        return;
                    }
                }
                break;
            default:
                System.out.println("Nieobsługiwany typ.");
                return;
        }

        System.out.println("Wybierz porządek sortowania (rosnąco/malejąco):");
        String porzadek = scanner.nextLine();
        Comparator<Object> comparator = (o1, o2) -> ((Comparable) o1).compareTo(o2);
        if (porzadek.equals("malejąco")) {
            comparator = comparator.reversed();
        }

        sorter.sortuj(lista, comparator);
        System.out.println("Posortowana lista: " + lista);
    }

    private static void sortowanieSzybkie(Scanner scanner) {
        SortowanieSzybkie sorter = new SortowanieSzybkie();
        System.out.println("Podaj liczby rzeczywiste oddzielone spacją:");
        String[] dane = scanner.nextLine().split(" ");
        List<Double> lista = new ArrayList<>();

        for (String s : dane) {
            try {
                lista.add(Double.parseDouble(s));
            } catch (NumberFormatException e) {
                System.out.println("Nieprawidłowa liczba.");
                return;
            }
        }

        sorter.sortuj(lista);
        System.out.println("Posortowana lista: " + lista);
    }

    private static void drzewoBinarne(Scanner scanner) {
        Drzewo drzewo = new Drzewo();
        System.out.println("Podaj liczby do drzewa oddzielone spacją:");
        String[] dane = scanner.nextLine().split(" ");
        for (String s : dane) {
            try {
                drzewo.wstaw(Integer.parseInt(s));
            } catch (NumberFormatException e) {
                System.out.println("Nieprawidłowa liczba.");
                return;
            }
        }

        System.out.println("Wybierz metodę przeglądania drzewa (inorder, preorder, postorder):");
        String metoda = scanner.nextLine();

        switch (metoda) {
            case "inorder":
                drzewo.inorder();
                break;
            case "preorder":
                drzewo.preorder();
                break;
            case "postorder":
                drzewo.postorder();
                break;
            default:
                System.out.println("Nieprawidłowa metoda.");
        }
        System.out.println();
    }
}