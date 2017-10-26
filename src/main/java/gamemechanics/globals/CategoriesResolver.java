package gamemechanics.globals;

public final class CategoriesResolver {
    private CategoriesResolver() {}

    public static Boolean meetsCategory(Integer toCheck, Integer referenceValue) {
        return (toCheck & referenceValue) != 0;
    }

    public static Integer categoriesCheckResult(Integer categoryOne, Integer categoryTwo) {
        return categoryOne & categoryTwo;
    }
}
