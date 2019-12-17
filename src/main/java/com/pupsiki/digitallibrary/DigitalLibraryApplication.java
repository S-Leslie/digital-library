package com.pupsiki.digitallibrary;

import com.github.javafaker.Faker;
import com.pupsiki.digitallibrary.models.Book;
import com.pupsiki.digitallibrary.models.Category;
import com.pupsiki.digitallibrary.repositories.BookRepository;
import com.pupsiki.digitallibrary.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import java.util.HashSet;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

@SpringBootApplication
@EnableCaching
public class DigitalLibraryApplication {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    public static void main(String[] args) {
        SpringApplication.run(DigitalLibraryApplication.class, args);
    }

    @Bean
    public void seedCategories() {
        if (categoryRepository.findAllByOrderByName().size() > 0) {
            return;
        }

        System.out.println("START SEEDING CATEGORIES!");

        Faker faker = new Faker(new Locale("ru"));
        Set<String> uniqueCategories = new HashSet<>();
        while (uniqueCategories.size() < 20) {
            String category = faker.book().genre();
            uniqueCategories.add(category);
        }

        for (String uniqueCategory : uniqueCategories) {
            categoryRepository.save(new Category(
                    uniqueCategory
            ));
        }

        System.out.println("END SEEDING CATEGORIES!");
    }

    @Bean
    public void seedBooks() {
        if (bookRepository.findAll().size() > 0) {
            return;
        }

        int booksCount = 95000;
        int freeBooksCount = 5000;
        System.out.println("START SEEDING BOOKS!");

        Faker faker = new Faker(new Locale("ru"));
        for (int i = 0; i < booksCount; i++) {
            System.out.println(i + "/" + booksCount);
            bookRepository.save(new Book(
                    faker.book().title(),
                    "book" + getRandomNumberInRange(1, 23),
                    faker.book().author(),
                    faker.book().publisher(),
                    faker.book().genre(),
                    faker.lorem().paragraph(10),
                    faker.number().numberBetween(1900, 2019),
                    faker.number().numberBetween(0, 2000),
                    (float) faker.number().randomDouble(1, 0, 5)
            ));
        }

        for (int i = 0; i < freeBooksCount; i++) {
            System.out.println(i + "/" + freeBooksCount);
            bookRepository.save(new Book(
                    faker.book().title(),
                    "book" + getRandomNumberInRange(1, 23),
                    faker.book().author(),
                    faker.book().publisher(),
                    faker.book().genre(),
                    faker.lorem().paragraph(10),
                    faker.number().numberBetween(1900, 2019),
                    0,
                    (float) faker.number().randomDouble(1, 0, 5)
            ));
        }

        System.out.println("END SEEDING BOOKS!");
    }

    private static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
}
