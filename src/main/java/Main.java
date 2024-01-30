import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    private static final int CITIZEN_COUNT = 10_000_000;
    private static final int LIMIT_COUNT = 5;

    public static void main(String[] args) {
        List<String> names = Arrays.asList("Jack", "Connor", "Harry", "George", "Samuel", "John");
        List<String> families = Arrays.asList("Evans", "Young", "Harris", "Wilson", "Davies", "Adamson", "Brown");
        Collection<Person> persons = new ArrayList<>();
        for (int i = 0; i < CITIZEN_COUNT; i++) {
            persons.add(new Person(
                    names.get(new Random().nextInt(names.size())),
                    families.get(new Random().nextInt(families.size())),
                    new Random().nextInt(100),
                    Sex.values()[new Random().nextInt(Sex.values().length)],
                    Education.values()[new Random().nextInt(Education.values().length)])
            );
        }
        System.out.printf("ВСЕГО горожан : %,d человек.\n", CITIZEN_COUNT);

        // Найти количество несовершеннолетних (т.е. людей младше 18 лет).
        // Для поиска несовершеннолетних используйте промежуточный метод filter() и терминальный метод count().
        long underageCount = persons.stream()
                .filter(x -> x.getAge() < 18)
                .count();
        System.out.printf("Кол-во НЕСОВЕРШЕННОЛЕТНИХ : %,d\n", underageCount);

        // Получить список фамилий призывников (т.е. мужчин от 18 и до 27 лет).
        // Для получения списка призывников потребуется применить несколько промежуточных методов filter(),
        // а также для преобразования данных из Person в String (так как нужны только фамилии) используйте метод map().
        // Так как требуется получить список List<String> терминальным методом будет collect(Collectors.toList()).
        long recruitCount = persons.stream()
                .filter(x -> x.getSex() == Sex.MAN)
                .filter(x -> x.getAge() >= 18 && x.getAge() < 27)
                .map(Person::getFamily)
                .count();
        List<String> recruits = persons.stream()
                .filter(x -> x.getSex() == Sex.MAN)
                .filter(x -> x.getAge() >= 18 && x.getAge() < 27)
                .map(Person::getFamily)
                .limit(LIMIT_COUNT)
                .collect(Collectors.toList());
        System.out.printf("Кол-во ПРИЗЫВНИКОВ : %,d\n", recruitCount);
        System.out.printf("Список ПРИЗЫВНИКОВ : %s ...и еще %,d мужчин\n", recruits, (recruitCount - LIMIT_COUNT));

        // Получить отсортированный по фамилии список потенциально работоспособных людей с высшим образованием в выборке
        // (т.е. людей с высшим образованием от 18 до 60 лет для женщин и до 65 лет для мужчин).
        // Для получения отсортированного по фамилии списка потенциально работоспособных людей с высшим образованием
        // необходимо применить ряд промежуточных методов filter(), метод sorted() в который нужно будет положить
        // компаратор по фамилиям Comparator.comparing(). Завершить стрим необходимо методом collect().
        long employableCount = persons.stream()
                .filter(x -> x.getEducation() == Education.HIGHER)
                .filter(x -> x.getAge() >= 18)
                .filter(x -> (x.getSex() == Sex.MAN && x.getAge() < 65) || (x.getSex() == Sex.WOMAN && x.getAge() < 60))
                .count();
        List<String> employables = persons.stream()
                .filter(x -> x.getEducation() == Education.HIGHER)
                .filter(x -> x.getAge() >= 18)
                .filter(x -> (x.getSex() == Sex.MAN && x.getAge() < 65) || (x.getSex() == Sex.WOMAN && x.getAge() < 60))
                .sorted(Comparator.comparing(Person::getFamily))
                .map(Person::getFamily)
                .distinct()
                .limit(LIMIT_COUNT)
                .collect(Collectors.toList());
        System.out.printf("Кол-во умных ТРУДОСПОСОБНЫХ : %,d\n", employableCount);
        System.out.printf("Список умных ТРУДОСПОСОБНЫХ : %s ...и еще %,d горожан\n", employables, (employableCount - LIMIT_COUNT));
    }
}
