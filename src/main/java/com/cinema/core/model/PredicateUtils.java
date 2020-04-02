package com.cinema.core.model;

import com.cinema.core.entity.Movie;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class PredicateUtils {

    public static Predicate[] buildPredicates(CriteriaBuilder builder, Root<Movie> root, Filter filter) {
        List<Predicate> predicates = new ArrayList<>();
        if (filter.getGenresIncl() != null) {
            predicates.add(createPredicateGenresIncl(builder, root, filter));
        }
        if (filter.getGenresExcl() != null) {
            predicates.add(createPredicateGenresExcl(builder, root, filter));
        }
        if (filter.getCountriesIncl() != null) {
            predicates.add(createPredicateCountriesIncl(builder, root, filter));
        }
        if (filter.getCountriesExcl() != null) {
            predicates.add(createPredicateCountriesExcl(builder, root, filter));
        }
        if (filter.getFromYear() != null) {
            predicates.add(createPredicateFromYear(builder, root, filter));
        }
        if (filter.getToYear() != null) {
            predicates.add(createPredicateToYear(builder, root, filter));
        }
        if (filter.getMinIMDB() != null) {
            predicates.add(createPredicateMinImdb(builder, root, filter));
        }
        if (filter.getMinKP() != null) {
            predicates.add(createPredicateMinKp(builder, root, filter));
        }
        if (filter.getMinVotes() != null) {
            predicates.add(createPredicateMinVotes(builder, root, filter));
        }
        if (filter.getTypes() != null) {
            predicates.add(createPredicateTypes(root, filter));
        }
        if (filter.getTitle() != null) {
            predicates.add(createPredicateTitle(builder, root, filter));
        }
        return predicates.toArray(Predicate[]::new);
    }
    
    private static Predicate createPredicateGenresIncl(CriteriaBuilder builder, Root<Movie> root, Filter filter) {
        Predicate predicate1 = root.get("genre1").in((Object[]) filter.getGenresIncl());
        Predicate predicate2 = root.get("genre2").in((Object[]) filter.getGenresIncl());
        Predicate predicate3 = root.get("genre3").in((Object[]) filter.getGenresIncl());
        return builder.or(predicate1, predicate2, predicate3);
    }

    private static Predicate createPredicateGenresExcl(CriteriaBuilder builder, Root<Movie> root, Filter filter) {
        Predicate predicate1 = root.get("genre1").in((Object[]) filter.getGenresExcl()).not();
        Predicate predicate2 = root.get("genre2").in((Object[]) filter.getGenresExcl()).not();
        Predicate predicate3 = root.get("genre3").in((Object[]) filter.getGenresExcl()).not();
        return builder.and(predicate1, predicate2, predicate3);
    }

    private static Predicate createPredicateCountriesIncl(CriteriaBuilder builder, Root<Movie> root, Filter filter) {
        Predicate predicate1 = root.get("counties1").in((Object[]) filter.getCountriesIncl());
        Predicate predicate2 = root.get("counties2").in((Object[]) filter.getCountriesIncl());
        Predicate predicate3 = root.get("counties3").in((Object[]) filter.getCountriesIncl());
        return builder.or(predicate1, predicate2, predicate3);
    }

    private static Predicate createPredicateCountriesExcl(CriteriaBuilder builder, Root<Movie> root, Filter filter) {
        Predicate predicate1 = root.get("counties1").in((Object[]) filter.getCountriesExcl()).not();
        Predicate predicate2 = root.get("counties2").in((Object[]) filter.getCountriesExcl()).not();
        Predicate predicate3 = root.get("counties3").in((Object[]) filter.getCountriesExcl()).not();
        return builder.and(predicate1, predicate2, predicate3);
    }

    private static Predicate createPredicateFromYear(CriteriaBuilder builder, Root<Movie> root, Filter filter) {
        return builder.greaterThanOrEqualTo(root.get("releaseDate").as(LocalDate.class),
                LocalDate.of(filter.getFromYear(), Month.JANUARY, 1));
    }

    private static Predicate createPredicateToYear(CriteriaBuilder builder, Root<Movie> root, Filter filter) {
        return builder.lessThanOrEqualTo(root.get("releaseDate").as(LocalDate.class),
                LocalDate.of(filter.getFromYear(), Month.JANUARY, 1));
    }

    private static Predicate createPredicateMinImdb(CriteriaBuilder builder, Root<Movie> root, Filter filter) {
        return builder.greaterThanOrEqualTo(root.get("ratingImdb").as(Double.class), filter.getMinIMDB());
    }

    private static Predicate createPredicateMinKp(CriteriaBuilder builder, Root<Movie> root, Filter filter) {
        return builder.greaterThanOrEqualTo(root.get("ratingKp").as(Double.class), filter.getMinKP());
    }

    private static Predicate createPredicateMinVotes(CriteriaBuilder builder, Root<Movie> root, Filter filter) {
        return builder.greaterThanOrEqualTo(root.get("ratingImdbVotes").as(Integer.class), filter.getMinVotes());
    }

    private static Predicate createPredicateTypes(Root<Movie> root, Filter filter) {
        return root.get("type").in((Object[]) filter.getTypes());
    }

    private static Predicate createPredicateTitle(CriteriaBuilder builder, Root<Movie> root, Filter filter) {
        return builder.like(builder.lower(root.get("title")), "%" + filter.getTitle().toLowerCase() + "%");
    }
}
