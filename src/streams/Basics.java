package streams;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Basics {
    public static void main(String[] args) {

        List<String> list = List.of("apple", "banana", "Cherry", "date");
        System.out.println(list.stream().map(String::toUpperCase).collect(Collectors.toList()));

        //Remove null or empty strings from a list --> List.of does not accept nulls, so be careful
        List<String> rmvNull = Arrays.asList("apple", "", null, "banana", " ", "cherry", null);
        System.out.println(rmvNull.stream().filter(Objects::nonNull).map(String::trim).filter(s -> !s.isEmpty())
                .collect(Collectors.toList()));//forgot to add map(String::trim)

        //Find min and max
        List<Integer> integerList = List.of(12, 5, 8, 19, 1, 33, 7);
        System.out.println(integerList.stream().min(Comparator.naturalOrder()).get());
        System.out.println(integerList.stream().max(Comparator.comparingInt(Integer::intValue)).get());

        //Remove duplicates
        List<Integer> dupes = List.of(10, 20, 10, 30, 20, 40, 30, 42, 89, 76, 12);
        System.out.println(dupes.stream().distinct().collect(Collectors.toList()));

        //Find dupes
        Set<Integer> set = new HashSet<>();
        System.out.println(dupes.stream().filter(num -> !set.add(num)).collect(Collectors.toList()));

        //find element appeared only once
        System.out.println(dupes.stream().filter(num -> Collections.frequency(dupes, num)==1).collect(Collectors.toList()));

        //Sum of all numbers in a list
        System.out.println(dupes.stream().mapToInt(Integer::intValue).sum());

        //Check if any number > 10 exists in list
        System.out.println(dupes.stream().anyMatch(num -> num > 10));

        //Partition numbers into even and odd
        System.out.println(dupes.stream().collect(Collectors.partitioningBy(num -> num%2==0)));

        //count frequency of each word
        List<String> words = List.of("apple", "banana", "apple", "orange", "banana", "mango","apple");
        System.out.println(words.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting())));

        //Group strings by their length
        System.out.println(words.stream().collect(Collectors.groupingBy(String::length)));

        //Get distinct characters from a list of strings
        System.out.println(words.stream().flatMap(s -> s.chars().mapToObj(i -> (char)i))
                .distinct().collect(Collectors.toList()));

        //Convert List<String> to Map<String, Integer> where value is string length
        System.out.println(words.stream().distinct().collect(Collectors.toMap(Function.identity(), String::length)));

        //Check if list contains only unique values
        Set<Integer> integerSet = new HashSet<>();
        List<Integer> nums = List.of(1, 2, 3, 4, 5, 6, 2);
        for(Integer n : nums)
            integerSet.add(n);
        System.out.println(integerSet.size() == nums.size());

        //Convert List<Integer> to comma-separated String

        //Find common elements between two lists
        List<String> list1 = List.of("apple", "banana", "cherry", "date");
        List<String> list2 = List.of("banana", "date", "fig", "grape");
        System.out.println(list1.stream().filter(lst -> list2.contains(lst)).collect(Collectors.toList()));

        //Flatten nested List<List<Integer>> to List<Integer>
        List<List<Integer>> nestedList = List.of(
                List.of(1, 2, 3),
                List.of(4, 5),
                List.of(6, 7, 8, 9, 10, 11)
        );
        System.out.println(nestedList.stream().flatMap(List::stream).collect(Collectors.toList()));

        List<List<String>> nestedListStr = List.of(
                List.of("Ram","Sita","Ravan","Meghanath"),
                List.of("Ravan","Shiva",""),
                List.of("Bhrama","Shiva","Vishnu")
        );
        System.out.println(nestedListStr.stream().flatMap(List::stream).collect(Collectors.toList()));
        List<String> stringList = nestedListStr.stream().flatMap(Collection::stream).collect(Collectors.toList());
        System.out.println(stringList.stream().filter(s -> Collections.frequency(stringList, s) > 1).distinct()
                .collect(Collectors.toList()));

        //Filter out all strings that start with a specific letter
        List<Integer> numbers = List.of(123, 245, 345, 456, 129, 198, 235);
        int startingDigit = 1;
        System.out.println(numbers.stream().map(String::valueOf).filter(s -> s.startsWith(""+startingDigit))
                .collect(Collectors.toList()));

        List<String> words1 = List.of("apple", "banana", "avocado", "blueberry", "apricot");
        char filterChar = 'a';
        System.out.println(words1.stream().filter(a -> a.startsWith(""+filterChar)).collect(Collectors.toList()));

        //Create histogram (word → count) from paragraph
        String paragraph = "Java is great. Java is fast. Java is everywhere!";
        List<String> paraList = Arrays.stream(paragraph.split("\\s+")).map(s -> s.replaceAll("[^a-zA-Z0-9]","")).collect(Collectors.toList());
        Map<String, Long> paraMap = paraList.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        System.out.println(paraMap);

        //Create summary statistics (max, min, avg) from list of numbers
        IntSummaryStatistics summaryStatistics = dupes.stream().collect(Collectors.summarizingInt(Integer::intValue));
        System.out.println(summaryStatistics.getCount());
        System.out.println(summaryStatistics.getMin());
        System.out.println(summaryStatistics.getMax());
        System.out.println(summaryStatistics.getAverage());
        System.out.println(summaryStatistics.getSum());

        //Find longest string in a list
        String maxLen = stringList.stream().max(Comparator.comparingInt(String::length)).get();
        System.out.println(maxLen);

        //Merge multiple maps into one map using stream
        /*
         Step-by-step:
            maps.stream() → stream of maps
            .flatMap(m -> m.entrySet().stream()) → all key-value pairs in one stream
            .collect(toMap(..., ..., mergeFn)) → merge into one map with a rule
         */
        List<Map<String, Integer>> maps = List.of(
                Map.of("apple", 2, "banana", 2),
                Map.of("banana", 3, "cherry", 4),
                Map.of("apple", 5, "date", 6)
        );
        Map<String, Integer> mapsMap = maps.stream().flatMap(map -> map.entrySet().stream()).collect(Collectors.toMap(
                Map.Entry::getKey, Map.Entry::getValue, Integer::sum
        ));
        System.out.println(mapsMap);

        //Find most frequent element in list
        String maxOcc = words.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream().max(Comparator.comparing(Map.Entry::getValue)).map(Map.Entry::getKey).get();
        System.out.println(maxOcc);
    }
}
