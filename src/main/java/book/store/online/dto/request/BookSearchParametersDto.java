package book.store.online.dto.request;

public record BookSearchParametersDto(String[] title, String[] author,
                                      String[] isbn, String[] description) {
}
